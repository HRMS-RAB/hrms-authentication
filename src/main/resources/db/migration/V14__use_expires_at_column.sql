-- Copy any data that might be in the new "expiry" column back to "expires_at"
ALTER TABLE password_reset_token
    ADD COLUMN IF NOT EXISTS expires_at TIMESTAMP;

UPDATE password_reset_token
SET    expires_at = expiry
WHERE  expires_at IS NULL
  AND  expiry     IS NOT NULL;

-- Drop the accidental column
ALTER TABLE password_reset_token
    DROP COLUMN IF EXISTS expiry;
