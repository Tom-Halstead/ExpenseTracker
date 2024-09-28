INSERT INTO "user" (username, first_name, last_name, email, password_hash, is_active)
VALUES
    ('johndoe', 'John', 'Doe', 'johndoe@example.com', 'hashedpassword1', TRUE),
    ('janedoe', 'Jane', 'Doe', 'janedoe@example.com', 'hashedpassword2', TRUE),
    ('mikejohnson', 'Mike', 'Johnson', 'mikej@example.com', 'hashedpassword3', TRUE),
    ('lisabrown', 'Lisa', 'Brown', 'lisab@example.com', 'hashedpassword4', FALSE),
    ('alexwhite', 'Alex', 'White', 'alexw@example.com', 'hashedpassword5', TRUE);


INSERT INTO category ("name", "type", description, user_id)
VALUES
    ('Food', 'Expense', 'All grocery and dining expenses', 1),
    ('Rent', 'Expense', 'Monthly rent payments', 1),
    ('Utilities', 'Expense', 'Electricity, water, internet bills', 1),
    ('Entertainment', 'Expense', 'Movies, concerts, events', 2),
    ('Transportation', 'Expense', 'Gas, public transport, vehicle maintenance', 2),
    ('Salary', 'Income', 'Monthly salary from job', 1),
    ('Freelance', 'Income', 'Income from freelance projects', 2),
    ('Investments', 'Income', 'Income from stock dividends or interest', 1);


INSERT INTO expense (amount, date, description, category_id, user_id)
VALUES
    (50.00, '2024-09-01', 'Grocery shopping', 1, 1),   -- Food for user 1
    (1200.00, '2024-09-01', 'September rent', 2, 1),   -- Rent for user 1
    (75.00, '2024-09-05', 'Electricity bill', 3, 1),   -- Utilities for user 1
    (20.00, '2024-09-10', 'Movie tickets', 4, 2),      -- Entertainment for user 2
    (40.00, '2024-09-12', 'Gas for car', 5, 2);        -- Transportation for user 2


INSERT INTO budget ("month", "year", amount, category_id, user_id)
VALUES
    (9, 2024, 500.00, 1, 1),  -- Budget for 'Food' in September 2024 for user 1
    (9, 2024, 1200.00, 2, 1), -- Budget for 'Rent' in September 2024 for user 1
    (9, 2024, 150.00, 3, 1),  -- Budget for 'Utilities' in September 2024 for user 1
    (9, 2024, 300.00, 4, 2),  -- Budget for 'Entertainment' in September 2024 for user 2
    (9, 2024, 200.00, 5, 2);  -- Budget for 'Transportation' in September 2024 for user 2



INSERT INTO income (amount, date, source, category_id, user_id)
VALUES
    (3000.00, '2024-09-01', 'September salary', 6, 1),  -- Salary for user 1
    (500.00, '2024-09-05', 'Freelance project', 7, 2),  -- Freelance for user 2
    (200.00, '2024-09-10', 'Stock dividends', 8, 1);    -- Investments for user 1


INSERT INTO income (amount, date, source, category_id, user_id)
VALUES
    (3000.00, '2024-09-01', 'September salary', 6, 1),  -- Salary for user 1
    (500.00, '2024-09-05', 'Freelance project', 7, 2),  -- Freelance for user 2
    (200.00, '2024-09-10', 'Stock dividends', 8, 1);    -- Investments for user 1
