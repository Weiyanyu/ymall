package com.ymall.dao;

import com.ymall.pojo.User;
import org.apache.commons.lang3.StringUtils;
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
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)

//添加事务管理，注解声明该测试需要在事务环境下进行unit test， 并且自动回滚，不会破坏原有的数据库内容
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectLoginMapper() {
        User user1 = userMapper.selectLogin("yeonon", "asd124563");
        assertNotNull(user1);
        assertEquals(user1.getPassword(), "asd124563");
        System.out.println(user1.getCreateTime());
    }

    @Test
    public void testCheckUserName() {
        int resultCount = userMapper.checkUserName("yeonon");
        assertEquals(1, resultCount);
        resultCount = userMapper.checkUserName("dasjdkasjkl");
        assertEquals(0, resultCount);

    }

    @Test
    public void testCheckUserEmail() {
        int resultCount = userMapper.checkUserEmail("724199771@qq.com");
        assertEquals(1, resultCount);
        resultCount = userMapper.checkUserEmail("dasdasd@qq.com");
        assertEquals(0, resultCount);
    }


    @Test
    public void testSelectQuestion() {
        String result = userMapper.selectQuestion("yeonon");
        assertEquals("你是谁", result);
        assertNotEquals("嘿嘿嘿", result);
    }

    @Test
    public void testCheckAnswer() {
        int resultCount = userMapper.checkAnswer("yeonon","你是谁", "韦燕宇");
        assertEquals(1, resultCount);
        resultCount = userMapper.checkAnswer("yeonon", "你是谁","yeonon");
        assertEquals(0, resultCount);
    }

    @Test
    public void testUpdatePassword() {
        int rowCount = userMapper.updatePassword("yeonon","124563");
        assertEquals(1, rowCount);
        rowCount = userMapper.updatePassword("dasjkldjaskl", "124563");
        assertEquals(0, rowCount);
    }

}
