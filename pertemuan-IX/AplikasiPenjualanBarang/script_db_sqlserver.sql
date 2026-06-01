IF DB_ID('db_penjualan') IS NULL
    CREATE DATABASE db_penjualan;
GO

USE db_penjualan;
GO

IF OBJECT_ID('detail_transaksi', 'U') IS NOT NULL DROP TABLE detail_transaksi;
IF OBJECT_ID('inventory', 'U') IS NOT NULL DROP TABLE inventory;
IF OBJECT_ID('transaksi', 'U') IS NOT NULL DROP TABLE transaksi;
IF OBJECT_ID('barang', 'U') IS NOT NULL DROP TABLE barang;
IF OBJECT_ID('customer', 'U') IS NOT NULL DROP TABLE customer;
IF OBJECT_ID('supplier', 'U') IS NOT NULL DROP TABLE supplier;
GO

CREATE TABLE barang (
    kd_barang VARCHAR(10) NOT NULL PRIMARY KEY,
    nm_barang VARCHAR(50),
    harga     FLOAT,
    stok      INT
);

CREATE TABLE customer (
    kd_customer VARCHAR(10) NOT NULL PRIMARY KEY,
    nm_customer VARCHAR(50),
    alamat      VARCHAR(100),
    telp        VARCHAR(15)
);

CREATE TABLE supplier (
    kd_supplier VARCHAR(10) NOT NULL PRIMARY KEY,
    nm_supplier VARCHAR(50),
    alamat      VARCHAR(100),
    telp        VARCHAR(15)
);

CREATE TABLE transaksi (
    no_transaksi  VARCHAR(15) NOT NULL PRIMARY KEY,
    kd_customer   VARCHAR(10),
    tgl_transaksi DATE,
    total         FLOAT,
    FOREIGN KEY (kd_customer) REFERENCES customer(kd_customer)
);

CREATE TABLE detail_transaksi (
    id_detail    INT IDENTITY(1,1) PRIMARY KEY,
    no_transaksi VARCHAR(15),
    kd_barang    VARCHAR(10),
    qty          INT,
    subtotal     FLOAT,
    FOREIGN KEY (no_transaksi) REFERENCES transaksi(no_transaksi),
    FOREIGN KEY (kd_barang)    REFERENCES barang(kd_barang)
);

CREATE TABLE inventory (
    id_inventory INT IDENTITY(1,1) PRIMARY KEY,
    kd_barang    VARCHAR(10),
    kd_supplier  VARCHAR(10),
    tgl_masuk    DATE,
    qty_masuk    INT,
    FOREIGN KEY (kd_barang)   REFERENCES barang(kd_barang),
    FOREIGN KEY (kd_supplier) REFERENCES supplier(kd_supplier)
);
GO

INSERT INTO barang VALUES ('B001', 'Pensil 2B', 3000, 100);
INSERT INTO barang VALUES ('B002', 'Buku Tulis', 5000, 200);
INSERT INTO customer VALUES ('C001', 'Andi', 'Jakarta', '08123456789');
INSERT INTO supplier VALUES ('S001', 'PT Alat Tulis', 'Bandung', '0227654321');
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
