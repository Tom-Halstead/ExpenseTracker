
-- Insert test data into the "user" table
INSERT INTO "user" (cognito_uuid, username, email, role, status)
VALUES
    ('uuid-1234', 'johndoe', 'johndoe@example.com', 'User', 'Active'),
    ('uuid-5678', 'janedoe', 'janedoe@example.com', 'User', 'Active'),
    ('uuid-9101', 'mikejohnson', 'mikej@example.com', 'User', 'Active'),
    ('uuid-1213', 'lisabrown', 'lisab@example.com', 'User', 'Inactive'),
    ('uuid-1415', 'alexwhite', 'alexw@example.com', 'User', 'Active');

-- Insert test data into the "category" table
INSERT INTO category (user_id, name, type, description)
VALUES
    (1, 'Food', 'Expense', 'All grocery and dining expenses'),
    (1, 'Rent', 'Expense', 'Monthly rent payments'),
    (1, 'Utilities', 'Expense', 'Electricity, water, internet bills'),
    (2, 'Entertainment', 'Expense', 'Movies, concerts, events'),
    (2, 'Transportation', 'Expense', 'Gas, public transport, vehicle maintenance'),
    (1, 'Salary', 'Income', 'Monthly salary from job'),
    (2, 'Freelance', 'Income', 'Income from freelance projects'),
    (1, 'Investments', 'Income', 'Income from stock dividends or interest');

-- Insert test data into the "expense" table
INSERT INTO expense (user_id, description, amount, date, category_id)
VALUES
    (1, 'Grocery shopping', 50.00, '2024-09-01', 1),   -- Food for user 1
    (1, 'September rent', 1200.00, '2024-09-01', 2),  -- Rent for user 1
    (1, 'Electricity bill', 75.00, '2024-09-05', 3),  -- Utilities for user 1
    (2, 'Movie tickets', 20.00, '2024-09-10', 4),     -- Entertainment for user 2
    (2, 'Gas for car', 40.00, '2024-09-12', 5);       -- Transportation for user 2

-- Insert test data into the "budget" table
INSERT INTO budget (user_id, month, year, amount, category_id)
VALUES
    (1, 9, 2024, 500.00, 1),  -- Budget for 'Food' in September 2024 for user 1
    (1, 9, 2024, 1200.00, 2), -- Budget for 'Rent' in September 2024 for user 1
    (1, 9, 2024, 150.00, 3),  -- Budget for 'Utilities' in September 2024 for user 1
    (2, 9, 2024, 300.00, 4),  -- Budget for 'Entertainment' in September 2024 for user 2
    (2, 9, 2024, 200.00, 5);  -- Budget for 'Transportation' in September 2024 for user 2

-- Insert test data into the "income" table
INSERT INTO income (user_id, amount, date, source, category_id)
VALUES
    (1, 3000.00, '2024-09-01', 'September salary', 6),  -- Salary for user 1
    (2, 500.00, '2024-09-05', 'Freelance project', 7),  -- Freelance for user 2
    (1, 200.00, '2024-09-10', 'Stock dividends', 8);   -- Investments for user 1
