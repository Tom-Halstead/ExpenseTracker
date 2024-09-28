CREATE ROLE basic_user WITH LOGIN PASSWORD 'user_password';
CREATE ROLE creator WITH LOGIN PASSWORD 'creator_password';
CREATE ROLE admin WITH LOGIN PASSWORD 'admin_password' SUPERUSER;

-- Grant Privileges to Roles
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE expense TO basic_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE income TO basic_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE category TO creator;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO admin;

-- Assign Roles to Users
CREATE ROLE john WITH LOGIN PASSWORD 'john_password';
GRANT basic_user TO alex;

CREATE ROLE mary WITH LOGIN PASSWORD 'mary_password';
GRANT creator TO lisa;

CREATE ROLE alice WITH LOGIN PASSWORD 'alice_password';
GRANT admin TO jane;