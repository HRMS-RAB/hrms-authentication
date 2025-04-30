-- file: V7__create_revinfo.sql   (Flyway)  OR paste once in pgAdmin, **but run against hrms_auth_db**

-- 1) create a separate sequence (do NOTHING if it already exists)
CREATE SEQUENCE IF NOT EXISTS revinfo_seq
  START WITH 1
  INCREMENT BY 1;

-- 2) create the table that Envers expects (if it isn’t there)
CREATE TABLE IF NOT EXISTS revinfo (
  rev      BIGINT PRIMARY KEY DEFAULT nextval('revinfo_seq'),
  revtstmp BIGINT
);

-- 3) (optional) bind sequence ownership so a drop cascades cleanly
ALTER SEQUENCE revinfo_seq OWNED BY revinfo.rev;
