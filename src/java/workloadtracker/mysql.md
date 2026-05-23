-- MySQL Database Setup Guide for workload_db (Multi-DBMS Architecture: Part 1) --

USE workload_db;

-- 1. Clean Slate: Drop existing placeholder tables to prevent conflicts
DROP TABLE IF EXISTS MYSQL_SUBMISSIONS;
DROP TABLE IF EXISTS MYSQL_STUDENT_TASKS;
DROP TABLE IF EXISTS MYSQL_TASKS;
DROP TABLE IF EXISTS MYSQL_TRAINING_MODULES;
DROP TABLE IF EXISTS workloads; -- Dropping the old placeholder
DROP TABLE IF EXISTS students;  -- Dropping the old placeholder

-- ==========================================
-- 2. Create Core Tables 
-- ==========================================

-- Table: MYSQL_TRAINING_MODULES
CREATE TABLE MYSQL_TRAINING_MODULES (
    MODULE_ID INT AUTO_INCREMENT PRIMARY KEY,
    -- Note: ADMIN_ID is a loose foreign key to DBMS 2 (Apache Derby: DERBY_USERS).
    -- Referential integrity must be enforced by your Java DAOs, not MySQL.
    ADMIN_ID INT NOT NULL, 
    TITLE VARCHAR(255) NOT NULL
);

-- Table: MYSQL_TASKS
CREATE TABLE MYSQL_TASKS (
    TASK_ID INT AUTO_INCREMENT PRIMARY KEY,
    MODULE_ID INT NOT NULL,
    TITLE VARCHAR(255) NOT NULL,
    FOREIGN KEY (MODULE_ID) REFERENCES MYSQL_TRAINING_MODULES(MODULE_ID) ON DELETE CASCADE
);

-- Table: MYSQL_STUDENT_TASKS
CREATE TABLE MYSQL_STUDENT_TASKS (
    ASSIGNMENT_ID INT AUTO_INCREMENT PRIMARY KEY,
    TASK_ID INT NOT NULL,
    -- Note: USER_ID is a loose foreign key to DBMS 2 (Apache Derby: DERBY_USERS).
    -- Referential integrity must be enforced by your Java DAOs.
    USER_ID INT NOT NULL,
    STATUS VARCHAR(50) DEFAULT 'Pending',
    FOREIGN KEY (TASK_ID) REFERENCES MYSQL_TASKS(TASK_ID) ON DELETE CASCADE
);

-- Table: MYSQL_SUBMISSIONS
CREATE TABLE MYSQL_SUBMISSIONS (
    SUBMISSION_ID INT AUTO_INCREMENT PRIMARY KEY,
    ASSIGNMENT_ID INT NOT NULL,
    CONTENT_URL VARCHAR(500),
    SUBMITTED_AT DATETIME,
    FOREIGN KEY (ASSIGNMENT_ID) REFERENCES MYSQL_STUDENT_TASKS(ASSIGNMENT_ID) ON DELETE CASCADE
);

-- ==========================================
-- 3. Insert Initial Test Data
-- ==========================================

-- Insert Training Modules
INSERT INTO MYSQL_TRAINING_MODULES (ADMIN_ID, TITLE) VALUES 
(1, 'Database Architecture Fundamentals'),
(1, 'Java EE Cross-DBMS Integration');

-- Insert Tasks
INSERT INTO MYSQL_TASKS (MODULE_ID, TITLE) VALUES 
(1, 'Configure MySQL Connection Pool'),
(1, 'Deploy Apache Derby IdP'),
(2, 'Implement Multi-Database DAO Pattern');

-- Insert Student Tasks (Assigning User_IDs 2 through 5)
INSERT INTO MYSQL_STUDENT_TASKS (TASK_ID, USER_ID, STATUS) VALUES 
(1, 2, 'Completed'),
(2, 3, 'In Progress'),
(3, 4, 'Pending'),
(3, 5, 'Pending');