/* ==============================================================
   V6__seed_admin_and_employee_rule.sql
   (merges old V6 + V7 in one safe, idempotent run)
   ============================================================== */

---------------------------
-- 0) Rename column if needed
---------------------------
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_name = 'users' AND column_name = 'emp_id')
     AND NOT EXISTS (SELECT 1 FROM information_schema.columns
                     WHERE table_name = 'users' AND column_name = 'employee_id')
  THEN
     ALTER TABLE users RENAME COLUMN emp_id TO employee_id;
  END IF;
END$$;

---------------------------
-- 1) Allow NULLs for admins
---------------------------
ALTER TABLE users
  ALTER COLUMN employee_id DROP NOT NULL;

---------------------------
-- 2) Add is_admin boolean
---------------------------
ALTER TABLE users
  ADD COLUMN IF NOT EXISTS is_admin BOOLEAN DEFAULT FALSE;

---------------------------
-- 3) Unique index on non-null employee_id
---------------------------
CREATE UNIQUE INDEX IF NOT EXISTS uq_users_employee_id_nonnull
  ON users (employee_id)
  WHERE employee_id IS NOT NULL;

---------------------------
-- 4) Trigger to enforce rule
---------------------------
CREATE OR REPLACE FUNCTION enforce_employee_id() RETURNS TRIGGER AS $$
BEGIN
  IF NEW.is_admin IS FALSE AND NEW.employee_id IS NULL THEN
     RAISE EXCEPTION
       'employee_id cannot be NULL for non-admin users (user_id=%)', NEW.user_id;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_enforce_employee_id ON users;
CREATE TRIGGER trg_enforce_employee_id
  BEFORE INSERT OR UPDATE ON users
  FOR EACH ROW EXECUTE FUNCTION enforce_employee_id();

---------------------------
-- 5) Seed HR_ADMIN role
---------------------------
INSERT INTO roles (id, name, description)
VALUES (1, 'HR_ADMIN', 'Full HR administrator')
ON CONFLICT (name) DO NOTHING;

---------------------------
-- 6) Seed platform admin user
---------------------------
INSERT INTO users (user_id, employee_id, work_email, "password",
                   is_admin, active, full_name)
VALUES (1000, NULL, 'admin@hrms.local',
        '$2a$10$Qz66E1T1FCuHojJX2KxhE.D/7W0.DWeRVah.YssmJoL5Y3ldD9hmS', -- bcrypt "Admin@123"
        TRUE, TRUE, 'Platform Admin')
ON CONFLICT (work_email) DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.id
FROM users u, roles r
WHERE u.work_email = 'admin@hrms.local'
  AND r.name = 'HR_ADMIN'
ON CONFLICT DO NOTHING;
