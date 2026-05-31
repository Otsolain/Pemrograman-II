IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'dbrentcar')
    CREATE DATABASE dbrentcar;
GO

USE dbrentcar;
GO

IF OBJECT_ID('tbmobil', 'U') IS NULL
    CREATE TABLE tbmobil (
        kode_mobil  VARCHAR(10)  NOT NULL PRIMARY KEY,
        nama_mobil  VARCHAR(50),
        merk        VARCHAR(30),
        tahun       INT,
        kapasitas   INT,
        harga_sewa  FLOAT,
        status      VARCHAR(15) DEFAULT 'Tersedia'
    );
GO

IF OBJECT_ID('tbcustomer', 'U') IS NULL
    CREATE TABLE tbcustomer (
        kode_customer VARCHAR(10) NOT NULL PRIMARY KEY,
        nama          VARCHAR(50),
        alamat        VARCHAR(100),
        no_telepon    VARCHAR(15),
        no_ktp        VARCHAR(20)
    );
GO

IF OBJECT_ID('tbsewa', 'U') IS NULL
    CREATE TABLE tbsewa (
        kode_sewa           VARCHAR(15) NOT NULL PRIMARY KEY,
        kode_customer       VARCHAR(10),
        kode_mobil          VARCHAR(10),
        tgl_sewa            DATE,
        tgl_kembali_rencana DATE,
        tgl_kembali_aktual  DATE,
        lama_sewa           INT,
        total_biaya         FLOAT,
        status              VARCHAR(15) DEFAULT 'Aktif',
        CONSTRAINT fk_sewa_customer FOREIGN KEY (kode_customer) REFERENCES tbcustomer(kode_customer),
        CONSTRAINT fk_sewa_mobil    FOREIGN KEY (kode_mobil)    REFERENCES tbmobil(kode_mobil)
    );
GO

IF OBJECT_ID('tbuser', 'U') IS NULL
    CREATE TABLE tbuser (
        username VARCHAR(30) NOT NULL PRIMARY KEY,
        password VARCHAR(32),
        nama     VARCHAR(50)
    );
GO

-- User admin default (username: admin, password: admin)
IF NOT EXISTS (SELECT 1 FROM tbuser WHERE username = 'admin')
    INSERT INTO tbuser (username, password, nama)
    VALUES ('admin', LOWER(CONVERT(VARCHAR(32), HASHBYTES('MD5', 'admin'), 2)), 'Administrator');
GO

-- Login SQL Server Authentication untuk koneksi aplikasi (user: app_user)
IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = 'app_user')
    CREATE LOGIN app_user WITH PASSWORD = 'AppUser123!', CHECK_POLICY = OFF;
GO

IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = 'app_user')
    CREATE USER app_user FOR LOGIN app_user;
GO

ALTER ROLE db_datareader ADD MEMBER app_user;
ALTER ROLE db_datawriter ADD MEMBER app_user;
GO
