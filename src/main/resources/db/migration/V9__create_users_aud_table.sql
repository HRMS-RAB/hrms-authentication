/* Create audit table for users if it doesn’t exist */
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_name = 'users_aud'
    ) THEN
        CREATE TABLE users_aud (
            user_id      BIGINT      NOT NULL,
            rev          INT         NOT NULL,
            revtype      SMALLINT,

            employee_id  BIGINT,
            dept_id      BIGINT,
            dept_name    VARCHAR(255),
            full_name    VARCHAR(255),
            work_email   VARCHAR(100),
            password     VARCHAR(255),
            grade_level  VARCHAR(50),
            is_admin     BOOLEAN,
            active       BOOLEAN,

            CONSTRAINT pk_users_aud PRIMARY KEY (user_id, rev),
            CONSTRAINT fk_users_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (rev)
        );
    END IF;
END$$;
