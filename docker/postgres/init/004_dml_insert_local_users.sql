INSERT INTO users (
    email,
    password_hash,
    display_name
)
VALUES (
    'user@example.com',
    '{bcrypt}$2a$10$ku4OW00cjuwDaTypfFh9UepU8dD6IaR9bllZyy2jZPzw3JCVRdNVO',
    '一般ユーザー'
);

INSERT INTO user_roles (
    user_id,
    role_id
)
SELECT
    u.id,
    r.id
FROM users AS u
CROSS JOIN roles AS r
WHERE u.email = 'user@example.com'
  AND r.role_code = 'USER';
