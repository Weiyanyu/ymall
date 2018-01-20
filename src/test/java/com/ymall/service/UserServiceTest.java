package com.ymall.service;

import com.ymall.common.Const;
import com.ymall.common.ServerResponse;
import com.ymall.pojo.User;
import com.ymall.util.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@Transactional
@TransactionConfiguration
public class UserServiceTest {
    @Autowired
    private IUserService userService;


    @Test
    public void testCheckValid() {
        ServerResponse response = userService.checkValid("yeonon", Const.USERNAME);
        assertEquals(response.getMsg(), "用户名已存在");
        response = userService.checkValid("abc",Const.USERNAME);
        assertEquals(response.getMsg(),"校验成功");
        response = userService.checkValid("724199771@qq.com", Const.EMAIL);
        assertEquals(response.getMsg(), "email已存在");
        response = userService.checkValid("yeonon","");
        assertEquals(response.getMsg(), "参数错误，校验失败");
    }

    @Test
    public void testRegister() {
        User user = new User();
        user.setUsername("weiyanyu");
        user.setPassword("asd124563");
        user.setEmail("2758256762@qq.com");
        ServerResponse response = userService.register(user);
        assertEquals(response.getMsg(), "注册成功");

        response = userService.register(user);
        assertEquals(response.getMsg(),"用户名已存在");
        User user2 = new User();
        user2.setUsername("xiangjinwei");
        user2.setPassword("dasd3");
        user2.setEmail("2758256762@qq.com");
        response = userService.register(user2);
        assertEquals(response.getMsg(),"email已存在");

        //注册失败的可能性应该是插入数据库失败，在这里不便于测试

    }


    @Test
    public void testLogin() {

        //这里测试比较麻烦，因为数据库里的密码使用MD5加密，所以这里先注册一个新用户，再测试登录功能
        //恰巧也验证了安全特性
        User user = new User();
        user.setUsername("weiyanyu");
        user.setPassword("asd124563");
        userService.register(user);
        ServerResponse response = userService.login("weiyanyu","asd124563");

        assertEquals(response.isSuccess(), true);
        response = userService.login("weiyanyu", "dasjkj");
        assertEquals(response.getMsg(), "密码错误");
        response = userService.login("dasdjas","asd124563");
        assertEquals(response.getMsg(),"用户名错误");
    }

    @Test
    public void testSelectQuestion() {
        ServerResponse response = userService.selectQuestion("yeonon");
        assertEquals(response.getData(), "你是谁");
        response = userService.selectQuestion("abc");
        assertEquals(response.getMsg(), "用户不存在");

        User user = new User();
        user.setUsername("xiangjinwei");
        user.setPassword("abc");
        userService.register(user);
        response = userService.selectQuestion("xiangjinwei");
        assertEquals(response.getMsg(), "找回密码得问题是空的");

    }




}
