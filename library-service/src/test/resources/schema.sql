CREATE TABLE IF NOT EXISTS orders
(
    id         VARCHAR(36) PRIMARY KEY NOT NULL,
    book_id    VARCHAR(36),
    expire_in  DATETIME(6),
    order_in   DATETIME(6),
    created_at DATETIME(6),
    updated_at DATETIME(6)
);