# OrientCompanion — UML Diagrams

> [!NOTE]
> This document covers: **Sequence Diagrams** (5 flows), a **Class Diagram** (domain model + services), and a **Use Case Diagram** (actors & system features).

---

## 1. User Registration

```mermaid
sequenceDiagram
    actor Client
    participant AuthController
    participant UserService
    participant UserRepository
    participant PasswordEncoder
    participant JwtService
    participant Redis

    Client->>AuthController: POST /api/auth/register {email, password, role, fullName}
    AuthController->>UserService: register(RegisterRequest)
    UserService->>UserRepository: existsByEmail(email)
    UserRepository-->>UserService: false
    UserService->>PasswordEncoder: encode(password)
    PasswordEncoder-->>UserService: encodedPassword
    UserService->>UserService: createUserInstance(role) → Student/Counselor/Admin
    UserService->>UserRepository: save(user)
    UserRepository-->>UserService: savedUser
    UserService->>JwtService: generateToken(savedUser)
    JwtService-->>UserService: JWT token
    UserService->>Redis: CacheEvict users_email[email]
    UserService-->>AuthController: AuthResponse{token, userInfo}
    AuthController-->>Client: 201 Created {token, user}
```

---

## 2. Login & JWT Authentication Filter

```mermaid
sequenceDiagram
    actor Client
    participant JwtAuthFilter
    participant AuthController
    participant UserService
    participant AuthManager
    participant JwtService
    participant Redis

    Client->>JwtAuthFilter: POST /api/auth/login {email, password}
    JwtAuthFilter->>JwtAuthFilter: No JWT header → pass through
    JwtAuthFilter->>AuthController: forward request
    AuthController->>UserService: login(LoginRequest)
    UserService->>AuthManager: authenticate(email, password)
    AuthManager-->>UserService: Authentication OK
    UserService->>Redis: Cacheable users_email[email]?
    Redis-->>UserService: cache miss
    UserService->>UserService: findByEmail(email)
    UserService->>JwtService: generateToken(user)
    JwtService-->>UserService: JWT token
    UserService-->>AuthController: AuthResponse{token, userInfo}
    AuthController-->>Client: 200 OK {token, user}

    Note over Client,JwtAuthFilter: Subsequent protected requests

    Client->>JwtAuthFilter: GET /api/... Authorization: Bearer <token>
    JwtAuthFilter->>JwtService: extractUsername(token)
    JwtService-->>JwtAuthFilter: email
    JwtAuthFilter->>UserService: findByEmail(email) [Redis cache]
    UserService-->>JwtAuthFilter: User principal
    JwtAuthFilter->>JwtService: isTokenValid(token, user)
    JwtService-->>JwtAuthFilter: true
    JwtAuthFilter->>JwtAuthFilter: set SecurityContext
    JwtAuthFilter->>AuthController: forward authenticated request
```

---

## 3. Assessment Submission

```mermaid
sequenceDiagram
    actor Student
    participant AssessmentController
    participant AssessmentService
    participant StudentRepository
    participant EmbeddingService
    participant GeminiAPI
    participant RecommendationService
    participant Redis

    Student->>AssessmentController: POST /api/student/assessment {interests, personalityScores, academicScores}
    Note over AssessmentController: @PreAuthorize STUDENT/ADMIN
    AssessmentController->>AssessmentService: submitAssessment(studentId, request)
    AssessmentService->>StudentRepository: findById(studentId)
    StudentRepository-->>AssessmentService: Student entity
    AssessmentService->>AssessmentService: update interests, personality, academic JSON fields
    AssessmentService->>AssessmentService: buildProfileText(request)
    AssessmentService->>EmbeddingService: embed(profileText)
    EmbeddingService->>Redis: Cacheable embeddings[text]?
    Redis-->>EmbeddingService: cache miss
    EmbeddingService->>GeminiAPI: POST /embeddings {model, input}
    GeminiAPI-->>EmbeddingService: float[] embedding vector
    EmbeddingService-->>AssessmentService: float[] embedding
    AssessmentService->>AssessmentService: EmbeddingCodec.toJson(embedding)
    AssessmentService->>StudentRepository: save(student)
    StudentRepository-->>AssessmentService: saved Student
    AssessmentService->>RecommendationService: generateRecommendations(student)
    Note over RecommendationService: See Diagram 4
    RecommendationService-->>AssessmentService: List<RecommendationResponse>
    AssessmentService->>Redis: CacheEvict student_profiles[studentId]
    AssessmentService-->>AssessmentController: StudentProfileResponse
    AssessmentController-->>Student: 200 OK {profile}
```

---

## 4. Recommendation Generation

