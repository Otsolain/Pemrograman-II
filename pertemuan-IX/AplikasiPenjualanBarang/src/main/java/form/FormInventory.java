package form;

import koneksi.Koneksi;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormInventory extends JFrame {

    JTextField txtKodeBarang = new JTextField();
    JTextField txtKodeSupplier = new JTextField();
    JTextField txtTanggal = new JTextField();
    JTextField txtQtyMasuk = new JTextField();

    JButton btnSimpan = new JButton("Simpan");
    JButton btnBersih = new JButton("Bersih");

    JTable table = new JTable();
    DefaultTableModel model;

    Connection conn;

    public FormInventory() {
        setTitle("Form Inventory Barang Masuk");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        conn = Koneksi.getConnection();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Kode Barang"));
        panel.add(txtKodeBarang);
        panel.add(new JLabel("Kode Supplier"));
        panel.add(txtKodeSupplier);
        panel.add(new JLabel("Tanggal Masuk (yyyy-mm-dd)"));
        panel.add(txtTanggal);
        panel.add(new JLabel("Qty Masuk"));
        panel.add(txtQtyMasuk);
        panel.add(btnSimpan);
        panel.add(btnBersih);

        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Kode Barang");
        model.addColumn("Kode Supplier");
        model.addColumn("Tanggal");
        model.addColumn("Qty Masuk");

        table.setModel(model);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        tampilData();
        aksi();
    }

    private void aksi() {
        btnSimpan.addActionListener(e -> simpanInventory());
        btnBersih.addActionListener(e -> bersihForm());
    }

    private void tampilData() {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM inventory";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_inventory"),
                    rs.getString("kd_barang"),
                    rs.getString("kd_supplier"),
                    rs.getString("tgl_masuk"),
                    rs.getInt("qty_masuk")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil inventory: " + e.getMessage());
        }
    }

    private void simpanInventory() {
        try {
            conn.setAutoCommit(false);

            String sqlInv = "INSERT INTO inventory (kd_barang, kd_supplier, tgl_masuk, qty_masuk) VALUES (?, ?, ?, ?)";
            PreparedStatement psInv = conn.prepareStatement(sqlInv);
            psInv.setString(1, txtKodeBarang.getText());
            psInv.setString(2, txtKodeSupplier.getText());
            psInv.setString(3, txtTanggal.getText());
            psInv.setInt(4, Integer.parseInt(txtQtyMasuk.getText()));
            psInv.executeUpdate();

            String sqlBarang = "UPDATE barang SET stok = stok + ? WHERE kd_barang = ?";
            PreparedStatement psBarang = conn.prepareStatement(sqlBarang);
            psBarang.setInt(1, Integer.parseInt(txtQtyMasuk.getText()));
            psBarang.setString(2, txtKodeBarang.getText());
            psBarang.executeUpdate();

            conn.commit();

            JOptionPane.showMessageDialog(this, "Inventory berhasil disimpan");
            bersihForm();
            tampilData();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
            }
            JOptionPane.showMessageDialog(this, "Gagal simpan inventory: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception ex) {
            }
        }
    }

    private void bersihForm() {
        txtKodeBarang.setText("");
        txtKodeSupplier.setText("");
        txtTanggal.setText("");
        txtQtyMasuk.setText("");
    }
}
