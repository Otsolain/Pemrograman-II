package com.rentcar.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "LogoutController", urlPatterns = {"/LogoutController"})
public class LogoutController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        response.sendRedirect("LoginController");
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException { processRequest(req, res); }
}
