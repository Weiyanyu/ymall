package com.ymall.service.Impl;

import com.ymall.common.ResponseCode;
import com.ymall.common.ServerResponse;
import com.ymall.dao.UserMapper;
import com.ymall.pojo.User;
import com.ymall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录service
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUserName(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名错误");
        }

        //todo md5 密码加密

        User user = userMapper.selectLogin(username, password);

        if (user == null) {
            //这里返回密码错误消息是因为上面已经判断过用户名是否正确了
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }
}
