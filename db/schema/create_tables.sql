DROP TABLE IF EXISTS expense, category, budget, income, "user" CASCADE;

CREATE TABLE "user" (
    "user_id" SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255),  -- Increased length for hashes
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE category (
    category_id SERIAL PRIMARY KEY,
    "user_id" INT NOT NULL,
    "name" VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Changed to TIMESTAMP
    CONSTRAINT fk_user FOREIGN KEY ("user_id") REFERENCES "user"("user_id")
);

CREATE TABLE expense (
    expense_id SERIAL PRIMARY KEY,
    "user_id" INT NOT NULL,
    description VARCHAR(100) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    recurring BOOLEAN DEFAULT FALSE,
    date DATE NOT NULL,
    category_id INT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(category_id),
    CONSTRAINT fk_user FOREIGN KEY ("user_id") REFERENCES "user"("user_id")
);

CREATE TABLE budget (
    budget_id SERIAL PRIMARY KEY,
    "user_id" INT NOT NULL,
    "month" INT NOT NULL,
    "year" INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    category_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(category_id),
    CONSTRAINT fk_user FOREIGN KEY ("user_id") REFERENCES "user"("user_id")
);

CREATE TABLE income (
    income_id SERIAL PRIMARY KEY,
    "user_id" INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    "source" VARCHAR(50) NOT NULL,
    description VARCHAR(50),
    category_id INT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(category_id),
    CONSTRAINT fk_user FOREIGN KEY ("user_id") REFERENCES "user"("user_id")
);