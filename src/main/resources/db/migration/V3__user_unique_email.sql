ALTER TABLE users
    ADD CONSTRAINT uk_users_work_email UNIQUE (work_email);
