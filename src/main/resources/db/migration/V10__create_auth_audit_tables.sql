/* ──────────────────────────────────────────────────────────────
   Envers audit tables for hrms-authentication
   • revinfo            (already exists; leave untouched)
   • roles_aud          (audit for roles)
   • user_roles_aud     (audit for join table)
   ────────────────────────────────────────────────────────────── */

--------------------- roles_aud ---------------------
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_name = 'roles_aud'
    ) THEN
        CREATE TABLE roles_aud (
            role_id   INT         NOT NULL,
            rev       INT         NOT NULL,
            revtype   SMALLINT,
            name      VARCHAR(100),
            description VARCHAR(255),

            CONSTRAINT pk_roles_aud PRIMARY KEY (role_id, rev),
            CONSTRAINT fk_roles_aud_rev FOREIGN KEY (rev)
                REFERENCES revinfo (rev)
        );
    END IF;
END$$;

--------------------- user_roles_aud ---------------------
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_name = 'user_roles_aud'
    ) THEN
        CREATE TABLE user_roles_aud (
            user_id BIGINT  NOT NULL,
            role_id INT     NOT NULL,
            rev     INT     NOT NULL,
            revtype SMALLINT,

            CONSTRAINT pk_user_roles_aud PRIMARY KEY (user_id, role_id, rev),
            CONSTRAINT fk_user_roles_aud_rev  FOREIGN KEY (rev)
                     REFERENCES revinfo (rev),
            CONSTRAINT fk_user_roles_aud_user FOREIGN KEY (user_id)
                     REFERENCES users (user_id),
            CONSTRAINT fk_user_roles_aud_role FOREIGN KEY (role_id)
                     REFERENCES roles (id)        -- ← fixed: roles.id is PK
        );
    END IF;
END$$;
