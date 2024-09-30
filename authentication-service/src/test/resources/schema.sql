CREATE TABLE IF NOT EXISTS role
(
    id   VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users
(
    id         VARCHAR(36) PRIMARY KEY,
    email      VARCHAR(255),
    password   VARCHAR(255),
    created_at DATETIME(6),
    updated_at DATETIME(6)
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id VARCHAR(36),
    role_id VARCHAR(36),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES role (id),
    PRIMARY KEY (user_id, role_id)
);