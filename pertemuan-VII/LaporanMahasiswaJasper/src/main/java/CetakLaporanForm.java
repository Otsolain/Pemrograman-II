import java.awt.FlowLayout;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class CetakLaporanForm extends JFrame {

    public CetakLaporanForm() {
        setTitle("Cetak Laporan");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JButton btnCetak = new JButton("Cetak Laporan");
        btnCetak.addActionListener(e -> cetakLaporan());
        setLayout(new FlowLayout());
        add(btnCetak);
    }

    private Connection getKoneksi() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://localhost:1433;"
                + "databaseName=MHS;"
                + "user=app_user;"
                + "password=AppUser123!;"
                + "encrypt=true;"
                + "trustServerCertificate=true;";
        return DriverManager.getConnection(url);
    }

    private void cetakLaporan() {
        try (Connection conn = getKoneksi();
             InputStream is = getClass().getResourceAsStream("/LaporanMahasiswa.jrxml")) {

            JasperReport report = JasperCompileManager.compileReport(is);

            Map<String, Object> params = new HashMap<>();
            params.put("judul", "Laporan Data Mahasiswa");

            JasperPrint print = JasperFillManager.fillReport(report, params, conn);
            JasperViewer.viewReport(print, false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal cetak laporan: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new CetakLaporanForm().setVisible(true));
    }
}