```mermaid
sequenceDiagram
    participant AssessmentService
    participant RecommendationService
    participant FieldRepository
    participant RecommendationRepository
    participant ScoringService
    participant EmbeddingCodec
    participant VectorUtils
    participant LlmExplanationService
    participant GeminiAPI
    participant SchoolService
    participant Redis

    AssessmentService->>RecommendationService: generateRecommendations(student)
    RecommendationService->>Redis: CacheEvict recommendations[studentId]
    RecommendationService->>FieldRepository: findAll()
    FieldRepository-->>RecommendationService: List<Field>
    RecommendationService->>RecommendationRepository: deleteByStudentId(studentId)

    loop For each Field
        RecommendationService->>ScoringService: calculateStructuredScore(student, field)
        ScoringService->>ScoringService: interest score (weighted avg on related subjects)
        ScoringService->>ScoringService: academic score (avg/20 * 100)
        ScoringService->>ScoringService: personality score (RIASEC weighted match)
        ScoringService-->>RecommendationService: structuredScore (0–100)

        RecommendationService->>EmbeddingCodec: fromJson(studentEmbedding)
        RecommendationService->>EmbeddingCodec: fromJson(fieldEmbedding)
        RecommendationService->>VectorUtils: cosineSimilarity(studentVec, fieldVec)
        VectorUtils-->>RecommendationService: similarity (-1..1)
        RecommendationService->>RecommendationService: fusedScore = 0.60*structured + 0.40*vector

        RecommendationService->>LlmExplanationService: generateExplanation(student, field, score)
        LlmExplanationService->>Redis: Cacheable llm_explanations[studentId:fieldId:score]?
        Redis-->>LlmExplanationService: cache miss
        LlmExplanationService->>GeminiAPI: POST /chat/completions {prompt}
        GeminiAPI-->>LlmExplanationService: explanation text
        LlmExplanationService-->>RecommendationService: explanation string
    end

    RecommendationService->>RecommendationService: sort by score DESC
    RecommendationService->>RecommendationRepository: saveAll(recommendations)
    RecommendationRepository-->>RecommendationService: saved List<Recommendation>

    loop Enrich each recommendation
        RecommendationService->>SchoolService: findByFieldId(fieldId)
        SchoolService-->>RecommendationService: List<SchoolResponse>
    end

    RecommendationService-->>AssessmentService: List<RecommendationResponse>
```

---

## 5. Mentorship Session Lifecycle

```mermaid
sequenceDiagram
    actor Student
    actor Counselor
    participant MentorshipController
    participant MentorshipService
    participant CounselorRepository
    participant StudentRepository
    participant MentorshipSessionRepository
    participant Redis

    Note over Student,MentorshipController: Step 1 — Browse available counselors

    Student->>MentorshipController: GET /api/student/mentorship/counselors?fieldId=3
    MentorshipController->>MentorshipService: findAllCounselors(fieldId)
    MentorshipService->>CounselorRepository: findBySpecialtyField_Id(fieldId)
    CounselorRepository-->>MentorshipService: List<Counselor>
    MentorshipService-->>MentorshipController: List<CounselorProfileResponse>
    MentorshipController-->>Student: 200 OK [counselors]

    Note over Student,MentorshipController: Step 2 — Request a session

    Student->>MentorshipController: POST /api/student/mentorship/sessions {counselorId}
    MentorshipController->>MentorshipService: requestSession(studentId, counselorId)
    MentorshipService->>StudentRepository: findById(studentId)
    StudentRepository-->>MentorshipService: Student
    MentorshipService->>CounselorRepository: findById(counselorId)
    CounselorRepository-->>MentorshipService: Counselor
    MentorshipService->>MentorshipSessionRepository: save(session{REQUESTED})
    MentorshipSessionRepository-->>MentorshipService: saved session
    MentorshipService->>Redis: CacheEvict mentorship_student + mentorship_counselor
    MentorshipService-->>MentorshipController: MentorshipSessionResponse
    MentorshipController-->>Student: 201 Created {session}

    Note over Counselor,MentorshipController: Step 3 — Counselor updates session status

    Counselor->>MentorshipController: PATCH /api/counselor/mentorship/sessions/{id} {status, scheduledAt}
    MentorshipController->>MentorshipService: updateStatus(sessionId, counselorId, request)
    MentorshipService->>MentorshipSessionRepository: findById(sessionId)
    MentorshipSessionRepository-->>MentorshipService: MentorshipSession
    MentorshipService->>MentorshipService: validate counselorId ownership
    MentorshipService->>MentorshipService: validate scheduledAt if SCHEDULED
    MentorshipService->>MentorshipSessionRepository: save(updated session)
    MentorshipSessionRepository-->>MentorshipService: updated session
    MentorshipService->>Redis: CacheEvict mentorship_student + mentorship_counselor
    MentorshipService-->>MentorshipController: MentorshipSessionResponse
    MentorshipController-->>Counselor: 200 OK {updated session}
```

---

## 6. Class Diagram — Domain Model & Service Layer

