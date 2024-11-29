-- Drop existing tables if they exist
DROP TABLE IF EXISTS expense, category, budget, income, "user" CASCADE;

-- Define the update_timestamp function
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- User table with a reference to Cognito's UUID and local username
CREATE TABLE "user" (
    "user_id" SERIAL PRIMARY KEY,
    "cognito_uuid" VARCHAR(36) UNIQUE NOT NULL,
    "username" VARCHAR(50) NOT NULL UNIQUE,
    "email" VARCHAR(100) NOT NULL UNIQUE,
    "role" VARCHAR(20),
    "status" VARCHAR(20),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create the trigger for the user table
CREATE TRIGGER user_updated_at_trigger
BEFORE UPDATE ON "user"
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Category table
CREATE TABLE category (
    category_id SERIAL PRIMARY KEY,
    "user_id" INT NOT NULL,
    "name" VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create the trigger for the category table
CREATE TRIGGER category_updated_at_trigger
BEFORE UPDATE ON category
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Expense table
CREATE TABLE expense (
    expense_id SERIAL PRIMARY KEY,
    "user_id" INT NOT NULL,
    description VARCHAR(100) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    recurring BOOLEAN DEFAULT FALSE,
    date DATE NOT NULL,
    category_id INT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create the trigger for the expense table
CREATE TRIGGER expense_updated_at_trigger
BEFORE UPDATE ON expense
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Budget table
CREATE TABLE budget (
    budget_id SERIAL PRIMARY KEY,
    "user_id" INT NOT NULL,
    "month" INT NOT NULL,
    "year" INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    category_id INTEGER,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create the trigger for the budget table
CREATE TRIGGER budget_updated_at_trigger
BEFORE UPDATE ON budget
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Income table
CREATE TABLE income (
    income_id SERIAL PRIMARY KEY,
    "user_id" INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    "source" VARCHAR(50) NOT NULL,
    description VARCHAR(50),
    category_id INT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create the trigger for the income table
CREATE TRIGGER income_updated_at_trigger
BEFORE UPDATE ON income
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

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
