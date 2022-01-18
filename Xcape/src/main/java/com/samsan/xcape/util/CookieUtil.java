package com.samsan.xcape.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {

    /**
     * getCookie * * @return cookie * @throws Exception
     */
    public static String getCookie(HttpServletRequest request, String key) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (key == null) return null;
        String value = "";
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (key.equals(cookies[i].getName())) {
                    value = java.net.URLDecoder.decode(cookies[i].getValue(), "UTF-8");
                    break;
                }
            }
        }
        return value;
    }


}
