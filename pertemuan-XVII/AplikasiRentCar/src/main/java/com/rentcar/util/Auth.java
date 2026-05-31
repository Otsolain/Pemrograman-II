package com.rentcar.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Auth {

    public static boolean isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        Object u = session.getAttribute("userName");
        return u != null && !u.toString().isEmpty();
    }
}
