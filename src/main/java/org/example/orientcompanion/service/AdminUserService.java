package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orientcompanion.dto.AdminAssessmentResponse;
import org.example.orientcompanion.dto.AdminUserResponse;
import org.example.orientcompanion.dto.RegisterRequest;
import org.example.orientcompanion.entity.Recommendation;
import org.example.orientcompanion.entity.Student;
import org.example.orientcompanion.entity.User;
import org.example.orientcompanion.enums.Role;
import org.example.orientcompanion.exception.BusinessException;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.repository.RecommendationRepository;
import org.example.orientcompanion.repository.SchoolRepository;
import org.example.orientcompanion.repository.StudentRepository;
import org.example.orientcompanion.repository.UserRepository;
import org.example.orientcompanion.util.JsonMapCodec;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RecommendationRepository recommendationRepository;
    private final SchoolRepository schoolRepository;

    private static final Map<String, String> RIASEC_TITLES = Map.of(
            "R", "Réaliste",
            "I", "Investigateur",
            "A", "Artistique",
            "S", "Social",
            "E", "Entreprenant",
            "C", "Conventionnel"
    );

    @Transactional(readOnly = true)
    public List<AdminUserResponse> findAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::toAdminUserResponse)
                .toList();
    }

    @Transactional
    public AdminUserResponse createUser(String email, String password, String fullName, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Un utilisateur avec l'email '" + email + "' existe déjà.");
        }
        RegisterRequest req = new RegisterRequest(email, password, fullName, role);
        userService.register(req);
        User created = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Erreur inattendue lors de la création de l'utilisateur."));
        log.info("Utilisateur id={} ({}) créé par l'admin.", created.getId(), role);
        return toAdminUserResponse(created);
    }

    @Transactional
    public void updateStatus(Long id, String status) {
        User user = findUserOrThrow(id);

        boolean newEnabledState = switch (status.toUpperCase()) {
            case "ACTIVE"    -> true;
            case "SUSPENDED" -> false;
            default -> throw new IllegalArgumentException(
                    "Statut invalide : '" + status + "'. Les valeurs acceptées sont ACTIVE et SUSPENDED."
            );
        };

        user.setEnabled(newEnabledState);
        userRepository.save(user);
        log.info("Statut du compte de l'utilisateur id={} mis à jour → {}", id, status.toUpperCase());
    }

    @Transactional
    public void resetPassword(Long id, String newPassword) {
        User user = findUserOrThrow(id);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Mot de passe de l'utilisateur id={} réinitialisé par un administrateur.", id);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUserOrThrow(id);
        userRepository.delete(user);
        log.info("Utilisateur id={} supprimé par l'admin.", id);
    }

    @Transactional(readOnly = true)
    public List<AdminAssessmentResponse> findAllAssessments() {
        return studentRepository.findAllAssessedStudents()
                .stream()
                .map(this::toAssessmentResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AdminAssessmentResponse> findRecentAssessments(int page, int size) {
        return studentRepository.findAllByAssessmentDateIsNotNullOrderByAssessmentDateDesc(
                        PageRequest.of(page, size))
                .stream()
                .map(this::toAssessmentResponse)
                .toList();
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Utilisateur introuvable avec l'identifiant : " + id
                ));
    }

    private AdminUserResponse toAdminUserResponse(User user) {
        AdminUserResponse.AdminUserResponseBuilder builder = AdminUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt());

        if (user instanceof Student s) {
            builder.assessmentDate(s.getAssessmentDate())
                    .assessmentDone(s.getAssessmentDate() != null && s.getPersonalityScoresJson() != null);
        }
        return builder.build();
    }

    private AdminAssessmentResponse toAssessmentResponse(Student s) {
        Map<String, Double> scores = JsonMapCodec.fromJson(s.getPersonalityScoresJson());
        String dominantCode = null;
        String dominantTitle = null;
        if (scores != null && !scores.isEmpty()) {
            dominantCode = scores.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
            if (dominantCode != null) {
                dominantTitle = RIASEC_TITLES.getOrDefault(dominantCode, dominantCode);
            }
        }

        List<Recommendation> recs = recommendationRepository.findByStudentIdOrderByScoreDesc(s.getId());
        List<AdminAssessmentResponse.TopRecommendationDTO> topRecs = recs.stream()
                .limit(3)
                .map(r -> {
                    String fieldName = r.getField() != null ? r.getField().getName() : "—";
                    String schoolName = "Établissement partenaire";
                    if (r.getField() != null) {
                        var schools = schoolRepository.findByFieldId(r.getField().getId());
                        if (!schools.isEmpty()) {
                            schoolName = schools.get(0).getName();
                        }
                    }
                    String matchStr = Math.round(r.getScore() * 100) + "%";
                    return AdminAssessmentResponse.TopRecommendationDTO.builder()
                            .field(fieldName)
                            .school(schoolName)
                            .match(matchStr)
                            .build();
                })
                .toList();

        String aiSummary = (!recs.isEmpty() && recs.get(0).getExplanation() != null)
                ? recs.get(0).getExplanation()
                : null;

        String status = (s.getAssessmentDate() != null && s.getPersonalityScoresJson() != null)
                ? "VALIDATED"
                : "PENDING_REVIEW";

        return AdminAssessmentResponse.builder()
                .id(s.getId())
                .studentId(s.getId())
                .studentName(s.getFullName())
                .studentEmail(s.getEmail())
                .assessmentDate(s.getAssessmentDate())
                .embeddingComputed(s.getProfileEmbedding() != null)
                .dominantCode(dominantCode)
                .dominantTitle(dominantTitle)
                .status(status)
                .scores(scores)
                .topRecommendations(topRecs)
                .aiSummary(aiSummary)
                .build();
    }
}
