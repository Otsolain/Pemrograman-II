package com.unpam.controller;

import com.unpam.model.Mahasiswa;
import com.unpam.model.Enkripsi;
import com.unpam.util.Auth;
import com.unpam.view.MainForm;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "MahasiswaController", urlPatterns = {"/MahasiswaController"})
public class MahasiswaController extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        if (!Auth.isAdmin(request)) {
            new MainForm().tampilkan(request, response,
                "<p style='color:red;'>Anda harus login sebagai admin untuk mengakses halaman ini.</p>"
                + "<a href='LoginController'>Login</a>");
            return;
        }

        String aksi  = request.getParameter("aksi");
        String konten = "";
        MainForm mainForm = new MainForm();
        Mahasiswa mhs     = new Mahasiswa();

        if (aksi == null) {
            if (mhs.tampilSemua()) {
                Object[][] list = mhs.getList();
                StringBuilder sb = new StringBuilder();
                sb.append("<h3>Data Mahasiswa</h3>");
                sb.append("<a href='MahasiswaController?aksi=tambah'>+ Tambah</a><br><br>");
                sb.append("<table border='1' cellpadding='6' style='border-collapse:collapse;'>");
                sb.append("<tr><th>NIM</th><th>Nama</th><th>Semester</th><th>Kelas</th></tr>");
                if (list != null) {
                    for (Object[] row : list) {
                        sb.append("<tr><td>").append(row[0]).append("</td>");
                        sb.append("<td>").append(row[1]).append("</td>");
                        sb.append("<td>").append(row[2]).append("</td>");
                        sb.append("<td>").append(row[3]).append("</td></tr>");
                    }
                }
                sb.append("</table>");
                konten = sb.toString();
            } else {
                konten = "<p style='color:red;'>" + mhs.getPesan() + "</p>";
            }
        } else if (aksi.equals("tambah")) {
            konten = "<h3>Tambah Mahasiswa</h3>"
                   + "<form method='POST' action='MahasiswaController'>"
                   + "<input type='hidden' name='aksi' value='simpan'>"
                   + "NIM: <input type='text' name='nim'><br><br>"
                   + "Nama: <input type='text' name='nama'><br><br>"
                   + "Semester: <input type='number' name='semester'><br><br>"
                   + "Kelas: <input type='text' name='kelas'><br><br>"
                   + "Password: <input type='password' name='password'><br><br>"
                   + "<input type='submit' value='Simpan'>"
                   + "</form>";
        } else if (aksi.equals("simpan")) {
            try {
                Enkripsi enkripsi = new Enkripsi();
                mhs.setNim(request.getParameter("nim"));
                mhs.setNama(request.getParameter("nama"));
                mhs.setSemester(Integer.parseInt(request.getParameter("semester")));
                mhs.setKelas(request.getParameter("kelas"));
                mhs.setPassword(enkripsi.hashMD5(request.getParameter("password")));

                if (mhs.simpan()) {
                    konten = "<p style='color:green;'>Data berhasil disimpan!</p>"
                           + "<a href='MahasiswaController'>Kembali ke Daftar</a>";
                } else {
                    konten = "<p style='color:red;'>Gagal: " + mhs.getPesan() + "</p>"
                           + "<a href='MahasiswaController?aksi=tambah'>Kembali</a>";
                }
            } catch (Exception ex) {
                konten = "<p>Error: " + ex.getMessage() + "</p>";
            }
        }

        mainForm.tampilkan(request, response, konten);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException { processRequest(req, res); }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException { processRequest(req, res); }
}
