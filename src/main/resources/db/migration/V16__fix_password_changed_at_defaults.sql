-- 1) back-fill existing users created before the column was enforced
UPDATE users
SET    password_changed_at = NOW()
WHERE  password_changed_at IS NULL;

-- 2) enforce NOT NULL and default for all future inserts
ALTER TABLE users
    ALTER COLUMN password_changed_at SET NOT NULL,
    ALTER COLUMN password_changed_at SET DEFAULT NOW();

-- do the same for the audit table
UPDATE users_aud
SET    password_changed_at = NOW()
WHERE  password_changed_at IS NULL;

ALTER TABLE users_aud
    ALTER COLUMN password_changed_at SET NOT NULL,
    ALTER COLUMN password_changed_at SET DEFAULT NOW();
