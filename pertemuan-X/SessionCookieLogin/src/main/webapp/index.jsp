<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <style>
        body { font-family: Arial; display: flex; justify-content: center; margin-top: 80px; }
        .form-box { border: 1px solid #ccc; padding: 30px; border-radius: 8px; width: 300px; }
        input { width: 100%; padding: 8px; margin: 6px 0 14px 0; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer; }
    </style>
</head>
<body>
    <div class="form-box">
        <h2>Login</h2>

        <%
            String pesan = (String) session.getAttribute("pesanError");
            if (pesan != null) {
        %>
            <p style="color:red;"><%= pesan %></p>
        <%
                session.removeAttribute("pesanError");
            }
        %>

        <%
            String lastVisit = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("lastVisit")) {
                        lastVisit = c.getValue();
                    }
                }
            }
            if (lastVisit != null) {
        %>
            <p style="color:gray; font-size:12px;">Login terakhir: <%= lastVisit %></p>
        <%  } %>

        <form method="POST" action="Validasi.jsp">
            <label>Username:</label>
            <input type="text" name="username" required />

            <label>Password:</label>
            <input type="password" name="password" required />

            <button type="submit">Login</button>
        </form>
    </div>
</body>
</html>
