-- PostgreSQL Database Setup Guide for workload_db (Multi-DBMS Architecture: Part 2) --

-- ==========================================
-- CRITICAL EXECUTION INSTRUCTIONS FOR THE TEAM:
-- Unlike MySQL, PostgreSQL does not support the `USE` command. 
-- Do NOT paste this entire script into the default 'postgres' database connection.
-- 
-- 1. First, run ONLY this command in your master connection: CREATE DATABASE workload_audit_db;
-- 2. Create a NEW connection node in NetBeans specifically wired to 'workload_audit_db'.
-- 3. Open the SQL editor for that NEW connection, paste the rest of this script, and run it.
-- ==========================================

-- 1. Clean Slate: Drop existing tables to prevent conflicts during testing
DROP TABLE IF EXISTS POSTGRES_TIME_LOGS;
DROP TABLE IF EXISTS POSTGRES_AUDIT_LOGS;
DROP TABLE IF EXISTS POSTGRES_SESSION_LOGS;

-- ==========================================
-- 2. Create Audit & Ledger Tables (Based on FAP_ERD_2.jpg)
-- ==========================================

-- Table: POSTGRES_TIME_LOGS
CREATE TABLE POSTGRES_TIME_LOGS (
    TIME_LOG_ID SERIAL PRIMARY KEY,
    -- USER_ID references Apache Derby (DBMS 2)
    USER_ID INT NOT NULL,
    -- TASK_ID references MySQL (DBMS 1)
    TASK_ID INT NOT NULL,
    DURATION_MINS INT NOT NULL,
    LOGGED_DATE DATE NOT NULL,
    WORK_NOTES TEXT
);

-- Table: POSTGRES_AUDIT_LOGS
CREATE TABLE POSTGRES_AUDIT_LOGS (
    AUDIT_ID SERIAL PRIMARY KEY,
    -- ADMIN_ID references Apache Derby (DBMS 2)
    ADMIN_ID INT NOT NULL,
    ACTION_TYPE VARCHAR(255) NOT NULL,
    ACTION_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: POSTGRES_SESSION_LOGS
CREATE TABLE POSTGRES_SESSION_LOGS (
    LOG_ID SERIAL PRIMARY KEY,
    -- USER_ID references Apache Derby (DBMS 2)
    USER_ID INT NOT NULL,
    LOGIN_TIME TIMESTAMP NOT NULL,
    LOGOUT_TIME TIMESTAMP
);

-- ==========================================
-- 3. Insert Initial Test Data
-- ==========================================

-- Insert Session Logs (Simulating logins for Derby Users 2 and 3)
INSERT INTO POSTGRES_SESSION_LOGS (USER_ID, LOGIN_TIME, LOGOUT_TIME) VALUES 
(2, '2026-05-23 08:00:00', '2026-05-23 10:30:00'),
(3, '2026-05-23 09:15:00', NULL); -- NULL simulates an active, ongoing session

-- Insert Audit Logs (Simulating Admin User 1 creating tasks in MySQL)
INSERT INTO POSTGRES_AUDIT_LOGS (ADMIN_ID, ACTION_TYPE, ACTION_TIMESTAMP) VALUES 
(1, 'CREATED_TRAINING_MODULE_1', '2026-05-22 14:00:00'),
(1, 'ASSIGNED_TASK_1_TO_USER_2', '2026-05-22 14:05:00');

-- Insert Time Logs (Simulating User 2 working on their assigned MySQL task)
INSERT INTO POSTGRES_TIME_LOGS (USER_ID, TASK_ID, DURATION_MINS, LOGGED_DATE, WORK_NOTES) VALUES 
(2, 1, 120, '2026-05-23', 'Successfully configured the local MySQL database and NetBeans driver connection.');