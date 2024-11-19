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
