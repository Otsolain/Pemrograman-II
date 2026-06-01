package form;

import koneksi.Koneksi;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormTransaksi extends JFrame {

    JTextField txtNo = new JTextField();
    JTextField txtTgl = new JTextField();
    JTextField txtCustomer = new JTextField();
    JTextField txtBarang = new JTextField();
    JTextField txtQty = new JTextField();
    JTextField txtHarga = new JTextField();
    JTextField txtSubtotal = new JTextField();
    JTextField txtTotal = new JTextField();

    JButton btnHitung = new JButton("Hitung");
    JButton btnTambah = new JButton("Tambah Item");
    JButton btnSimpan = new JButton("Simpan Transaksi");
    JButton btnBersih = new JButton("Bersih");

    JTable table = new JTable();
    DefaultTableModel model;

    Connection conn;

    public FormTransaksi() {
        setTitle("Form Transaksi Penjualan");
        setSize(900, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        conn = Koneksi.getConnection();

        JPanel panel = new JPanel(new GridLayout(10, 2, 10, 10));
        panel.add(new JLabel("No Transaksi"));
        panel.add(txtNo);
        panel.add(new JLabel("Tanggal (yyyy-mm-dd)"));
        panel.add(txtTgl);
        panel.add(new JLabel("Kode Customer"));
        panel.add(txtCustomer);
        panel.add(new JLabel("Kode Barang"));
        panel.add(txtBarang);
        panel.add(new JLabel("Qty"));
        panel.add(txtQty);
        panel.add(new JLabel("Harga"));
        panel.add(txtHarga);
        panel.add(new JLabel("Subtotal"));
        panel.add(txtSubtotal);
        panel.add(new JLabel("Total"));
        panel.add(txtTotal);
        panel.add(btnHitung);
        panel.add(btnTambah);
        panel.add(btnSimpan);
        panel.add(btnBersih);

        model = new DefaultTableModel();
        model.addColumn("Kode Barang");
        model.addColumn("Qty");
        model.addColumn("Harga");
        model.addColumn("Subtotal");

        table.setModel(model);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        aksi();
    }

    private void aksi() {
        btnHitung.addActionListener(e -> hitungSubtotal());
        btnTambah.addActionListener(e -> tambahItem());
        btnSimpan.addActionListener(e -> simpanTransaksi());
        btnBersih.addActionListener(e -> bersihForm());
    }

    private void hitungSubtotal() {
        try {
            int qty = Integer.parseInt(txtQty.getText());
            double harga = Double.parseDouble(txtHarga.getText());
            double subtotal = qty * harga;
            txtSubtotal.setText(String.valueOf(subtotal));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal hitung subtotal: " + e.getMessage());
        }
    }

    private void tambahItem() {
        try {
            model.addRow(new Object[]{
                txtBarang.getText(),
                txtQty.getText(),
                txtHarga.getText(),
                txtSubtotal.getText()
            });

            double total = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                total += Double.parseDouble(model.getValueAt(i, 3).toString());
            }
            txtTotal.setText(String.valueOf(total));

            txtBarang.setText("");
            txtQty.setText("");
            txtHarga.setText("");
            txtSubtotal.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tambah item: " + e.getMessage());
        }
    }

    private void simpanTransaksi() {
        try {
            conn.setAutoCommit(false);

            String sqlTransaksi = "INSERT INTO transaksi (no_transaksi, kd_customer, tgl_transaksi, total) VALUES (?, ?, ?, ?)";
            PreparedStatement psTransaksi = conn.prepareStatement(sqlTransaksi);
            psTransaksi.setString(1, txtNo.getText());
            psTransaksi.setString(2, txtCustomer.getText());
            psTransaksi.setString(3, txtTgl.getText());
            psTransaksi.setDouble(4, Double.parseDouble(txtTotal.getText()));
            psTransaksi.executeUpdate();

            for (int i = 0; i < model.getRowCount(); i++) {
                String kdBarang = model.getValueAt(i, 0).toString();
                int qty = Integer.parseInt(model.getValueAt(i, 1).toString());
                double subtotal = Double.parseDouble(model.getValueAt(i, 3).toString());

                String sqlDetail = "INSERT INTO detail_transaksi (no_transaksi, kd_barang, qty, subtotal) VALUES (?, ?, ?, ?)";
                PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                psDetail.setString(1, txtNo.getText());
                psDetail.setString(2, kdBarang);
                psDetail.setInt(3, qty);
                psDetail.setDouble(4, subtotal);
                psDetail.executeUpdate();

                String sqlUpdateStok = "UPDATE barang SET stok = stok - ? WHERE kd_barang = ?";
                PreparedStatement psStok = conn.prepareStatement(sqlUpdateStok);
                psStok.setInt(1, qty);
                psStok.setString(2, kdBarang);
                psStok.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan");
            bersihForm();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
            }
            JOptionPane.showMessageDialog(this, "Gagal simpan transaksi: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception ex) {
            }
        }
    }

    private void bersihForm() {
        txtNo.setText("");
        txtTgl.setText("");
        txtCustomer.setText("");
        txtBarang.setText("");
        txtQty.setText("");
        txtHarga.setText("");
        txtSubtotal.setText("");
        txtTotal.setText("");
        model.setRowCount(0);
    }
}
