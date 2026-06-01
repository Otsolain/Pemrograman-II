import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;

public class CariDataMahasiswaForm extends javax.swing.JFrame {

    DatabaseKoneksi db = new DatabaseKoneksi();
    Connection koneksi;

    JTextField nimTF, namaTF;
    JButton btnCariNim, btnCariNama, btnTampilSemua, btnBersih;
    JTable tabel;
    DefaultTableModel modelTabel;

    public CariDataMahasiswaForm() {
        koneksi = db.getKoneksi();
        initComponents();
        tampilSemua();
    }

    private void initComponents() {
        setTitle("Cari Data Mahasiswa");
        setSize(640, 440);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        nimTF  = new JTextField();
        namaTF = new JTextField();

        JPanel panelInput = new JPanel(new GridLayout(2, 2, 5, 5));
        panelInput.add(new JLabel("Cari berdasarkan NIM:"));  panelInput.add(nimTF);
        panelInput.add(new JLabel("Cari berdasarkan Nama:")); panelInput.add(namaTF);

        btnCariNim     = new JButton("Cari NIM");
        btnCariNama    = new JButton("Cari Nama");
        btnTampilSemua = new JButton("Tampil Semua");
        btnBersih      = new JButton("Bersih");

        JPanel panelTombol = new JPanel(new FlowLayout());
        panelTombol.add(btnCariNim);
        panelTombol.add(btnCariNama);
        panelTombol.add(btnTampilSemua);
        panelTombol.add(btnBersih);

        modelTabel = new DefaultTableModel();
        modelTabel.addColumn("NIM");
        modelTabel.addColumn("Nama");
        modelTabel.addColumn("Semester");
        modelTabel.addColumn("Kelas");
        tabel = new JTable(modelTabel);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelInput, BorderLayout.CENTER);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(panelAtas, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        btnCariNim.addActionListener(e -> cariByNim());
        btnCariNama.addActionListener(e -> cariByNama());
        btnTampilSemua.addActionListener(e -> tampilSemua());
        btnBersih.addActionListener(e -> {
            nimTF.setText("");
            namaTF.setText("");
            tampilSemua();
        });
    }

    private void isiTabel(ResultSet rs) throws SQLException {
        modelTabel.setRowCount(0);
        while (rs.next()) {
            modelTabel.addRow(new Object[]{
                rs.getString("nim"),
                rs.getString("nama"),
                rs.getString("semester"),
                rs.getString("kelas")
            });
        }
    }

    private void cariByNim() {
        try {
            PreparedStatement ps = koneksi.prepareStatement(
                "SELECT * FROM datamhs WHERE nim = ?"
            );
            ps.setString(1, nimTF.getText());
            ResultSet rs = ps.executeQuery();
            isiTabel(rs);
            if (modelTabel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Koneksi gagal " + e.toString());
        }
    }

    private void cariByNama() {
        try {
            PreparedStatement ps = koneksi.prepareStatement(
                "SELECT * FROM datamhs WHERE nama LIKE ? ORDER BY nama ASC"
            );
            ps.setString(1, "%" + namaTF.getText() + "%");
            ResultSet rs = ps.executeQuery();
            isiTabel(rs);
            if (modelTabel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Koneksi gagal " + e.toString());
        }
    }

    private void tampilSemua() {
        try {
            Statement st = koneksi.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM datamhs ORDER BY nama ASC");
            isiTabel(rs);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil data: " + e.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CariDataMahasiswaForm().setVisible(true));
    }
}
