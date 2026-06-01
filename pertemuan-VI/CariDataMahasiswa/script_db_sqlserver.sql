IF DB_ID('MHS') IS NULL
    CREATE DATABASE MHS;
GO

USE MHS;
GO

IF OBJECT_ID('datamhs', 'U') IS NULL
    CREATE TABLE datamhs (
        nim      VARCHAR(15) NOT NULL PRIMARY KEY,
        nama     VARCHAR(50),
        semester INT,
        kelas    VARCHAR(20)
    );
GO

ALTER TABLE datamhs ALTER COLUMN nama VARCHAR(50);
GO
ALTER TABLE datamhs ALTER COLUMN kelas VARCHAR(20);
GO

IF NOT EXISTS (SELECT 1 FROM datamhs WHERE nim = '2023001')
    INSERT INTO datamhs (nim, nama, semester, kelas) VALUES ('2023001', 'Budi Santoso', 3, 'TI-A');
IF NOT EXISTS (SELECT 1 FROM datamhs WHERE nim = '2023002')
    INSERT INTO datamhs (nim, nama, semester, kelas) VALUES ('2023002', 'Siti Aminah', 3, 'TI-B');
IF NOT EXISTS (SELECT 1 FROM datamhs WHERE nim = '2023003')
    INSERT INTO datamhs (nim, nama, semester, kelas) VALUES ('2023003', 'Budiman', 5, 'TI-A');
GO

IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = 'app_user')
    CREATE LOGIN app_user WITH PASSWORD = 'AppUser123!', CHECK_POLICY = OFF;
GO

IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = 'app_user')
    CREATE USER app_user FOR LOGIN app_user;
GO

ALTER ROLE db_datareader ADD MEMBER app_user;
ALTER ROLE db_datawriter ADD MEMBER app_user;
GO
