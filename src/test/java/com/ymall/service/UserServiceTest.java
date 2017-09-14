package com.ymall.service;


import com.ymall.dao.UserMapper;
import com.ymall.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserServiceTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void loginTest() {
        User user = userMapper.selectLogin("yeonon", "124563");
        System.out.println(user.getUsername());
    }
}
