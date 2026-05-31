package com.rentcar.model;

import java.sql.*;

public class Mobil {
    private String kodeMobil, namaMobil, merk, status;
    private int tahun, kapasitas;
    private double hargaSewa;
    private String pesan;
    private Object[][] list;
    private final Koneksi koneksi = new Koneksi();

    public String getKodeMobil()       { return kodeMobil; }
    public void setKodeMobil(String k) { this.kodeMobil = k; }
    public String getNamaMobil()       { return namaMobil; }
    public void setNamaMobil(String n) { this.namaMobil = n; }
    public String getMerk()            { return merk; }
    public void setMerk(String m)      { this.merk = m; }
    public int getTahun()              { return tahun; }
    public void setTahun(int t)        { this.tahun = t; }
    public int getKapasitas()          { return kapasitas; }
    public void setKapasitas(int k)    { this.kapasitas = k; }
    public double getHargaSewa()       { return hargaSewa; }
    public void setHargaSewa(double h) { this.hargaSewa = h; }
    public String getStatus()          { return status; }
    public void setStatus(String s)    { this.status = s; }
    public String getPesan()           { return pesan; }
    public Object[][] getList()        { return list; }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO tbmobil VALUES (?,?,?,?,?,?,'Tersedia')");
                ps.setString(1, kodeMobil);
                ps.setString(2, namaMobil);
                ps.setString(3, merk);
                ps.setInt(4, tahun);
                ps.setInt(5, kapasitas);
                ps.setDouble(6, hargaSewa);
                if (ps.executeUpdate() < 1) { adaKesalahan = true; pesan = "Gagal menyimpan data mobil"; }
                ps.close(); conn.close();
            } catch (SQLException ex) { adaKesalahan = true; pesan = "Error: " + ex; }
        } else { adaKesalahan = true; pesan = koneksi.getPesanKesalahan(); }
        return !adaKesalahan;
    }

    public boolean tampilSemua() {
        boolean adaKesalahan = false;
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = st.executeQuery("SELECT * FROM tbmobil ORDER BY kode_mobil ASC");
                rs.last(); int jml = rs.getRow(); rs.beforeFirst();
                list = new Object[jml][7]; int i = 0;
                while (rs.next()) {
                    list[i][0] = rs.getString("kode_mobil");
                    list[i][1] = rs.getString("nama_mobil");
                    list[i][2] = rs.getString("merk");
                    list[i][3] = rs.getInt("tahun");
                    list[i][4] = rs.getInt("kapasitas");
                    list[i][5] = rs.getDouble("harga_sewa");
                    list[i][6] = rs.getString("status");
                    i++;
                }
                conn.close();
            } catch (SQLException ex) { adaKesalahan = true; pesan = "Error: " + ex; }
        } else { adaKesalahan = true; pesan = koneksi.getPesanKesalahan(); }
        return !adaKesalahan;
    }

    public boolean cari() {
        boolean ketemu = false;
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM tbmobil WHERE kode_mobil=?");
                ps.setString(1, kodeMobil);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    namaMobil = rs.getString("nama_mobil");
                    merk      = rs.getString("merk");
                    tahun     = rs.getInt("tahun");
                    kapasitas = rs.getInt("kapasitas");
                    hargaSewa = rs.getDouble("harga_sewa");
                    status    = rs.getString("status");
                    ketemu    = true;
                } else {
                    pesan = "Mobil dengan kode " + kodeMobil + " tidak ditemukan";
                }
                conn.close();
            } catch (SQLException ex) { pesan = "Error: " + ex; }
        } else { pesan = koneksi.getPesanKesalahan(); }
        return ketemu;
    }

    public boolean tampilTersedia() {
        boolean adaKesalahan = false;
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = st.executeQuery("SELECT * FROM tbmobil WHERE status='Tersedia' ORDER BY kode_mobil ASC");
                rs.last(); int jml = rs.getRow(); rs.beforeFirst();
                list = new Object[jml][7]; int i = 0;
                while (rs.next()) {
                    list[i][0] = rs.getString("kode_mobil");
                    list[i][1] = rs.getString("nama_mobil");
                    list[i][2] = rs.getString("merk");
                    list[i][3] = rs.getInt("tahun");
                    list[i][4] = rs.getInt("kapasitas");
                    list[i][5] = rs.getDouble("harga_sewa");
                    list[i][6] = rs.getString("status");
                    i++;
                }
                conn.close();
            } catch (SQLException ex) { adaKesalahan = true; pesan = "Error: " + ex; }
        } else { adaKesalahan = true; pesan = koneksi.getPesanKesalahan(); }
        return !adaKesalahan;
    }
}
