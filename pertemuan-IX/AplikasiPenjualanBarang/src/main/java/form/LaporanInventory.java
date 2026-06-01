package form;

import koneksi.Koneksi;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LaporanInventory extends JFrame {

    JTable table = new JTable();
    DefaultTableModel model;
    Connection conn;

    public LaporanInventory() {
        setTitle("Laporan Inventory");
        setSize(900, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        conn = Koneksi.getConnection();

        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Kode Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Supplier");
        model.addColumn("Tanggal Masuk");
        model.addColumn("Qty Masuk");

        table.setModel(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        tampilData();
    }

    private void tampilData() {
        model.setRowCount(0);
        try {
            String sql = "SELECT i.id_inventory, i.kd_barang, b.nm_barang, s.nm_supplier, i.tgl_masuk, i.qty_masuk " +
                         "FROM inventory i " +
                         "LEFT JOIN barang b ON i.kd_barang = b.kd_barang " +
                         "LEFT JOIN supplier s ON i.kd_supplier = s.kd_supplier " +
                         "ORDER BY i.tgl_masuk DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_inventory"),
                    rs.getString("kd_barang"),
                    rs.getString("nm_barang"),
                    rs.getString("nm_supplier"),
                    rs.getString("tgl_masuk"),
                    rs.getInt("qty_masuk")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil laporan inventory: " + e.getMessage());
        }
    }
}
