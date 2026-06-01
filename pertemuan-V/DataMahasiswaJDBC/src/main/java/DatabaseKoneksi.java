import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseKoneksi {

    public Connection getKoneksi() {
        Connection koneksi = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String url = "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=MHS;"
                    + "user=app_user;"
                    + "password=AppUser123!;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;";

            koneksi = DriverManager.getConnection(url);
            System.out.println("Koneksi berhasil!");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Koneksi gagal: " + e.toString());
        }
        return koneksi;
    }
}
