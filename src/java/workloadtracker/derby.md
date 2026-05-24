-- Apache Derby Database Setup Guide (Multi-DBMS Architecture: Part 3) --

-- ==========================================
-- 1. Create Identity & Role Tables (Based on FAP_ERD_2.jpg)
-- ==========================================

CREATE TABLE DERBY_USERS (
    USER_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    USERNAME VARCHAR(50) NOT NULL UNIQUE,
    EMAIL VARCHAR(100) NOT NULL UNIQUE,
    PASSWORD_HASH VARCHAR(255) NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE DERBY_ROLES (
    ROLE_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ROLE_NAME VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE DERBY_USER_ROLES (
    USER_ID INT NOT NULL,
    ROLE_ID INT NOT NULL,
    PRIMARY KEY (USER_ID, ROLE_ID),
    FOREIGN KEY (USER_ID) REFERENCES DERBY_USERS(USER_ID),
    FOREIGN KEY (ROLE_ID) REFERENCES DERBY_ROLES(ROLE_ID)
);

-- ==========================================
-- 2. Insert Initial Test Identity Data
-- ==========================================

-- Insert Roles
INSERT INTO DERBY_ROLES (ROLE_NAME) VALUES 
('ADMIN'), 
('STUDENT');

-- Insert Users (Using mock password hashes for the legacy IdP)
INSERT INTO DERBY_USERS (USERNAME, EMAIL, PASSWORD_HASH) VALUES
('admin_peralta', 'francis.peralta@university.edu', 'hashed_pwd_admin_1'),
('carl_t', 'carl@university.edu', 'hashed_pwd_student_2'),
('daniel_t', 'daniel@university.edu', 'hashed_pwd_student_3'),
('gelo_t', 'gelo@university.edu', 'hashed_pwd_student_4'),
('rhett_t', 'rhett@university.edu', 'hashed_pwd_student_5');

-- Assign Roles to Users
INSERT INTO DERBY_USER_ROLES (USER_ID, ROLE_ID) VALUES
(1, 1), -- User 1 is an ADMIN
(2, 2), -- User 2 is a STUDENT
(3, 2), -- User 3 is a STUDENT
(4, 2), -- User 4 is a STUDENT
(5, 2); -- User 5 is a STUDENT
