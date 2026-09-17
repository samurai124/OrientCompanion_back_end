-- V4: Add 'enabled' column to users table for account suspension/activation management
--
-- Default value is TRUE (1) so that all existing accounts remain active after migration.

ALTER TABLE users
    ADD COLUMN enabled TINYINT(1) NOT NULL DEFAULT 1
        COMMENT 'Account status: 1 = ACTIVE, 0 = SUSPENDED';
