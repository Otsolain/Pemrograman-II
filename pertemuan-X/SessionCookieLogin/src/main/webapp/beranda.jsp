<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Beranda</title>
    <style>
        body { font-family: Arial; margin: 40px; }
        .card { border: 1px solid #ddd; padding: 20px; border-radius: 8px; max-width: 400px; }
        .btn { padding: 8px 16px; margin: 5px; border: none; border-radius: 5px; cursor: pointer; }
        .btn-refresh { background: #28a745; color: white; }
        .btn-logout  { background: #dc3545; color: white; }
    </style>
</head>
<body>
<%
    String userLogin = (String) session.getAttribute("userLogin");
    if (userLogin == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
    <div class="card">
        <h2>Selamat Datang, <%= userLogin %>!</h2>
        <p>Status: <b><%= session.getAttribute("statusLogin") %></b></p>
        <p>Session ID: <code><%= session.getId() %></code></p>

        <hr>

        <%
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("lastVisit")) {
        %>
                        <p>Login terakhir (dari cookie): <b><%= c.getValue() %></b></p>
        <%
                    }
                }
            }
        %>

        <form method="GET" action="beranda.jsp">
            <button class="btn btn-refresh" type="submit">Refresh</button>
        </form>

        <form method="GET" action="Logout.jsp">
            <button class="btn btn-logout" type="submit">Logout</button>
        </form>
    </div>
</body>
</html>
