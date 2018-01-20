package com.ymall.controller.common;

import com.ymall.common.Const;
import com.ymall.pojo.User;
import com.ymall.util.CookieUtil;
import com.ymall.util.JsonUtil;
import com.ymall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionExpireFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String loginToken = CookieUtil.readCookie(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userLoginJson = RedisShardedPoolUtil.get(loginToken);
            User user = JsonUtil.stringToObject(userLoginJson, User.class);
            if (user != null) {
                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExTime.exTime);
            }
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
