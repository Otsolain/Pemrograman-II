package com.unpam;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;

public class MainAdministrasi extends JFrame {

    private JDesktopPane desktopPane;

    public MainAdministrasi() {
        setTitle("Aplikasi Administrasi Nilai Mahasiswa");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        desktopPane = new JDesktopPane();
        setContentPane(desktopPane);

        JLabel judul = new JLabel("APLIKASI ADMINISTRASI NILAI MAHASISWA", SwingConstants.CENTER);
        judul.setFont(new Font("Arial", Font.BOLD, 20));
        judul.setBounds(0, 220, 900, 30);
        desktopPane.add(judul, Integer.valueOf(0));

        setJMenuBar(buatMenu());
    }

    private JMenuBar buatMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuData = new JMenu("Data");
        JMenuItem itemInput = new JMenuItem("Input & Kelola Data Mahasiswa");
        JMenuItem itemCari  = new JMenuItem("Cari Data Mahasiswa");
        menuData.add(itemInput);
        menuData.add(itemCari);

        JMenu menuLaporan = new JMenu("Laporan");
        JMenuItem itemCetak = new JMenuItem("Cetak Laporan Mahasiswa");
        menuLaporan.add(itemCetak);

        JMenu menuKeluar = new JMenu("Keluar");
        JMenuItem itemExit = new JMenuItem("Exit");
        menuKeluar.add(itemExit);

        menuBar.add(menuData);
        menuBar.add(menuLaporan);
        menuBar.add(menuKeluar);

        itemInput.addActionListener(e -> tampilkanChild(new DataMahasiswaForm()));
        itemCari.addActionListener(e -> tampilkanChild(new CariDataMahasiswaForm()));
        itemCetak.addActionListener(e -> tampilkanChild(new CetakLaporanForm()));
        itemExit.addActionListener(e -> {
            int jawab = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?",
                    "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (jawab == JOptionPane.YES_OPTION) System.exit(0);
        });

        return menuBar;
    }

    private void tampilkanChild(JInternalFrame child) {
        desktopPane.add(child);
        child.setVisible(true);
        try { child.setSelected(true); } catch (Exception ex) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainAdministrasi().setVisible(true));
    }
}
