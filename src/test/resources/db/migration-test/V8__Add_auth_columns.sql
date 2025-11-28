-- Create user_roles table if it doesn't exist
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert roles for existing users if they don't have any roles assigned yet
INSERT INTO user_roles (user_id, role) 
SELECT id, 'ROLE_USER' FROM users u
WHERE NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id);
