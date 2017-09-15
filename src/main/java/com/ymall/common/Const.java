package com.ymall.common;

public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role {
        int ROLE_CUSTOMER = 1;  //普通用户
        int ROLE_ADMIN = 2;   //管理员
    }
}
