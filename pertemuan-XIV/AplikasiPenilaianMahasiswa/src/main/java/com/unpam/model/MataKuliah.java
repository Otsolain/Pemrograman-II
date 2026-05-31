package com.unpam.model;

import java.sql.*;
import com.unpam.view.PesanDialog;

public class MataKuliah {

    private String kodeMataKuliah, namaMataKuliah;
    private int jumlahSks;
    private String pesan;
    private Object[][] list;
    private final Koneksi koneksi         = new Koneksi();
    private final PesanDialog pesanDialog = new PesanDialog();

    public String getKodeMataKuliah()                    { return kodeMataKuliah; }
    public void setKodeMataKuliah(String kodeMataKuliah) { this.kodeMataKuliah = kodeMataKuliah; }
    public String getNamaMataKuliah()                    { return namaMataKuliah; }
    public void setNamaMataKuliah(String namaMataKuliah) { this.namaMataKuliah = namaMataKuliah; }
    public int getJumlahSks()                            { return jumlahSks; }
    public void setJumlahSks(int jumlahSks)              { this.jumlahSks = jumlahSks; }
    public String getPesan()                             { return pesan; }
    public Object[][] getList()                          { return list; }
    public void setList(Object[][] list)                 { this.list = list; }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            int jumlahSimpan = 0;
            boolean simpan   = false;
            String SQLStatemen = "";
            PreparedStatement ps = null;

            try {
                simpan       = true;
                SQLStatemen  = "INSERT INTO tbmatakuliah(kode_mk, nama_mk, sks) VALUES (?,?,?)";
                ps           = connection.prepareStatement(SQLStatemen);
                ps.setString(1, kodeMataKuliah);
                ps.setString(2, namaMataKuliah);
                ps.setInt(3, jumlahSks);
                jumlahSimpan = ps.executeUpdate();

                if (simpan && jumlahSimpan < 1) {
                    adaKesalahan = true;
                    pesan = "Gagal menyimpan data mata kuliah";
                }
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbmatakuliah\n" + ex;
            } finally {
                try {
                    if (ps != null) ps.close();
                    connection.close();
                } catch (SQLException e) {}
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
                Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = st.executeQuery("SELECT * FROM tbmatakuliah ORDER BY kode_mk ASC");
                rs.last();
                int jumlah   = rs.getRow();
                rs.beforeFirst();
                list = new Object[jumlah][3];
                int i = 0;
                while (rs.next()) {
                    list[i][0] = rs.getString("kode_mk");
                    list[i][1] = rs.getString("nama_mk");
                    list[i][2] = rs.getInt("sks");
                    i++;
                }
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbmatakuliah\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi\n" + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }

    public boolean cari() {
        boolean ketemu = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM tbmatakuliah WHERE kode_mk = ?");
                ps.setString(1, kodeMataKuliah);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    namaMataKuliah = rs.getString("nama_mk");
                    jumlahSks      = rs.getInt("sks");
                    ketemu         = true;
                } else {
                    pesan = "Data mata kuliah dengan kode " + kodeMataKuliah + " tidak ditemukan";
                }
                connection.close();
            } catch (SQLException ex) {
                pesan = "Tidak dapat membuka tabel tbmatakuliah\n" + ex;
            }
        } else {
            pesan = "Tidak dapat melakukan koneksi\n" + koneksi.getPesanKesalahan();
        }
        return ketemu;
    }
}
