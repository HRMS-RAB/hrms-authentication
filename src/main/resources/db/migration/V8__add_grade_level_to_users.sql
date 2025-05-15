-- V<N>__add_grade_level_to_users.sql
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'users'
          AND column_name = 'grade_level'
    ) THEN
        ALTER TABLE users
        ADD COLUMN grade_level VARCHAR(50);
    END IF;
END$$;
