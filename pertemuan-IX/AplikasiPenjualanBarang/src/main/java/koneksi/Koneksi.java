package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    private static Connection conn;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                String url = "jdbc:sqlserver://localhost:1433;"
                        + "databaseName=db_penjualan;"
                        + "user=app_user;"
                        + "password=AppUser123!;"
                        + "encrypt=true;"
                        + "trustServerCertificate=true;";

                conn = DriverManager.getConnection(url);
                System.out.println("Koneksi berhasil");
            }
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
