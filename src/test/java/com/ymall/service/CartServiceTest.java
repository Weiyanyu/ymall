package com.ymall.service;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;


public class CartServiceTest {
    @Test
    public void testBigDecimalTest() {
        BigDecimal bigDecimal = new BigDecimal("0.05");
        BigDecimal bigDecimal1 = new BigDecimal("0.01");
        System.out.println(bigDecimal.add(bigDecimal1));
    }
}
