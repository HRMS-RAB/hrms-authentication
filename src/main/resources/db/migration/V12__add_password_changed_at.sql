--  <<< CHANGED >>>  (new migration)
ALTER TABLE users
ADD COLUMN IF NOT EXISTS password_changed_at TIMESTAMP;
