package com.unpam.model;

import java.sql.*;

public class Nilai {

    private String nim, kodeMataKuliah;
    private double nilaiTugas, nilaiUTS, nilaiUAS;
    private String pesan;
    private Object[][] list;
    private final Koneksi koneksi = new Koneksi();

    public String getNim()                       { return nim; }
    public void setNim(String nim)               { this.nim = nim; }
    public String getKodeMataKuliah()            { return kodeMataKuliah; }
    public void setKodeMataKuliah(String k)      { this.kodeMataKuliah = k; }
    public double getNilaiTugas()                { return nilaiTugas; }
    public void setNilaiTugas(double nilaiTugas) { this.nilaiTugas = nilaiTugas; }
    public double getNilaiUTS()                  { return nilaiUTS; }
    public void setNilaiUTS(double nilaiUTS)     { this.nilaiUTS = nilaiUTS; }
    public double getNilaiUAS()                  { return nilaiUAS; }
    public void setNilaiUAS(double nilaiUAS)     { this.nilaiUAS = nilaiUAS; }
    public String getPesan()                     { return pesan; }
    public Object[][] getList()                  { return list; }
    public void setList(Object[][] list)         { this.list = list; }

    public double hitungNilaiAkhir() {
        return (nilaiTugas * 0.30) + (nilaiUTS * 0.30) + (nilaiUAS * 0.40);
    }

    public String hitungGrade() {
        double na = hitungNilaiAkhir();
        if      (na >= 85) return "A";
        else if (na >= 70) return "B";
        else if (na >= 60) return "C";
        else if (na >= 50) return "D";
        else               return "E";
    }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            int jumlahSimpan = 0;
            String SQLStatemen = "";
            PreparedStatement ps = null;

            try {
                double nilaiAkhir = hitungNilaiAkhir();
                String grade      = hitungGrade();

                SQLStatemen = "INSERT INTO tbnilai(nim, kode_mk, nilai_tugas, nilai_uts, nilai_uas, nilai_akhir, grade) "
                            + "VALUES (?,?,?,?,?,?,?)";
                ps = connection.prepareStatement(SQLStatemen);
                ps.setString(1, nim);
                ps.setString(2, kodeMataKuliah);
                ps.setDouble(3, nilaiTugas);
                ps.setDouble(4, nilaiUTS);
                ps.setDouble(5, nilaiUAS);
                ps.setDouble(6, nilaiAkhir);
                ps.setString(7, grade);

                jumlahSimpan = ps.executeUpdate();

                if (jumlahSimpan < 1) {
                    adaKesalahan = true;
                    pesan = "Gagal menyimpan data nilai";
                }
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat menyimpan nilai\n" + ex + "\n" + SQLStatemen;
            } finally {
                try { if (ps != null) ps.close(); connection.close(); } catch (SQLException e) {}
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi\n" + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }

    public boolean hapus() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(
                    "DELETE FROM tbnilai WHERE nim = ? AND kode_mk = ?");
                ps.setString(1, nim);
                ps.setString(2, kodeMataKuliah);
                if (ps.executeUpdate() < 1) {
                    adaKesalahan = true;
                    pesan = "Data nilai tidak ditemukan untuk dihapus";
                }
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat menghapus nilai\n" + ex;
            } finally {
                try { if (ps != null) ps.close(); connection.close(); } catch (SQLException e) {}
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi\n" + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }

    public boolean tampilSemua() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            try {
                Statement st = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
                );
                ResultSet rs = st.executeQuery(
                    "SELECT n.nim, m.nama, mk.nama_mk, n.nilai_tugas, n.nilai_uts, "
                  + "n.nilai_uas, n.nilai_akhir, n.grade "
                  + "FROM tbnilai n "
                  + "JOIN tbmahasiswa m   ON n.nim = m.nim "
                  + "JOIN tbmatakuliah mk ON n.kode_mk = mk.kode_mk "
                  + "ORDER BY n.nim ASC"
                );
                rs.last();
                int jumlah = rs.getRow();
                rs.beforeFirst();
                list = new Object[jumlah][8];
                int i = 0;
                while (rs.next()) {
                    list[i][0] = rs.getString("nim");
                    list[i][1] = rs.getString("nama");
                    list[i][2] = rs.getString("nama_mk");
                    list[i][3] = rs.getDouble("nilai_tugas");
                    list[i][4] = rs.getDouble("nilai_uts");
                    list[i][5] = rs.getDouble("nilai_uas");
                    list[i][6] = rs.getDouble("nilai_akhir");
                    list[i][7] = rs.getString("grade");
                    i++;
                }
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbnilai\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi\n" + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }

    public boolean tampilByNim(String nimMhs) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                    "SELECT mk.kode_mk, mk.nama_mk, mk.sks, "
                  + "n.nilai_tugas, n.nilai_uts, n.nilai_uas, n.nilai_akhir, n.grade "
                  + "FROM tbnilai n "
                  + "JOIN tbmatakuliah mk ON n.kode_mk = mk.kode_mk "
                  + "WHERE n.nim = ? "
                  + "ORDER BY mk.kode_mk ASC",
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ps.setString(1, nimMhs);
                ResultSet rs = ps.executeQuery();
                rs.last();
                int jumlah = rs.getRow();
                rs.beforeFirst();
                list = new Object[jumlah][8];
                int i = 0;
                while (rs.next()) {
                    list[i][0] = rs.getString("kode_mk");
                    list[i][1] = rs.getString("nama_mk");
                    list[i][2] = rs.getInt("sks");
                    list[i][3] = rs.getDouble("nilai_tugas");
                    list[i][4] = rs.getDouble("nilai_uts");
                    list[i][5] = rs.getDouble("nilai_uas");
                    list[i][6] = rs.getDouble("nilai_akhir");
                    list[i][7] = rs.getString("grade");
                    i++;
                }
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbnilai\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi\n" + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }
}
