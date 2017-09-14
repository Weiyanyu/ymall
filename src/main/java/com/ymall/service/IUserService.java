package com.ymall.service;

import com.ymall.common.ServerResponse;
import com.ymall.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String username, String password);
}
