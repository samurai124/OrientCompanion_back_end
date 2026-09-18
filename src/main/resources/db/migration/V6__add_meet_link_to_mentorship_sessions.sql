ALTER TABLE mentorship_sessions
    ADD COLUMN meet_link VARCHAR(500) NULL AFTER scheduled_at;
