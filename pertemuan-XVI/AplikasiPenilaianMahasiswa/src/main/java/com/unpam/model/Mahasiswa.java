package com.unpam.model;

import java.sql.*;
import com.unpam.view.PesanDialog;

public class Mahasiswa {

    private String nim, nama, kelas, password;
    private int semester;
    private String pesan;
    private Object[][] list;
    private final Koneksi koneksi         = new Koneksi();
    private final PesanDialog pesanDialog = new PesanDialog();

    public String getNim()                   { return nim; }
    public void setNim(String nim)           { this.nim = nim; }
    public String getNama()                  { return nama; }
    public void setNama(String nama)         { this.nama = nama; }
    public String getKelas()                 { return kelas; }
    public void setKelas(String kelas)       { this.kelas = kelas; }
    public int getSemester()                 { return semester; }
    public void setSemester(int semester)    { this.semester = semester; }
    public String getPassword()              { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPesan()                 { return pesan; }
    public Object[][] getList()              { return list; }
    public void setList(Object[][] list)     { this.list = list; }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            int jumlahSimpan = 0;
            boolean simpan   = false;
            String SQLStatemen = "";
            PreparedStatement preparedStatement = null;

            try {
                simpan = true;
                SQLStatemen = "INSERT INTO tbmahasiswa(nim, nama, semester, kelas, password) VALUES (?,?,?,?,?)";

                preparedStatement = connection.prepareStatement(SQLStatemen);
                preparedStatement.setString(1, nim);
                preparedStatement.setString(2, nama);
                preparedStatement.setInt(3, semester);
                preparedStatement.setString(4, kelas);
                preparedStatement.setString(5, password);

                jumlahSimpan = preparedStatement.executeUpdate();

                if (simpan && jumlahSimpan < 1) {
                    adaKesalahan = true;
                    pesan = "Gagal menyimpan data mahasiswa";
                }

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbmahasiswa\n" + ex + "\n" + SQLStatemen;
            } finally {
                try {
                    if (preparedStatement != null) preparedStatement.close();
                    connection.close();
                } catch (SQLException e) {}
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }

    public boolean tampilSemua() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            try {
                Statement st  = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs  = st.executeQuery("SELECT * FROM tbmahasiswa ORDER BY nim ASC");
                rs.last();
                int jumlah    = rs.getRow();
                rs.beforeFirst();
                list = new Object[jumlah][5];
                int i = 0;
                while (rs.next()) {
                    list[i][0] = rs.getString("nim");
                    list[i][1] = rs.getString("nama");
                    list[i][2] = rs.getInt("semester");
                    list[i][3] = rs.getString("kelas");
                    list[i][4] = rs.getString("password");
                    i++;
                }
                connection.close();
            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbmahasiswa\n" + ex;
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
                    "SELECT * FROM tbmahasiswa WHERE nim = ?");
                ps.setString(1, nim);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    nama     = rs.getString("nama");
                    semester = rs.getInt("semester");
                    kelas    = rs.getString("kelas");
                    ketemu   = true;
                } else {
                    pesan = "Data mahasiswa dengan NIM " + nim + " tidak ditemukan";
                }
                connection.close();
            } catch (SQLException ex) {
                pesan = "Tidak dapat membuka tabel tbmahasiswa\n" + ex;
            }
        } else {
            pesan = "Tidak dapat melakukan koneksi\n" + koneksi.getPesanKesalahan();
        }
        return ketemu;
    }
}
