-- Insert sample user (password is bcrypt hash of 'password123')
INSERT INTO users (email, name, phone, password, is_verified) VALUES
('john.doe@example.com', 'John Doe', '+1234567890', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRkgVduVfzCore3fakZ8.N8wDyq', true);

-- Insert wallet for the user
INSERT INTO wallets (user_id, balance, currency) VALUES
(1, 2540.50, 'USD');

-- Insert sample transactions
INSERT INTO transactions (user_id, type, amount, currency, description, status, reference) VALUES
(1, 'DEPOSIT', 1000.00, 'USD', 'Bank Transfer', 'COMPLETED', 'TXN_001'),
(1, 'BILL_PAYMENT', 85.00, 'USD', 'Electricity Bill', 'COMPLETED', 'TXN_002'),
(1, 'WITHDRAWAL', 120.50, 'USD', 'Coffee Shop', 'COMPLETED', 'TXN_003'),
(1, 'DEPOSIT', 500.00, 'USD', 'Payment from Jane Smith', 'COMPLETED', 'TXN_004');