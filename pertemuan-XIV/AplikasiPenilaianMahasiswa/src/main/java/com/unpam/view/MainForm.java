package com.unpam.view;

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

        String role     = "";
        String userName = "";
        try { role     = (String) session.getAttribute("role"); }     catch (Exception ex) {}
        try { userName = (String) session.getAttribute("userName"); } catch (Exception ex) {}
        if (role == null) role = "";
        if (userName == null) userName = "";

        String menu;
        String topMenu;

        if (role.equals("admin")) {
            menu = "<br><b>" + userName + "</b> (Admin)<br><br>"
                 + "<b>Master Data</b><br>"
                 + "<a href='MahasiswaController'>Mahasiswa</a><br>"
                 + "<a href='MataKuliahController'>Mata Kuliah</a><br><br>"
                 + "<a href='LogoutController'>Logout</a><br><br>";

            topMenu = "<nav><ul>"
                 + "<li><a href='MainForm'>Home</a></li>"
                 + "<li><a href='#'>Master Data</a>"
                 + "<ul>"
                 + "<li><a href='MahasiswaController'>Mahasiswa</a></li>"
                 + "<li><a href='MataKuliahController'>Mata Kuliah</a></li>"
                 + "</ul></li>"
                 + "<li><a href='LogoutController'>Logout</a></li>"
                 + "</ul></nav>";

        } else if (role.equals("mahasiswa")) {
            menu = "<br><b>" + userName + "</b> (Mahasiswa)<br><br>"
                 + "<b>Menu</b><br>"
                 + "<a href='LogoutController'>Logout</a><br><br>";

            topMenu = "<nav><ul>"
                 + "<li><a href='MainForm'>Home</a></li>"
                 + "<li><a href='LogoutController'>Logout</a></li>"
                 + "</ul></nav>";

        } else {
            menu = "<br><b>Silakan Login</b><br><br>"
                 + "<a href='LoginController'>Login</a><br><br>";

            topMenu = "<nav><ul>"
                 + "<li><a href='MainForm'>Home</a></li>"
                 + "<li><a href='LoginController'>Login</a></li>"
                 + "</ul></nav>";

            if (konten.equals("")) {
                konten = "<br><h2>Selamat Datang</h2>"
                       + "<p>Silakan <a href='LoginController'>login</a> terlebih dahulu untuk mengakses aplikasi.</p>";
            }
        }

        if (!role.equals("") && konten.equals("")) {
            konten = "<br><h1>Selamat Datang</h1><h2>" + userName + "</h2>";
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='id'><head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<link href='style.css' rel='stylesheet' type='text/css'/>");
            out.println("<title>Informasi Nilai Mahasiswa - Universitas Pamulang</title></head>");
            out.println("<body>");

            out.println("<header class='app-header'>");
            out.println("  <div class='app-header-inner'>");
            out.println("    <div class='logo-circle'>UP</div>");
            out.println("    <div class='app-header-text'>");
            out.println("      <p class='app-subtitle'>Informasi Nilai Mahasiswa</p>");
            out.println("      <h1 class='app-title'>UNIVERSITAS PAMULANG</h1>");
            out.println("      <p class='app-address'>Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p>");
            out.println("    </div>");
            out.println("  </div>");
            out.println("  <div class='topbar'>" + topMenu + "</div>");
            out.println("</header>");

            out.println("<div class='layout'>");
            out.println("  <aside class='sidebar'><div id='menu'>" + menu + "</div></aside>");
            out.println("  <main class='content'>" + konten + "</main>");
            out.println("</div>");

            out.println("<footer class='app-footer'>");
            out.println("  Copyright &copy; 2026 Universitas Pamulang<br>");
            out.println("  Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten");
            out.println("</footer>");

            out.println("</body></html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException { tampilkan(req, res, ""); }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException { tampilkan(req, res, ""); }
}
