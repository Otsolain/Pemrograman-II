package com.unpam.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Auth {

    public static String getRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        Object r = session.getAttribute("role");
        return (r == null) ? null : r.toString();
    }

    public static boolean isLogin(HttpServletRequest request) {
        return getRole(request) != null;
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return "admin".equals(getRole(request));
    }

    public static boolean isMahasiswa(HttpServletRequest request) {
        return "mahasiswa".equals(getRole(request));
    }

    public static String getNim(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        Object n = session.getAttribute("nim");
        return (n == null) ? null : n.toString();
    }
}
