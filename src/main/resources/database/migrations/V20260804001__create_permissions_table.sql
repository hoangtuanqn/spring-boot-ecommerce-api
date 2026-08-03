CREATE TABLE permissions (
    id          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    resource    VARCHAR(100) NOT NULL,
    action      VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    UNIQUE KEY uq_resource_action (resource, action)
)