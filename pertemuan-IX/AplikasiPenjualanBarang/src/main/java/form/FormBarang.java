package form;

import koneksi.Koneksi;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormBarang extends JFrame {

    JTextField txtKode = new JTextField();
    JTextField txtNama = new JTextField();
    JTextField txtHarga = new JTextField();
    JTextField txtStok = new JTextField();

    JButton btnSimpan = new JButton("Simpan");
    JButton btnUbah = new JButton("Ubah");
    JButton btnHapus = new JButton("Hapus");
    JButton btnBersih = new JButton("Bersih");

    JTable table = new JTable();
    DefaultTableModel model;

    Connection conn;

    public FormBarang() {
        setTitle("Form Barang");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        conn = Koneksi.getConnection();

        JPanel panelInput = new JPanel(new GridLayout(6, 2, 10, 10));
        panelInput.add(new JLabel("Kode Barang"));
        panelInput.add(txtKode);
        panelInput.add(new JLabel("Nama Barang"));
        panelInput.add(txtNama);
        panelInput.add(new JLabel("Harga"));
        panelInput.add(txtHarga);
        panelInput.add(new JLabel("Stok"));
        panelInput.add(txtStok);
        panelInput.add(btnSimpan);
        panelInput.add(btnUbah);
        panelInput.add(btnHapus);
        panelInput.add(btnBersih);

        model = new DefaultTableModel();
        model.addColumn("Kode");
        model.addColumn("Nama");
        model.addColumn("Harga");
        model.addColumn("Stok");

        table.setModel(model);
        JScrollPane scroll = new JScrollPane(table);

        add(panelInput, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        tampilData();
        aksi();
    }

    private void aksi() {
        btnSimpan.addActionListener(e -> simpanData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnBersih.addActionListener(e -> bersihForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtKode.setText(model.getValueAt(row, 0).toString());
                txtNama.setText(model.getValueAt(row, 1).toString());
                txtHarga.setText(model.getValueAt(row, 2).toString());
                txtStok.setText(model.getValueAt(row, 3).toString());
                txtKode.setEnabled(false);
            }
        });
    }

    private void tampilData() {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM barang";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("kd_barang"),
                    rs.getString("nm_barang"),
                    rs.getDouble("harga"),
                    rs.getInt("stok")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil data: " + e.getMessage());
        }
    }

    private void simpanData() {
        try {
            String sql = "INSERT INTO barang (kd_barang, nm_barang, harga, stok) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtKode.getText());
            ps.setString(2, txtNama.getText());
            ps.setDouble(3, Double.parseDouble(txtHarga.getText()));
            ps.setInt(4, Integer.parseInt(txtStok.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data barang berhasil disimpan");
            bersihForm();
            tampilData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal simpan: " + e.getMessage());
        }
    }

    private void ubahData() {
        try {
            String sql = "UPDATE barang SET nm_barang=?, harga=?, stok=? WHERE kd_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNama.getText());
            ps.setDouble(2, Double.parseDouble(txtHarga.getText()));
            ps.setInt(3, Integer.parseInt(txtStok.getText()));
            ps.setString(4, txtKode.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data barang berhasil diubah");
            bersihForm();
            tampilData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal ubah: " + e.getMessage());
        }
    }

    private void hapusData() {
        try {
            String sql = "DELETE FROM barang WHERE kd_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtKode.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data barang berhasil dihapus");
            bersihForm();
            tampilData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal hapus: " + e.getMessage());
        }
    }

    private void bersihForm() {
        txtKode.setText("");
        txtNama.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        txtKode.setEnabled(true);
        table.clearSelection();
    }
}
