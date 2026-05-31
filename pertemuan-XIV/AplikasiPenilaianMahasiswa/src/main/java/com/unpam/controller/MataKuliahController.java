package com.unpam.controller;

import com.unpam.model.MataKuliah;
import com.unpam.util.Auth;
import com.unpam.view.MainForm;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "MataKuliahController", urlPatterns = {"/MataKuliahController"})
public class MataKuliahController extends HttpServlet {

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
        MataKuliah mk     = new MataKuliah();

        if (aksi == null) {
            if (mk.tampilSemua()) {
                Object[][] list = mk.getList();
                StringBuilder sb = new StringBuilder();
                sb.append("<h3>Data Mata Kuliah</h3>");
                sb.append("<a href='MataKuliahController?aksi=tambah'>+ Tambah</a><br><br>");
                sb.append("<table border='1' cellpadding='6' style='border-collapse:collapse;'>");
                sb.append("<tr><th>Kode MK</th><th>Nama Mata Kuliah</th><th>SKS</th></tr>");
                if (list != null) {
                    for (Object[] row : list) {
                        sb.append("<tr><td>").append(row[0]).append("</td>");
                        sb.append("<td>").append(row[1]).append("</td>");
                        sb.append("<td>").append(row[2]).append("</td></tr>");
                    }
                }
                sb.append("</table>");
                konten = sb.toString();
            } else {
                konten = "<p style='color:red;'>" + mk.getPesan() + "</p>";
            }
        } else if (aksi.equals("tambah")) {
            konten = "<h3>Tambah Mata Kuliah</h3>"
                   + "<form method='POST' action='MataKuliahController'>"
                   + "<input type='hidden' name='aksi' value='simpan'>"
                   + "Kode MK: <input type='text' name='kodeMataKuliah'><br><br>"
                   + "Nama Mata Kuliah: <input type='text' name='namaMataKuliah'><br><br>"
                   + "SKS: <input type='number' name='jumlahSks'><br><br>"
                   + "<input type='submit' value='Simpan'>"
                   + "</form>";
        } else if (aksi.equals("simpan")) {
            try {
                mk.setKodeMataKuliah(request.getParameter("kodeMataKuliah"));
                mk.setNamaMataKuliah(request.getParameter("namaMataKuliah"));
                mk.setJumlahSks(Integer.parseInt(request.getParameter("jumlahSks")));

                if (mk.simpan()) {
                    konten = "<p style='color:green;'>Data berhasil disimpan!</p>"
                           + "<a href='MataKuliahController'>Kembali ke Daftar</a>";
                } else {
                    konten = "<p style='color:red;'>Gagal: " + mk.getPesan() + "</p>"
                           + "<a href='MataKuliahController?aksi=tambah'>Kembali</a>";
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
