
CREATE TABLE IF NOT EXISTS users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_type     VARCHAR(20)  NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(255) NOT NULL,
    created_at    DATETIME     NOT NULL,
    CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE INDEX idx_users_user_type ON users (user_type);

CREATE TABLE IF NOT EXISTS students (
    id                      BIGINT PRIMARY KEY,
    interests_json          TEXT,
    personality_scores_json TEXT,
    academic_scores_json    TEXT,
    profile_embedding       TEXT,
    assessment_date         DATE,
    CONSTRAINT fk_students_user FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS admins (
    id BIGINT PRIMARY KEY,
    CONSTRAINT fk_admins_user FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS fields (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                 VARCHAR(255) NOT NULL,
    description          TEXT,
    required_traits_json TEXT,
    category             VARCHAR(100),
    related_subjects     VARCHAR(500),
    field_embedding      TEXT
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE INDEX idx_fields_category ON fields (category);

CREATE TABLE IF NOT EXISTS counselors (
    id                 BIGINT PRIMARY KEY,
    bio                TEXT,
    specialty_field_id BIGINT,
    CONSTRAINT fk_counselors_user FOREIGN KEY (id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_counselors_specialty_field FOREIGN KEY (specialty_field_id) REFERENCES fields (id) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS recommendations (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id  BIGINT   NOT NULL,
    field_id    BIGINT   NOT NULL,
    score       DOUBLE   NOT NULL,
    explanation TEXT,
    created_at  DATETIME NOT NULL,
    CONSTRAINT fk_recommendations_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT fk_recommendations_field   FOREIGN KEY (field_id)   REFERENCES fields (id)   ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE INDEX idx_recommendations_student ON recommendations (student_id);
CREATE INDEX idx_recommendations_score   ON recommendations (score DESC);

CREATE TABLE IF NOT EXISTS mentorship_sessions (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id   BIGINT      NOT NULL,
    counselor_id BIGINT      NOT NULL,
    status       VARCHAR(20) NOT NULL,
    scheduled_at DATETIME,
    created_at   DATETIME    NOT NULL,
    CONSTRAINT fk_mentorship_sessions_student   FOREIGN KEY (student_id)   REFERENCES students  (id) ON DELETE CASCADE,
    CONSTRAINT fk_mentorship_sessions_counselor FOREIGN KEY (counselor_id) REFERENCES counselors (id) ON DELETE CASCADE,
    CONSTRAINT ck_mentorship_sessions_status    CHECK (status IN ('REQUESTED', 'SCHEDULED', 'COMPLETED'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE INDEX idx_mentorship_sessions_student   ON mentorship_sessions (student_id);
CREATE INDEX idx_mentorship_sessions_counselor ON mentorship_sessions (counselor_id);
