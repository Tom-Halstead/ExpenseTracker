DROP TABLE IF EXISTS expense, category, budget, recurring_expense, income, "user" CASCADE;


CREATE TABLE "user" (
    user_id serial PRIMARY KEY,
    username varchar(50) NOT NULL UNIQUE,
    email varchar(20) NOT NULL,
    password_hash varchar(50),
    is_active varchar(255) NOT NULL,
    created_at TIMESTAMP DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE category (
    category_id serial PRIMARY KEY,
    user_id int NOT NULL,
    "name" varchar(50) NOT NULL UNIQUE,
    type varchar(20) NOT NULL,
    description varchar(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATE DEFAULT CURRENT_DATE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);


CREATE TABLE expense (
    expense_id serial PRIMARY KEY,
    user_id int NOT NULL,
    description varchar(100) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    date DATE NOT NULL,
    category_id int,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(category_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);

CREATE TABLE budget (
    budget_id serial PRIMARY KEY,
    user_id int NOT NULL,
    "month" int NOT NULL,
    "year" int NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    category_id INTEGER,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(category_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);
CREATE TABLE recurring_expense (
    recurring_expense_id serial PRIMARY KEY,
    user_id int NOT NULL,
    description varchar(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    category_id int,
    recurrence_interval varchar(20) NOT NULL,
    next_due_date DATE NOT NULL,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(category_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);
CREATE TABLE income (
    income_id serial PRIMARY KEY,
    user_id int NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    "source" varchar(15) NOT NULL,
    description varchar(50),
    category_id int,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(category_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);

