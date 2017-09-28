package com.ymall.dao;

import com.ymall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectOrderByUserIdAndOrderId(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    Order selectOrderByOrderNo(long orderNo);

    List<Order> selectOrdersByUserId(Integer userId);

    List<Order> selectAll();
}