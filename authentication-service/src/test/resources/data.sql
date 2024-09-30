INSERT INTO role (id, name)
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'ADMIN');
INSERT INTO role (id, name)
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d489', 'USER');

INSERT INTO users (id, email, password, created_at, updated_at)
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d481', 'admin@example.com', 'admin123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO users (id, email, password, created_at, updated_at)
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d482', 'user@example.com', 'user123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
