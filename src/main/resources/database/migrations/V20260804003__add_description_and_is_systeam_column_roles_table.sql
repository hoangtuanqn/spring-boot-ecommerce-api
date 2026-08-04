ALTER TABLE roles
    ADD description VARCHAR(255) AFTER `name`,
    ADD is_system TINYINT(1) DEFAULT 0 AFTER description;