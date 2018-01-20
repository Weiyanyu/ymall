package com.ymall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {
    private static String COOKIE_DOMAIN = ".yeonon.com";
    private static String COOKIE_NAME = "ymall_login_token";


    /**
     * 写cookie,有请求过来的时候会有响应，在响应中对客户端写入cookie，设置maxAge会写入硬盘中，否则只会写入内存中
     */

    public static void writeCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*365);
        cookie.setHttpOnly(true);

        log.info("write cookie name {} value {}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    /**
     *读取请求中和要求的cookie相同的cookie value
     */
    public static String readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    log.info("read cookie name {} value {}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 删除cookie,设置maxAge为0，再写入即可实现删除
     */
    public static void delCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    cookie.setPath("/");
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    log.info("del cookie name {} value {}", cookie.getName(), cookie.getValue());
                    return;
                }
            }
        }
    }


}
