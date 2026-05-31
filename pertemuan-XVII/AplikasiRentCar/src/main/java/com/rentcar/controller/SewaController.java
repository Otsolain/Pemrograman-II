package com.rentcar.controller;

import com.rentcar.model.*;
import com.rentcar.util.Auth;
import com.rentcar.view.MainForm;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "SewaController", urlPatterns = {"/SewaController"})
public class SewaController extends HttpServlet {

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
        Sewa sewa     = new Sewa();

        if (aksi == null) {
            if (sewa.tampilSemua()) {
                Object[][] list = sewa.getList();
                StringBuilder sb = new StringBuilder();
                sb.append("<h3>Daftar Transaksi Penyewaan</h3>");
                sb.append("<a href='SewaController?aksi=tambah'>+ Input Penyewaan</a>&nbsp;&nbsp;");
                sb.append("<a href='SewaController?aksi=kembali'>Pengembalian</a><br><br>");
                sb.append("<table border='1' cellpadding='5' style='border-collapse:collapse; font-size:85%;'>");
                sb.append("<tr style='background:#ddf;'><th>Kode Sewa</th><th>Customer</th><th>Mobil</th>"
                        + "<th>Tgl Sewa</th><th>Tgl Rencana Kembali</th><th>Tgl Kembali</th>"
                        + "<th>Lama</th><th>Total Biaya</th><th>Status</th></tr>");
                if (list != null) for (Object[] row : list) {
                    String sc = "Aktif".equals(row[8]) ? "orange" : "green";
                    sb.append("<tr><td>").append(row[0]).append("</td><td>").append(row[1])
                      .append("</td><td>").append(row[2]).append("</td><td>").append(row[3])
                      .append("</td><td>").append(row[4]).append("</td><td>").append(row[5] != null ? row[5] : "-")
                      .append("</td><td>").append(row[6]).append(" hari</td><td>Rp ")
                      .append(String.format("%,.0f", row[7]))
                      .append("</td><td style='color:").append(sc).append(";'><b>").append(row[8]).append("</b></td></tr>");
                }
                sb.append("</table>");
                konten = sb.toString();
            } else {
                konten = "<p style='color:red;'>" + sewa.getPesan() + "</p>";
            }

        } else if (aksi.equals("tambah")) {
            Mobil mobil       = new Mobil();     mobil.tampilTersedia();
            Customer customer = new Customer();  customer.tampilSemua();

            StringBuilder sb = new StringBuilder();
            sb.append("<h3>Form Penyewaan Mobil</h3>");
            sb.append("<form method='POST' action='SewaController'>");
            sb.append("<input type='hidden' name='aksi' value='simpan'>");
            sb.append("<table cellpadding='6'>");
            sb.append("<tr><td>Kode Sewa</td><td><input type='text' name='kodeSewa' placeholder='SW001'></td></tr>");

            sb.append("<tr><td>Customer</td><td><select name='kodeCustomer'>");
            if (customer.getList() != null) for (Object[] row : customer.getList())
                sb.append("<option value='").append(row[0]).append("'>").append(row[0]).append(" - ").append(row[1]).append("</option>");
            sb.append("</select></td></tr>");

            sb.append("<tr><td>Mobil (Tersedia)</td><td><select name='kodeMobil'>");
            if (mobil.getList() != null) for (Object[] row : mobil.getList())
                sb.append("<option value='").append(row[0]).append("'>")
                  .append(row[1]).append(" - ").append(row[2])
                  .append(" | Rp ").append(String.format("%,.0f", row[5])).append("/hari</option>");
            sb.append("</select></td></tr>");

            sb.append("<tr><td>Tanggal Sewa</td><td><input type='date' name='tglSewa'></td></tr>");
            sb.append("<tr><td>Tanggal Kembali Rencana</td><td><input type='date' name='tglKembaliRencana'></td></tr>");
            sb.append("<tr><td colspan='2'><input type='submit' value='Simpan Transaksi'> ");
            sb.append("<a href='SewaController'>Batal</a></td></tr>");
            sb.append("</table></form>");
            sb.append("<p><small>Harga sewa per hari diambil otomatis dari data mobil yang dipilih.</small></p>");
            konten = sb.toString();

        } else if (aksi.equals("simpan")) {
            sewa.setKodeSewa(request.getParameter("kodeSewa"));
            sewa.setKodeCustomer(request.getParameter("kodeCustomer"));
            sewa.setKodeMobil(request.getParameter("kodeMobil"));
            sewa.setTglSewa(request.getParameter("tglSewa"));
            sewa.setTglKembaliRencana(request.getParameter("tglKembaliRencana"));

            Mobil mobilDipilih = new Mobil();
            mobilDipilih.setKodeMobil(request.getParameter("kodeMobil"));
            double harga = mobilDipilih.cari() ? mobilDipilih.getHargaSewa() : 0;
            sewa.hitungBiaya(harga);
            konten = sewa.simpan()
                ? "<p style='color:green;'><b>Transaksi sewa berhasil disimpan!</b></p>"
                  + "<p>Lama Sewa: <b>" + sewa.getLamaSewa() + " hari</b></p>"
                  + "<p>Total Biaya: <b>Rp " + String.format("%,.0f", sewa.getTotalBiaya()) + "</b></p>"
                  + "<a href='SewaController'>Kembali ke Daftar</a>"
                : "<p style='color:red;'>Gagal: " + sewa.getPesan() + "</p><a href='SewaController?aksi=tambah'>Kembali</a>";

        } else if (aksi.equals("kembali")) {
            konten = "<h3>Form Pengembalian Mobil</h3>"
                   + "<form method='POST' action='SewaController'>"
                   + "<input type='hidden' name='aksi' value='prosesKembali'>"
                   + "<table cellpadding='6'>"
                   + "<tr><td>Kode Sewa</td><td><input type='text' name='kodeSewa' placeholder='SW001'></td></tr>"
                   + "<tr><td>Tanggal Kembali Aktual</td><td><input type='date' name='tglKembaliAktual'></td></tr>"
                   + "<tr><td colspan='2'><input type='submit' value='Proses Pengembalian'> "
                   + "<a href='SewaController'>Batal</a></td></tr>"
                   + "</table></form>";

        } else if (aksi.equals("prosesKembali")) {
            sewa.setKodeSewa(request.getParameter("kodeSewa"));
            sewa.setTglKembaliAktual(request.getParameter("tglKembaliAktual"));
            konten = sewa.kembalikan()
                ? "<p style='color:green;'><b>Pengembalian mobil berhasil diproses!</b></p><a href='SewaController'>Kembali ke Daftar</a>"
                : "<p style='color:red;'>Gagal: " + sewa.getPesan() + "</p><a href='SewaController?aksi=kembali'>Kembali</a>";
        }

        mainForm.tampilkan(request, response, konten);
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
}
