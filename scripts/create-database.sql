-- =====================================================================
-- Run this once against your local SQL Server instance
-- BEFORE starting the Spring Boot application.
-- =====================================================================
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'TESTDB')
BEGIN
    CREATE DATABASE TESTDB;
    PRINT 'TESTDB database created.';
END
ELSE
BEGIN
    PRINT 'TESTDB database already exists.';
END
