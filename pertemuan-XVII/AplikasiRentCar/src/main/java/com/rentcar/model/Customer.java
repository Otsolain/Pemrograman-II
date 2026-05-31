package com.rentcar.model;

import java.sql.*;

public class Customer {
    private String kodeCustomer, nama, alamat, noTelepon, noKtp, pesan;
    private Object[][] list;
    private final Koneksi koneksi = new Koneksi();

    public String getKodeCustomer()       { return kodeCustomer; }
    public void setKodeCustomer(String k) { this.kodeCustomer = k; }
    public String getNama()               { return nama; }
    public void setNama(String n)         { this.nama = n; }
    public String getAlamat()             { return alamat; }
    public void setAlamat(String a)       { this.alamat = a; }
    public String getNoTelepon()          { return noTelepon; }
    public void setNoTelepon(String n)    { this.noTelepon = n; }
    public String getNoKtp()              { return noKtp; }
    public void setNoKtp(String n)        { this.noKtp = n; }
    public String getPesan()              { return pesan; }
    public Object[][] getList()           { return list; }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO tbcustomer VALUES (?,?,?,?,?)");
                ps.setString(1, kodeCustomer);
                ps.setString(2, nama);
                ps.setString(3, alamat);
                ps.setString(4, noTelepon);
                ps.setString(5, noKtp);
                if (ps.executeUpdate() < 1) { adaKesalahan = true; pesan = "Gagal menyimpan data customer"; }
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
                ResultSet rs = st.executeQuery("SELECT * FROM tbcustomer ORDER BY kode_customer ASC");
                rs.last(); int jml = rs.getRow(); rs.beforeFirst();
                list = new Object[jml][5]; int i = 0;
                while (rs.next()) {
                    list[i][0] = rs.getString("kode_customer");
                    list[i][1] = rs.getString("nama");
                    list[i][2] = rs.getString("alamat");
                    list[i][3] = rs.getString("no_telepon");
                    list[i][4] = rs.getString("no_ktp");
                    i++;
                }
                conn.close();
            } catch (SQLException ex) { adaKesalahan = true; pesan = "Error: " + ex; }
        } else { adaKesalahan = true; pesan = koneksi.getPesanKesalahan(); }
        return !adaKesalahan;
    }
}
