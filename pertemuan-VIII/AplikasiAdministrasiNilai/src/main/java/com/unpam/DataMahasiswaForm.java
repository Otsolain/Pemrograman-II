package com.unpam;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;

public class DataMahasiswaForm extends JInternalFrame {

    private final DatabaseKoneksi db = new DatabaseKoneksi();
    private Connection koneksi;

    private JTextField nimTF, namaTF, semesterTF, kelasTF;
    private JButton btnSimpan, btnUbah, btnHapus, btnBersih;
    private JTable tabel;
    private DefaultTableModel modelTabel;

    public DataMahasiswaForm() {
        super("Input & Kelola Data Mahasiswa", true, true, true, true);
        setSize(620, 420);
        setLocation(20, 20);

        koneksi = db.getKoneksi();
        initComponents();
        tampilkanData();
    }

    private void initComponents() {
        nimTF      = new JTextField();
        namaTF     = new JTextField();
        semesterTF = new JTextField();
        kelasTF    = new JTextField();

        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInput.add(new JLabel("NIM:"));      panelInput.add(nimTF);
        panelInput.add(new JLabel("Nama:"));     panelInput.add(namaTF);
        panelInput.add(new JLabel("Semester:")); panelInput.add(semesterTF);
        panelInput.add(new JLabel("Kelas:"));    panelInput.add(kelasTF);

        btnSimpan = new JButton("Simpan");
        btnUbah   = new JButton("Ubah");
        btnHapus  = new JButton("Hapus");
        btnBersih = new JButton("Bersih");

        JPanel panelTombol = new JPanel(new FlowLayout());
        panelTombol.add(btnSimpan);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
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

        btnSimpan.addActionListener(e -> simpanData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnBersih.addActionListener(e -> kosongkanField());

        tabel.getSelectionModel().addListSelectionListener(e -> {
            int row = tabel.getSelectedRow();
            if (row != -1) {
                nimTF.setText(modelTabel.getValueAt(row, 0).toString());
                namaTF.setText(modelTabel.getValueAt(row, 1).toString());
                semesterTF.setText(modelTabel.getValueAt(row, 2).toString());
                kelasTF.setText(modelTabel.getValueAt(row, 3).toString());
            }
        });
    }

    private void tampilkanData() {
        modelTabel.setRowCount(0);
        try {
            Statement st = koneksi.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM datamhs ORDER BY nama ASC");
            while (rs.next()) {
                modelTabel.addRow(new Object[]{
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("semester"),
                    rs.getString("kelas")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil data: " + e.toString());
        }
    }

    private void simpanData() {
        try {
            PreparedStatement ps = koneksi.prepareStatement(
                "INSERT INTO datamhs (nim, nama, semester, kelas) VALUES (?, ?, ?, ?)");
            ps.setString(1, nimTF.getText());
            ps.setString(2, namaTF.getText());
            ps.setString(3, semesterTF.getText());
            ps.setString(4, kelasTF.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            kosongkanField();
            tampilkanData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal simpan: " + e.toString());
        }
    }

    private void ubahData() {
        try {
            PreparedStatement ps = koneksi.prepareStatement(
                "UPDATE datamhs SET nama=?, semester=?, kelas=? WHERE nim=?");
            ps.setString(1, namaTF.getText());
            ps.setString(2, semesterTF.getText());
            ps.setString(3, kelasTF.getText());
            ps.setString(4, nimTF.getText());
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
                kosongkanField();
                tampilkanData();
            } else {
                JOptionPane.showMessageDialog(this, "NIM tidak ditemukan!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal ubah: " + e.toString());
        }
    }

    private void hapusData() {
        try {
            PreparedStatement ps = koneksi.prepareStatement("DELETE FROM datamhs WHERE nim=?");
            ps.setString(1, nimTF.getText());
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                kosongkanField();
                tampilkanData();
            } else {
                JOptionPane.showMessageDialog(this, "NIM tidak ditemukan!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal hapus: " + e.toString());
        }
    }

    private void kosongkanField() {
        nimTF.setText("");
        namaTF.setText("");
        semesterTF.setText("");
        kelasTF.setText("");
        tabel.clearSelection();
    }
}
