package com.ymall.controller.backend;

import com.ymall.common.Const;
import com.ymall.common.ServerResponse;
import com.ymall.pojo.User;
import com.ymall.service.IUserService;

import com.ymall.util.CookieUtil;
import com.ymall.util.JsonUtil;
import com.ymall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user")
public class UserManagerController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                CookieUtil.writeCookie(httpServletResponse,session.getId());
                RedisShardedPoolUtil.setex(session.getId(),
                        JsonUtil.objToPrettyString(response.getData()),
                        Const.RedisCacheExTime.exTime);
                return ServerResponse.createBySuccess(user);
            }

            return ServerResponse.createByErrorMessage("不是管理员");
        }

        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> logout(HttpServletRequest request, HttpServletResponse response) {
        String loginToken = CookieUtil.readCookie(request);
        CookieUtil.delCookie(request, response);
        RedisShardedPoolUtil.del(loginToken);
        return ServerResponse.createBySuccess();
    }
}
