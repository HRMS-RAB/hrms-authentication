-- Adds expiry timestamp to password reset tokens
ALTER TABLE password_reset_token
    ADD COLUMN IF NOT EXISTS expiry TIMESTAMP;
