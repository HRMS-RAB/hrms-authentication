CREATE TABLE password_reset_token (
    id           BIGSERIAL PRIMARY KEY,
    token        VARCHAR(100) NOT NULL,
    user_id      BIGINT       NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    expires_at   TIMESTAMP    NOT NULL,
    used         BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE UNIQUE INDEX idx_reset_token_token ON password_reset_token(token);
