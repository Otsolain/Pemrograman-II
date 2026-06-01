package form;

import koneksi.Koneksi;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LaporanTransaksi extends JFrame {

    JTable table = new JTable();
    DefaultTableModel model;
    Connection conn;

    public LaporanTransaksi() {
        setTitle("Laporan Transaksi");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        conn = Koneksi.getConnection();

        model = new DefaultTableModel();
        model.addColumn("No Transaksi");
        model.addColumn("Tanggal");
        model.addColumn("Customer");
        model.addColumn("Total");

        table.setModel(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        tampilData();
    }

    private void tampilData() {
        model.setRowCount(0);
        try {
            String sql = "SELECT t.no_transaksi, t.tgl_transaksi, c.nm_customer, t.total " +
                         "FROM transaksi t LEFT JOIN customer c ON t.kd_customer = c.kd_customer " +
                         "ORDER BY t.tgl_transaksi DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("no_transaksi"),
                    rs.getString("tgl_transaksi"),
                    rs.getString("nm_customer"),
                    rs.getDouble("total")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil laporan transaksi: " + e.getMessage());
        }
    }
}
