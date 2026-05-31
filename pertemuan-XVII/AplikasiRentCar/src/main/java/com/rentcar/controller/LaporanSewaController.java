package com.rentcar.controller;

import com.rentcar.model.Koneksi;
import com.rentcar.model.Sewa;
import com.rentcar.util.Auth;
import com.rentcar.view.MainForm;
import java.io.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import net.sf.jasperreports.engine.*;

@WebServlet(name = "LaporanSewaController", urlPatterns = {"/LaporanSewaController"})
public class LaporanSewaController extends HttpServlet {

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

        if ("cetak".equals(aksi)) {
            OutputStream os = null;
            Connection conn = null;
            try {
                String jrxmlPath = getServletContext().getRealPath("/reports/LaporanSewa.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlPath);

                Map<String, Object> params = new HashMap<>();
                params.put("REPORT_TITLE", "Laporan Transaksi Penyewaan Mobil");

                Koneksi koneksiObj = new Koneksi();
                conn = koneksiObj.getConnection();
                if (conn == null) throw new Exception(koneksiObj.getPesanKesalahan());

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

                response.setContentType("application/pdf");
                response.setContentLength(pdf.length);
                response.setHeader("Content-Disposition", "inline; filename=LaporanSewa.pdf");
                os = response.getOutputStream();
                os.write(pdf);
                os.flush();
                return;
            } catch (Exception ex) {
                konten = "<p style='color:red;'>Gagal mencetak: " + ex.getMessage() + "</p>"
                       + "<p><i>Pastikan file LaporanSewa.jrxml ada di folder Web Pages/reports/</i></p>"
                       + "<a href='LaporanSewaController'>Kembali</a>";
            } finally {
                if (conn != null) try { conn.close(); } catch (Exception e) {}
                if (os != null)   try { os.close(); }   catch (IOException e) {}
            }
        } else {
            Sewa sewa = new Sewa();
            sewa.tampilSemua();
            Object[][] list = sewa.getList();

            StringBuilder sb = new StringBuilder();
            sb.append("<h3>Laporan Transaksi Penyewaan</h3>");
            sb.append("<a href='LaporanSewaController?aksi=cetak' target='_blank'>Cetak PDF</a><br><br>");
            sb.append("<table border='1' cellpadding='5' style='border-collapse:collapse; font-size:85%;'>");
            sb.append("<tr style='background:#ddf;'><th>Kode Sewa</th><th>Customer</th><th>Mobil</th>"
                    + "<th>Tgl Sewa</th><th>Lama</th><th>Total Biaya</th><th>Status</th></tr>");
            double grandTotal = 0;
            if (list != null) for (Object[] row : list) {
                String sc = "Aktif".equals(row[8]) ? "orange" : "green";
                sb.append("<tr><td>").append(row[0]).append("</td><td>").append(row[1])
                  .append("</td><td>").append(row[2]).append("</td><td>").append(row[3])
                  .append("</td><td>").append(row[6]).append(" hari</td><td>Rp ")
                  .append(String.format("%,.0f", row[7]))
                  .append("</td><td style='color:").append(sc).append(";'><b>").append(row[8]).append("</b></td></tr>");
                grandTotal += (Double) row[7];
            }
            sb.append("<tr style='background:#eef;'><td colspan='5'><b>Grand Total</b></td>")
              .append("<td><b>Rp ").append(String.format("%,.0f", grandTotal)).append("</b></td><td></td></tr>");
            sb.append("</table>");
            konten = sb.toString();
        }

        mainForm.tampilkan(request, response, konten);
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
}
