-- Insert sample bills
INSERT INTO bills (user_id, name, amount, currency, due_date, category, description, is_recurring, status) VALUES
(1, 'Electricity Bill', 85.00, 'USD', CURRENT_DATE + INTERVAL '7 days', 'Utilities', 'Monthly electricity bill', true, 'PENDING'),
(1, 'Internet Bill', 60.00, 'USD', CURRENT_DATE + INTERVAL '14 days', 'Utilities', 'Monthly internet service', true, 'PENDING'),
(1, 'Credit Card Payment', 250.00, 'USD', CURRENT_DATE + INTERVAL '21 days', 'Finance', 'Monthly credit card payment', true, 'PENDING');

-- Insert sample budgets
INSERT INTO budgets (user_id, name, amount, currency, category, period, start_date, end_date, spent_amount, is_active) VALUES
(1, 'Groceries Budget', 500.00, 'USD', 'Food', 'MONTHLY', CURRENT_DATE, CURRENT_DATE + INTERVAL '1 month', 120.50, true),
(1, 'Entertainment Budget', 200.00, 'USD', 'Entertainment', 'MONTHLY', CURRENT_DATE, CURRENT_DATE + INTERVAL '1 month', 45.00, true),
(1, 'Transportation Budget', 150.00, 'USD', 'Transportation', 'MONTHLY', CURRENT_DATE, CURRENT_DATE + INTERVAL '1 month', 25.75, true);
