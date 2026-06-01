package com.rentcar.controller;

import com.rentcar.model.Mobil;
import com.rentcar.util.Auth;
import com.rentcar.view.MainForm;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "MobilController", urlPatterns = {"/MobilController"})
public class MobilController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        MainForm mainForm = new MainForm();

        if (!Auth.isLogin(request)) {
            mainForm.tampilkan(request, response,
                "<p style='color:red;'>Anda harus login terlebih dahulu.</p><a href='LoginController'>Login</a>");
            return;
        }

        String aksi   = request.getParameter("aksi");
        String konten = "";
        Mobil mobil   = new Mobil();

        if (aksi == null) {
            if (mobil.tampilSemua()) {
                Object[][] list = mobil.getList();
                StringBuilder sb = new StringBuilder();
                sb.append("<h3>Data Mobil</h3>");
                sb.append("<a href='MobilController?aksi=tambah'>+ Tambah Mobil</a><br><br>");
                sb.append("<table border='1' cellpadding='6' style='border-collapse:collapse; font-size:90%;'>");
                sb.append("<tr style='background:#ddf;'><th>Kode</th><th>Nama</th><th>Merk</th><th>Tahun</th><th>Kapasitas</th><th>Harga/Hari</th><th>Status</th><th>Aksi</th></tr>");
                if (list != null) for (Object[] row : list) {
                    String statusColor = "Tersedia".equals(row[6]) ? "green" : "red";
                    sb.append("<tr><td>").append(row[0]).append("</td><td>").append(row[1])
                      .append("</td><td>").append(row[2]).append("</td><td>").append(row[3])
                      .append("</td><td>").append(row[4]).append(" org</td><td>Rp ")
                      .append(String.format("%,.0f", row[5]))
                      .append("</td><td style='color:").append(statusColor).append(";'><b>").append(row[6]).append("</b></td>")
                      .append("<td><a href='MobilController?aksi=hapus&kodeMobil=").append(row[0])
                      .append("' onclick=\"return confirm('Hapus mobil ").append(row[0]).append("?');\">Hapus</a></td></tr>");
                }
                sb.append("</table>");
                konten = sb.toString();
            } else {
                konten = "<p style='color:red;'>" + mobil.getPesan() + "</p>";
            }
        } else if (aksi.equals("tambah")) {
            konten = "<h3>Tambah Data Mobil</h3>"
                   + "<form method='POST' action='MobilController'>"
                   + "<input type='hidden' name='aksi' value='simpan'>"
                   + "<table cellpadding='6'>"
                   + "<tr><td>Kode Mobil</td><td><input type='text' name='kodeMobil'></td></tr>"
                   + "<tr><td>Nama Mobil</td><td><input type='text' name='namaMobil'></td></tr>"
                   + "<tr><td>Merk</td><td><input type='text' name='merk'></td></tr>"
                   + "<tr><td>Tahun</td><td><input type='number' name='tahun'></td></tr>"
                   + "<tr><td>Kapasitas (orang)</td><td><input type='number' name='kapasitas'></td></tr>"
                   + "<tr><td>Harga Sewa/Hari (Rp)</td><td><input type='number' name='hargaSewa'></td></tr>"
                   + "<tr><td colspan='2'><input type='submit' value='Simpan'> "
                   + "<a href='MobilController'>Batal</a></td></tr>"
                   + "</table></form>";
        } else if (aksi.equals("simpan")) {
            mobil.setKodeMobil(request.getParameter("kodeMobil"));
            mobil.setNamaMobil(request.getParameter("namaMobil"));
            mobil.setMerk(request.getParameter("merk"));
            mobil.setTahun(Integer.parseInt(request.getParameter("tahun")));
            mobil.setKapasitas(Integer.parseInt(request.getParameter("kapasitas")));
            mobil.setHargaSewa(Double.parseDouble(request.getParameter("hargaSewa")));
            konten = mobil.simpan()
                ? "<p style='color:green;'><b>Data mobil berhasil disimpan!</b></p><a href='MobilController'>Kembali</a>"
                : "<p style='color:red;'>Gagal: " + mobil.getPesan() + "</p><a href='MobilController?aksi=tambah'>Kembali</a>";
        } else if (aksi.equals("hapus")) {
            mobil.setKodeMobil(request.getParameter("kodeMobil"));
            konten = mobil.hapus()
                ? "<p style='color:green;'><b>Data mobil berhasil dihapus!</b></p><a href='MobilController'>Kembali</a>"
                : "<p style='color:red;'>Gagal: " + mobil.getPesan() + "</p><a href='MobilController'>Kembali</a>";
        }
        mainForm.tampilkan(request, response, konten);
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
}
