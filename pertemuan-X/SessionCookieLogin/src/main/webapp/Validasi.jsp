<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@page import="javax.servlet.http.Cookie"%>
<%@page import="java.net.URLEncoder"%>

<%
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    if (username != null && password != null &&
        username.equals("ADMIN") && password.equals("ADMIN")) {

        session.setAttribute("userLogin", username);
        session.setAttribute("statusLogin", "Sudah Login");
        session.setMaxInactiveInterval(60 * 60 * 24);

        String lastVisit = URLEncoder.encode(new Date().toString(), "UTF-8");
        Cookie lastVisitCookie = new Cookie("lastVisit", lastVisit);
        lastVisitCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(lastVisitCookie);

        Cookie userCookie = new Cookie("user", username);
        userCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(userCookie);

        response.sendRedirect("beranda.jsp");

    } else {
        session.setAttribute("pesanError", "Username atau Password salah!");
        response.sendRedirect("index.jsp");
    }
%>
