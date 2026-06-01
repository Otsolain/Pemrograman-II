package com.unpam.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {

    private static final String driver   = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String database =
        "jdbc:sqlserver://localhost:1433;"
      + "databaseName=dbaplikasipenilaianmahasiswa;"
      + "user=app_user;"
      + "password=AppUser123!;"
      + "encrypt=true;"
      + "trustServerCertificate=true";

    private Connection connection;
    private String pesanKesalahan;

    public String getPesanKesalahan() {
        return pesanKesalahan;
    }

    public Connection getConnection() {
        connection      = null;
        pesanKesalahan  = "";

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            pesanKesalahan = "JDBC Driver tidak ditemukan atau rusak\n" + ex;
        }

        if (pesanKesalahan.equals("")) {
            try {
                connection = DriverManager.getConnection(database);
            } catch (SQLException ex) {
                pesanKesalahan = "Koneksi ke " + database + " gagal\n" + ex;
            }
        }
        return connection;
    }
}
