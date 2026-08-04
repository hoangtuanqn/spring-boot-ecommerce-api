CREATE TABLE user_roles (
    user_id    BIGINT UNSIGNED NOT NULL,
    role_id    BIGINT UNSIGNED NOT NULL,
    granted_by BIGINT UNSIGNED NULL,
    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id)    REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id)    REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (granted_by) REFERENCES users(id) ON DELETE SET NULL
);
