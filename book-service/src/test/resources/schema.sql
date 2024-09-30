CREATE TABLE IF NOT EXISTS books
(
    id          VARCHAR(36) PRIMARY KEY,
    title       VARCHAR(255),
    author      VARCHAR(255),
    genre       VARCHAR(255),
    description VARCHAR(255),
    isbn        VARCHAR(255),
    created_at  DATETIME(6),
    updated_at  DATETIME(6)
);