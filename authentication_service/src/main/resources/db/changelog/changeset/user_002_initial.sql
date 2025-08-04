INSERT INTO users (username, password, email, role)
VALUES
    ('admin_user', 'hashed_password_1', 'admin@example.com', 'ADMIN'),
    ('john_doe', 'hashed_password_2', 'john@example.com', 'USER'),
    ('jane_smith', 'hashed_password_3', 'jane@example.com', 'USER');

INSERT INTO tokens (username, refresh_token)
VALUES
    ((SELECT id FROM users WHERE username = 'admin_user'), 'refresh-token-uuid-1111'),
    ((SELECT id FROM users WHERE username = 'john_doe'), 'refresh-token-uuid-2222'),
    ((SELECT id FROM users WHERE username = 'jane_smith'), 'refresh-token-uuid-3333');