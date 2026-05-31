package com.rentcar.view;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "MainForm", urlPatterns = {"/MainForm"})
public class MainForm extends HttpServlet {

    public void tampilkan(HttpServletRequest request,
                          HttpServletResponse response,
                          String konten)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);

        String userName = "";
        try { userName = (String) session.getAttribute("userName"); } catch (Exception ex) {}
        if (userName == null) userName = "";

        String menu;
        String topMenu;

        if (!userName.equals("")) {
            menu = "<br><b>" + userName + "</b><br><br>"
                 + "<b>Master Data</b><br>"
                 + "<a href='MobilController'>Data Mobil</a><br>"
                 + "<a href='CustomerController'>Data Customer</a><br><br>"
                 + "<b>Transaksi</b><br>"
                 + "<a href='SewaController'>Penyewaan</a><br>"
                 + "<a href='SewaController?aksi=kembali'>Pengembalian</a><br><br>"
                 + "<b>Laporan</b><br>"
                 + "<a href='LaporanSewaController'>Laporan Transaksi</a><br><br>"
                 + "<a href='LogoutController'>Logout</a><br>";

            topMenu = "<nav><ul>"
                 + "<li><a href='MainForm'>Home</a></li>"
                 + "<li><a href='#'>Master Data</a><ul>"
                 + "<li><a href='MobilController'>Mobil</a></li>"
                 + "<li><a href='CustomerController'>Customer</a></li>"
                 + "</ul></li>"
                 + "<li><a href='#'>Transaksi</a><ul>"
                 + "<li><a href='SewaController'>Penyewaan</a></li>"
                 + "<li><a href='SewaController?aksi=kembali'>Pengembalian</a></li>"
                 + "</ul></li>"
                 + "<li><a href='#'>Laporan</a><ul>"
                 + "<li><a href='LaporanSewaController'>Laporan Transaksi</a></li>"
                 + "</ul></li>"
                 + "<li><a href='LogoutController'>Logout</a></li>"
                 + "</ul></nav>";

            if (konten.equals("")) konten = "<br><h1>Selamat Datang</h1><h2>" + userName + "</h2>";
        } else {
            menu = "<br><b>Silakan Login</b><br><br>"
                 + "<a href='LoginController'>Login</a><br>";

            topMenu = "<nav><ul>"
                 + "<li><a href='MainForm'>Home</a></li>"
                 + "<li><a href='LoginController'>Login</a></li>"
                 + "</ul></nav>";

            if (konten.equals("")) {
                konten = "<br><h2>Selamat Datang di Aplikasi Rent Car</h2>"
                       + "<p>Silakan <a href='LoginController'>login</a> terlebih dahulu untuk mengakses aplikasi.</p>";
            }
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html lang='id'><head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<link href='style.css' rel='stylesheet' type='text/css'/>");
            out.println("<title>Aplikasi Rent Car</title></head>");
            out.println("<body>");

            out.println("<header class='app-header'>");
            out.println("  <div class='app-header-inner'>");
            out.println("    <div class='logo-circle'>RC</div>");
            out.println("    <div class='app-header-text'>");
            out.println("      <h1 class='app-title'>Aplikasi Penyewaan Mobil (Rent Car)</h1>");
            out.println("      <p class='app-address'>Sistem Informasi Manajemen Kendaraan</p>");
            out.println("    </div>");
            out.println("  </div>");
            out.println("  <div class='topbar'>" + topMenu + "</div>");
            out.println("</header>");

            out.println("<div class='layout'>");
            out.println("  <aside class='sidebar'><div id='menu'>" + menu + "</div></aside>");
            out.println("  <main class='content'>" + konten + "</main>");
            out.println("</div>");

            out.println("<footer class='app-footer'>Copyright &copy; 2026 Rent Car System</footer>");
            out.println("</body></html>");
        }
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { tampilkan(req, res, ""); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { tampilkan(req, res, ""); }
}
