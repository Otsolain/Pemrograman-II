package com.rentcar.controller;

import com.rentcar.model.Enkripsi;
import com.rentcar.model.Koneksi;
import com.rentcar.view.MainForm;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String konten   = "";
        MainForm mainForm = new MainForm();

        if (username == null || password == null) {
            konten = "<br><h3>Login</h3>"
                   + "<form method='POST' action='LoginController'>"
                   + "<table align='center' cellpadding='4'>"
                   + "<tr><td align='right'>Username</td><td><input type='text' name='username'></td></tr>"
                   + "<tr><td align='right'>Password</td><td><input type='password' name='password'></td></tr>"
                   + "<tr><td colspan='2' align='center'><input type='submit' value='Login'></td></tr>"
                   + "</table></form>"
                   + "<p align='center'><small>Gunakan username <b>admin</b> password <b>admin</b></small></p>";
        } else {
            try {
                Enkripsi enkripsi = new Enkripsi();
                String passHash   = enkripsi.hashMD5(password);
                Koneksi koneksi   = new Koneksi();
                Connection conn   = koneksi.getConnection();

                if (conn != null) {
                    PreparedStatement ps = conn.prepareStatement(
                        "SELECT nama FROM tbuser WHERE username=? AND password=?");
                    ps.setString(1, username);
                    ps.setString(2, passHash);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        HttpSession session = request.getSession(true);
                        session.setAttribute("userName", rs.getString("nama"));
                        session.setAttribute("username", username);
                        conn.close();
                        response.sendRedirect("MainForm");
                        return;
                    } else {
                        konten = "<br><h3 style='color:red;'>Username atau Password salah!</h3>"
                               + "<a href='LoginController'>Kembali</a>";
                    }
                    conn.close();
                } else {
                    konten = "<br><p style='color:red;'>" + koneksi.getPesanKesalahan() + "</p>";
                }
            } catch (Exception ex) {
                konten = "<br><p>Error: " + ex.getMessage() + "</p>";
            }
        }
        mainForm.tampilkan(request, response, konten);
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
}
