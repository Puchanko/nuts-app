INSERT INTO users (
    email,
    password_hash,
    display_name
)
VALUES (
    'admin@example.com',
    '{bcrypt}生成したハッシュ値',
    '管理者ユーザー'
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
WHERE u.email = 'admin@example.com'
  AND r.role_code IN ('USER', 'ADMIN');