```mermaid
classDiagram
    direction TB

    class User {
        <<abstract>>
        +Long id
        +String email
        +String passwordHash
        +String fullName
        +LocalDateTime createdAt
        +getRole() Role
        +getAuthorities() Collection
        +getUsername() String
        +getPassword() String
    }

    class Student {
        +String interestsJson
        +String personalityScoresJson
        +String academicScoresJson
        +String profileEmbedding
        +LocalDate assessmentDate
        +getRole() Role
    }

    class Counselor {
        +String bio
        +Field specialtyField
        +getRole() Role
    }

    class Admin {
        +getRole() Role
    }

    class Field {
        +Long id
        +String name
        +String description
        +String requiredTraitsJson
        +String category
        +String relatedSubjects
        +String fieldEmbedding
    }

    class School {
        +Long id
        +String name
        +String city
        +String country
        +String type
        +String website
        +String description
        +Field field
    }

    class Recommendation {
        +Long id
        +Student student
        +Field field
        +double score
        +String explanation
        +LocalDateTime createdAt
    }

    class MentorshipSession {
        +Long id
        +Student student
        +Counselor counselor
        +SessionStatus status
        +LocalDateTime scheduledAt
        +LocalDateTime createdAt
    }

    class SessionStatus {
        <<enumeration>>
        REQUESTED
        ACCEPTED
        SCHEDULED
        REJECTED
        COMPLETED
    }

    class Role {
        <<enumeration>>
        STUDENT
        COUNSELOR
        ADMIN
    }

    class UserService {
        +register(RegisterRequest) AuthResponse
        +login(LoginRequest) AuthResponse
        +findByEmail(String) User
        +findById(Long) User
    }

    class AssessmentService {
        +submitAssessment(Long, AssessmentRequest) StudentProfileResponse
        +getProfile(Long) StudentProfileResponse
    }

    class RecommendationService {
        +getRecommendations(Long) List~RecommendationResponse~
        +generateRecommendations(Long) List~RecommendationResponse~
        +generateRecommendations(Student) List~RecommendationResponse~
    }

    class ScoringService {
        +calculateStructuredScore(Student, Field) double
    }

    class EmbeddingService {
        +embed(String) float[]
    }

    class LlmExplanationService {
        +generateExplanation(Student, Field, double) String
    }

    class MentorshipService {
        +findAllCounselors(Long) List~CounselorProfileResponse~
        +requestSession(Long, Long) MentorshipSessionResponse
        +findByStudent(Long) List~MentorshipSessionResponse~
        +findByCounselor(Long) List~MentorshipSessionResponse~
        +updateStatus(Long, Long, MentorshipSessionUpdateRequest) MentorshipSessionResponse
    }

    class JwtService {
        +generateToken(User) String
        +extractUsername(String) String
        +isTokenValid(String, User) boolean
    }

    %% Inheritance
    User <|-- Student
    User <|-- Counselor
    User <|-- Admin

    %% Entity associations
    Counselor --> Field : specialtyField
    School --> Field : belongs to
    Recommendation --> Student : for
    Recommendation --> Field : about
    MentorshipSession --> Student : requested by
    MentorshipSession --> Counselor : assigned to
    MentorshipSession --> SessionStatus : has status
    User --> Role : has role

    %% Service dependencies
    UserService --> JwtService : uses
    AssessmentService --> EmbeddingService : uses
    AssessmentService --> RecommendationService : triggers
    RecommendationService --> ScoringService : uses
    RecommendationService --> LlmExplanationService : uses
    MentorshipService --> MentorshipSession : manages
```

---

## 7. Use Case Diagram

```mermaid
flowchart LR
    %% Actors
    S(["👤 Student"])
    C(["🧑‍🏫 Counselor"])
    A(["🔧 Admin"])
    GEM(["🤖 Gemini AI API"])

    %% System boundary
    subgraph OrientCompanion ["OrientCompanion System"]

        subgraph AUTH ["Authentication"]
            UC1["Register account"]
            UC2["Login / get JWT token"]
            UC3["View own profile (me)"]
        end

        subgraph ASSESS ["Assessment"]
            UC4["Submit orientation assessment\n(interests, RIASEC, academic scores)"]
            UC5["View assessment profile"]
        end

        subgraph RECO ["Recommendations"]
            UC6["View field recommendations"]
            UC7["Regenerate recommendations"]
        end

        subgraph MENTOR ["Mentorship"]
            UC8["Browse available counselors"]
            UC9["Request a mentorship session"]
            UC10["View my sessions (as student)"]
            UC11["View my sessions (as counselor)"]
            UC12["Accept / Schedule / Reject session"]
        end

        subgraph ADMIN_UC ["Administration"]
            UC13["Manage fields (CRUD)"]
            UC14["Manage schools (CRUD)"]
            UC15["Access all endpoints"]
        end

        subgraph AI ["AI / External"]
            UC16["Generate profile embedding"]
            UC17["Generate LLM explanation"]
        end
    end

    %% Student use cases
    S --> UC1
    S --> UC2
    S --> UC3
    S --> UC4
    S --> UC5
    S --> UC6
    S --> UC7
    S --> UC8
    S --> UC9
    S --> UC10

    %% Counselor use cases
    C --> UC2
    C --> UC3
    C --> UC11
    C --> UC12

    %% Admin use cases
    A --> UC2
    A --> UC3
    A --> UC13
    A --> UC14
    A --> UC15

    %% AI system interactions
    UC4 --> UC16
    UC7 --> UC17
    UC16 --> GEM
    UC17 --> GEM
```
