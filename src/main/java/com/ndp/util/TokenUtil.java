package com.ndp.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class TokenUtil {

    private TokenUtil() {
        super();
    }

    public static String getTokenFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return StringUtils.isEmpty(authorization) ? null : authorization.replace("Bearer ", "");
    }

    public static String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access-token")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
