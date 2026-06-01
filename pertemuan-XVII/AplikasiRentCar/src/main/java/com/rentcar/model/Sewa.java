package com.rentcar.model;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Sewa {
    private String kodeSewa, kodeCustomer, kodeMobil, status;
    private String tglSewa, tglKembaliRencana, tglKembaliAktual;
    private int lamaSewa;
    private double totalBiaya;
    private String pesan;
    private Object[][] list;
    private final Koneksi koneksi = new Koneksi();

    public String getKodeSewa()                { return kodeSewa; }
    public void setKodeSewa(String k)          { this.kodeSewa = k; }
    public String getKodeCustomer()            { return kodeCustomer; }
    public void setKodeCustomer(String k)      { this.kodeCustomer = k; }
    public String getKodeMobil()               { return kodeMobil; }
    public void setKodeMobil(String k)         { this.kodeMobil = k; }
    public String getTglSewa()                 { return tglSewa; }
    public void setTglSewa(String t)           { this.tglSewa = t; }
    public String getTglKembaliRencana()       { return tglKembaliRencana; }
    public void setTglKembaliRencana(String t) { this.tglKembaliRencana = t; }
    public String getTglKembaliAktual()        { return tglKembaliAktual; }
    public void setTglKembaliAktual(String t)  { this.tglKembaliAktual = t; }
    public int getLamaSewa()                   { return lamaSewa; }
    public double getTotalBiaya()              { return totalBiaya; }
    public String getStatus()                  { return status; }
    public String getPesan()                   { return pesan; }
    public Object[][] getList()                { return list; }

    public void hitungBiaya(double hargaPerHari) {
        LocalDate tgl1 = LocalDate.parse(tglSewa);
        LocalDate tgl2 = LocalDate.parse(tglKembaliRencana);
        long selisih = ChronoUnit.DAYS.between(tgl1, tgl2);
        lamaSewa   = (selisih < 1) ? 1 : (int) selisih;
        totalBiaya = lamaSewa * hargaPerHari;
    }

    public String generateKodeSewa() {
        String kodeBaru = "SW001";
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(
                    "SELECT MAX(kode_sewa) AS maksimal FROM tbsewa WHERE kode_sewa LIKE 'SW%'");
                if (rs.next() && rs.getString("maksimal") != null) {
                    String terakhir = rs.getString("maksimal");
                    int angka = Integer.parseInt(terakhir.substring(2)) + 1;
                    kodeBaru = String.format("SW%03d", angka);
                }
                st.close();
                conn.close();
            } catch (Exception ex) { pesan = "Error: " + ex; }
        } else { pesan = koneksi.getPesanKesalahan(); }
        return kodeBaru;
    }

    public boolean simpan() {
        boolean adaKesalahan = false;
        if (kodeSewa == null || kodeSewa.trim().isEmpty()) {
            kodeSewa = generateKodeSewa();
        }
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO tbsewa(kode_sewa, kode_customer, kode_mobil, tgl_sewa, " +
                    "tgl_kembali_rencana, lama_sewa, total_biaya, status) VALUES (?,?,?,?,?,?,?,'Aktif')");
                ps.setString(1, kodeSewa);
                ps.setString(2, kodeCustomer);
                ps.setString(3, kodeMobil);
                ps.setString(4, tglSewa);
                ps.setString(5, tglKembaliRencana);
                ps.setInt(6, lamaSewa);
                ps.setDouble(7, totalBiaya);
                if (ps.executeUpdate() < 1) { adaKesalahan = true; pesan = "Gagal menyimpan transaksi sewa"; }

                if (!adaKesalahan) {
                    PreparedStatement ps2 = conn.prepareStatement(
                        "UPDATE tbmobil SET status='Disewa' WHERE kode_mobil=?");
                    ps2.setString(1, kodeMobil);
                    ps2.executeUpdate();
                    ps2.close();
                }
                ps.close(); conn.close();
            } catch (SQLException ex) { adaKesalahan = true; pesan = "Error: " + ex; }
        } else { adaKesalahan = true; pesan = koneksi.getPesanKesalahan(); }
        return !adaKesalahan;
    }

    public boolean kembalikan() {
        boolean adaKesalahan = false;
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                String mobilTerkait = null;
                PreparedStatement cek = conn.prepareStatement(
                    "SELECT kode_mobil, status FROM tbsewa WHERE kode_sewa=?");
                cek.setString(1, kodeSewa);
                ResultSet rs = cek.executeQuery();
                if (rs.next()) {
                    mobilTerkait = rs.getString("kode_mobil");
                    if ("Selesai".equals(rs.getString("status"))) {
                        adaKesalahan = true;
                        pesan = "Transaksi sewa ini sudah dikembalikan sebelumnya";
                    }
                } else {
                    adaKesalahan = true;
                    pesan = "Kode sewa tidak ditemukan";
                }
                cek.close();

                if (!adaKesalahan) {
                    PreparedStatement ps = conn.prepareStatement(
                        "UPDATE tbsewa SET tgl_kembali_aktual=?, status='Selesai' WHERE kode_sewa=?");
                    ps.setString(1, tglKembaliAktual);
                    ps.setString(2, kodeSewa);
                    ps.executeUpdate();
                    ps.close();

                    PreparedStatement ps2 = conn.prepareStatement(
                        "UPDATE tbmobil SET status='Tersedia' WHERE kode_mobil=?");
                    ps2.setString(1, mobilTerkait);
                    ps2.executeUpdate();
                    ps2.close();
                }
                conn.close();
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
                ResultSet rs = st.executeQuery(
                    "SELECT s.kode_sewa, c.nama, m.nama_mobil, m.merk, " +
                    "s.tgl_sewa, s.tgl_kembali_rencana, s.tgl_kembali_aktual, " +
                    "s.lama_sewa, s.total_biaya, s.status " +
                    "FROM tbsewa s " +
                    "JOIN tbcustomer c ON s.kode_customer = c.kode_customer " +
                    "JOIN tbmobil m    ON s.kode_mobil    = m.kode_mobil " +
                    "ORDER BY s.tgl_sewa DESC");
                rs.last(); int jml = rs.getRow(); rs.beforeFirst();
                list = new Object[jml][10]; int i = 0;
                while (rs.next()) {
                    list[i][0] = rs.getString("kode_sewa");
                    list[i][1] = rs.getString("nama");
                    list[i][2] = rs.getString("nama_mobil") + " (" + rs.getString("merk") + ")";
                    list[i][3] = rs.getString("tgl_sewa");
                    list[i][4] = rs.getString("tgl_kembali_rencana");
                    list[i][5] = rs.getString("tgl_kembali_aktual");
                    list[i][6] = rs.getInt("lama_sewa");
                    list[i][7] = rs.getDouble("total_biaya");
                    list[i][8] = rs.getString("status");
                    i++;
                }
                conn.close();
            } catch (SQLException ex) { adaKesalahan = true; pesan = "Error: " + ex; }
        } else { adaKesalahan = true; pesan = koneksi.getPesanKesalahan(); }
        return !adaKesalahan;
    }
}
