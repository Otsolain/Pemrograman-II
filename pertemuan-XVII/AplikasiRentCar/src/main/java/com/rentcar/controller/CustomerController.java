package com.rentcar.controller;

import com.rentcar.model.Customer;
import com.rentcar.util.Auth;
import com.rentcar.view.MainForm;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "CustomerController", urlPatterns = {"/CustomerController"})
public class CustomerController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        MainForm mainForm = new MainForm();

        if (!Auth.isLogin(request)) {
            mainForm.tampilkan(request, response,
                "<p style='color:red;'>Anda harus login terlebih dahulu.</p><a href='LoginController'>Login</a>");
            return;
        }

        String aksi       = request.getParameter("aksi");
        String konten     = "";
        Customer customer = new Customer();

        if (aksi == null) {
            if (customer.tampilSemua()) {
                Object[][] list = customer.getList();
                StringBuilder sb = new StringBuilder();
                sb.append("<h3>Data Customer</h3>");
                sb.append("<a href='CustomerController?aksi=tambah'>+ Tambah Customer</a><br><br>");
                sb.append("<table border='1' cellpadding='6' style='border-collapse:collapse; font-size:90%;'>");
                sb.append("<tr style='background:#ddf;'><th>Kode</th><th>Nama</th><th>Alamat</th><th>No. Telepon</th><th>No. KTP</th></tr>");
                if (list != null) for (Object[] row : list)
                    sb.append("<tr><td>").append(row[0]).append("</td><td>").append(row[1])
                      .append("</td><td>").append(row[2]).append("</td><td>").append(row[3])
                      .append("</td><td>").append(row[4]).append("</td></tr>");
                sb.append("</table>");
                konten = sb.toString();
            } else {
                konten = "<p style='color:red;'>" + customer.getPesan() + "</p>";
            }
        } else if (aksi.equals("tambah")) {
            konten = "<h3>Tambah Data Customer</h3>"
                   + "<form method='POST' action='CustomerController'>"
                   + "<input type='hidden' name='aksi' value='simpan'>"
                   + "<table cellpadding='6'>"
                   + "<tr><td>Kode Customer</td><td><input type='text' name='kodeCustomer'></td></tr>"
                   + "<tr><td>Nama</td><td><input type='text' name='nama'></td></tr>"
                   + "<tr><td>Alamat</td><td><textarea name='alamat' rows='3' cols='25'></textarea></td></tr>"
                   + "<tr><td>No. Telepon</td><td><input type='text' name='noTelepon'></td></tr>"
                   + "<tr><td>No. KTP</td><td><input type='text' name='noKtp'></td></tr>"
                   + "<tr><td colspan='2'><input type='submit' value='Simpan'> "
                   + "<a href='CustomerController'>Batal</a></td></tr>"
                   + "</table></form>";
        } else if (aksi.equals("simpan")) {
            customer.setKodeCustomer(request.getParameter("kodeCustomer"));
            customer.setNama(request.getParameter("nama"));
            customer.setAlamat(request.getParameter("alamat"));
            customer.setNoTelepon(request.getParameter("noTelepon"));
            customer.setNoKtp(request.getParameter("noKtp"));
            konten = customer.simpan()
                ? "<p style='color:green;'><b>Data customer berhasil disimpan!</b></p><a href='CustomerController'>Kembali</a>"
                : "<p style='color:red;'>Gagal: " + customer.getPesan() + "</p><a href='CustomerController?aksi=tambah'>Kembali</a>";
        }
        mainForm.tampilkan(request, response, konten);
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
}
