package com.ymall.controller.common.interceptor;

import com.ymall.common.Const;
import com.ymall.common.ServerResponse;
import com.ymall.pojo.User;
import com.ymall.util.CookieUtil;
import com.ymall.util.JsonUtil;
import com.ymall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.security.Key;
import java.util.Map;

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("preHandle");

        HandlerMethod handlerMethod = (HandlerMethod) o;

        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        for (Object obj: paramMap.entrySet()) {
            Map.Entry<String, String[]> entry = (Map.Entry) obj;
            String mapKey = entry.getKey();
            String[] value = entry.getValue();
            String mapValue = "";
            if (ArrayUtils.isNotEmpty(value)) {
                mapValue = ArrayUtils.toString(value);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        User user = null;
        String loginToken = CookieUtil.readCookie(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userLoginJson = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.stringToObject(userLoginJson, User.class);
        }

        if (user == null || (!user.getRole().equals(Const.Role.ROLE_ADMIN))) {
            httpServletResponse.reset();  //重置response
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            PrintWriter writer = httpServletResponse.getWriter();

            if (user == null) {
                writer.write(JsonUtil.objToString(ServerResponse.createByErrorMessage("拦截器拦截，请先登录")));
            }
            else {
                writer.write(JsonUtil.objToString(ServerResponse.createByErrorMessage("拦截器拦截，请登录管理员账号")));
            }

            writer.flush();
            writer.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
