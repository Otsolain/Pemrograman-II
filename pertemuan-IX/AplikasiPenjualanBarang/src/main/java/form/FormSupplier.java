package form;

import koneksi.Koneksi;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormSupplier extends JFrame {

    JTextField txtKode = new JTextField();
    JTextField txtNama = new JTextField();
    JTextField txtAlamat = new JTextField();
    JTextField txtTelp = new JTextField();

    JButton btnSimpan = new JButton("Simpan");
    JButton btnUbah = new JButton("Ubah");
    JButton btnHapus = new JButton("Hapus");
    JButton btnBersih = new JButton("Bersih");

    JTable table = new JTable();
    DefaultTableModel model;

    Connection conn;

    public FormSupplier() {
        setTitle("Form Supplier");
        setSize(750, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        conn = Koneksi.getConnection();

        JPanel panelInput = new JPanel(new GridLayout(6, 2, 10, 10));
        panelInput.add(new JLabel("Kode Supplier"));
        panelInput.add(txtKode);
        panelInput.add(new JLabel("Nama Supplier"));
        panelInput.add(txtNama);
        panelInput.add(new JLabel("Alamat"));
        panelInput.add(txtAlamat);
        panelInput.add(new JLabel("Telepon"));
        panelInput.add(txtTelp);
        panelInput.add(btnSimpan);
        panelInput.add(btnUbah);
        panelInput.add(btnHapus);
        panelInput.add(btnBersih);

        model = new DefaultTableModel();
        model.addColumn("Kode Supplier");
        model.addColumn("Nama Supplier");
        model.addColumn("Alamat");
        model.addColumn("Telepon");

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
                txtAlamat.setText(model.getValueAt(row, 2).toString());
                txtTelp.setText(model.getValueAt(row, 3).toString());
                txtKode.setEnabled(false);
            }
        });
    }

    private void tampilData() {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM supplier";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("kd_supplier"),
                    rs.getString("nm_supplier"),
                    rs.getString("alamat"),
                    rs.getString("telp")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil data: " + e.getMessage());
        }
    }

    private void simpanData() {
        try {
            String sql = "INSERT INTO supplier (kd_supplier, nm_supplier, alamat, telp) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtKode.getText());
            ps.setString(2, txtNama.getText());
            ps.setString(3, txtAlamat.getText());
            ps.setString(4, txtTelp.getText());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data supplier berhasil disimpan");
            bersihForm();
            tampilData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal simpan: " + e.getMessage());
        }
    }

    private void ubahData() {
        try {
            String sql = "UPDATE supplier SET nm_supplier=?, alamat=?, telp=? WHERE kd_supplier=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNama.getText());
            ps.setString(2, txtAlamat.getText());
            ps.setString(3, txtTelp.getText());
            ps.setString(4, txtKode.getText());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data supplier berhasil diubah");
            bersihForm();
            tampilData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal ubah: " + e.getMessage());
        }
    }

    private void hapusData() {
        try {
            String sql = "DELETE FROM supplier WHERE kd_supplier=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtKode.getText());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data supplier berhasil dihapus");
            bersihForm();
            tampilData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal hapus: " + e.getMessage());
        }
    }

    private void bersihForm() {
        txtKode.setText("");
        txtNama.setText("");
        txtAlamat.setText("");
        txtTelp.setText("");
        txtKode.setEnabled(true);
        table.clearSelection();
    }
}
