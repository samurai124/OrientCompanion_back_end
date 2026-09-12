
CREATE TABLE IF NOT EXISTS schools (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    city        VARCHAR(150),
    country     VARCHAR(100),
    type        VARCHAR(100),
    website     VARCHAR(500),
    description TEXT,
    field_id    BIGINT NOT NULL,
    CONSTRAINT fk_schools_field FOREIGN KEY (field_id) REFERENCES fields (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE INDEX idx_schools_field ON schools (field_id);
