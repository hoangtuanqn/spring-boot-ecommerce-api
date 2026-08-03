ALTER TABLE users
    ADD role_id BIGINT UNSIGNED AFTER user_catalogues_id,
    ADD CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE;