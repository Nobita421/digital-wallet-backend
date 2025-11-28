-- Add password column to users table if it doesn't exist
ALTER TABLE users ADD COLUMN IF NOT EXISTS password VARCHAR(255);

-- Create user_roles table if it doesn't exist
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Update existing users with a default password (hashed 'password123') and role
-- $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRkgVduVfzCore3fakZ8.N8wDyq is 'password123'
UPDATE users SET password = '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRkgVduVfzCore3fakZ8.N8wDyq' WHERE password IS NULL;

-- Insert roles for existing users if they don't have any roles assigned yet
INSERT INTO user_roles (user_id, role) 
SELECT id, 'ROLE_USER' FROM users u
WHERE NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id);
