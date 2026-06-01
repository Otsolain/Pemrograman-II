IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'dbaplikasipenilaianmahasiswa')
    CREATE DATABASE dbaplikasipenilaianmahasiswa;
GO

USE dbaplikasipenilaianmahasiswa;
GO

IF OBJECT_ID('tbmahasiswa', 'U') IS NULL
    CREATE TABLE tbmahasiswa (
        nim      VARCHAR(15) NOT NULL PRIMARY KEY,
        nama     VARCHAR(50),
        semester INT,
        kelas    VARCHAR(20),
        password VARCHAR(32)
    );
GO

IF OBJECT_ID('tbmatakuliah', 'U') IS NULL
    CREATE TABLE tbmatakuliah (
        kode_mk  VARCHAR(10) NOT NULL PRIMARY KEY,
        nama_mk  VARCHAR(50),
        sks      INT
    );
GO

IF OBJECT_ID('tbuser', 'U') IS NULL
    CREATE TABLE tbuser (
        username VARCHAR(30) NOT NULL PRIMARY KEY,
        password VARCHAR(32),
        nama     VARCHAR(50)
    );
GO

IF NOT EXISTS (SELECT 1 FROM tbuser WHERE username = 'admin')
    INSERT INTO tbuser (username, password, nama)
    VALUES ('admin', LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'admin'), 2)), 'Administrator');
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
