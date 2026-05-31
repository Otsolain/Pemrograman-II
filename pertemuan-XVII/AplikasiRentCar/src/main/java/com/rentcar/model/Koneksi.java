package com.rentcar.model;

import java.sql.*;

public class Koneksi {

    private static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String database =
        "jdbc:sqlserver://localhost:1433;"
      + "databaseName=dbrentcar;"
      + "user=app_user;"
      + "password=AppUser123!;"
      + "encrypt=true;"
      + "trustServerCertificate=true";

    private String pesanKesalahan;

    public String getPesanKesalahan() { return pesanKesalahan; }

    public Connection getConnection() {
        Connection connection = null;
        pesanKesalahan = "";
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(database);
        } catch (ClassNotFoundException ex) {
            pesanKesalahan = "Driver tidak ditemukan: " + ex;
        } catch (SQLException ex) {
            pesanKesalahan = "Koneksi gagal: " + ex;
        }
        return connection;
    }
}
