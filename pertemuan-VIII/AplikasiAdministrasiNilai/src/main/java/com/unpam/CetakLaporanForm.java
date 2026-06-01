package com.unpam;

import java.awt.FlowLayout;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class CetakLaporanForm extends JInternalFrame {

    private final DatabaseKoneksi db = new DatabaseKoneksi();

    public CetakLaporanForm() {
        super("Cetak Laporan Mahasiswa", true, true, true, true);
        setSize(300, 150);
        setLocation(100, 100);
        setLayout(new FlowLayout());

        JButton btnCetak = new JButton("Cetak Laporan");
        btnCetak.addActionListener(e -> cetakLaporan());
        add(btnCetak);
    }

    private JasperReport kompilasiLaporan() throws Exception {
        File external = new File("reports/LaporanMahasiswa.jrxml");
        if (external.exists()) {
            return JasperCompileManager.compileReport(external.getAbsolutePath());
        }
        try (InputStream is = getClass().getResourceAsStream("/LaporanMahasiswa.jrxml")) {
            if (is == null) {
                throw new Exception("File LaporanMahasiswa.jrxml tidak ditemukan");
            }
            return JasperCompileManager.compileReport(is);
        }
    }

    private void cetakLaporan() {
        Connection conn = db.getKoneksi();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Koneksi ke database gagal", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            JasperReport report = kompilasiLaporan();
            Map<String, Object> params = new HashMap<>();
            params.put("judul", "Laporan Data Mahasiswa");
            JasperPrint print = JasperFillManager.fillReport(report, params, conn);
            JasperViewer.viewReport(print, false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal cetak laporan: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try { conn.close(); } catch (Exception e) {}
        }
    }
}
