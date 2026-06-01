<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    session.invalidate();

    Cookie userCookie      = new Cookie("user", "");
    Cookie lastVisitCookie = new Cookie("lastVisit", "");
    userCookie.setMaxAge(0);
    lastVisitCookie.setMaxAge(0);
    response.addCookie(userCookie);
    response.addCookie(lastVisitCookie);

    response.sendRedirect("index.jsp");
%>
