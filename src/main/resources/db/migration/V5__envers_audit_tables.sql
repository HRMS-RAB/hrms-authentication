-- V5__envers_audit_tables.sql
ALTER SEQUENCE revinfo_seq INCREMENT BY 50;

-- optional: make sure the table exists
CREATE TABLE IF NOT EXISTS revinfo (
    rev        INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('revinfo_seq'),
    rev_tstmp  BIGINT
);
