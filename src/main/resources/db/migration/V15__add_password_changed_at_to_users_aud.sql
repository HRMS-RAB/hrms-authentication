-- Adds the missing column to the Envers audit table
ALTER TABLE users_aud
    ADD COLUMN IF NOT EXISTS password_changed_at TIMESTAMP;
