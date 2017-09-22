package com.ymall.dao;

import com.ymall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectCartByUser(Integer userId);

    int selectCartProductCheckedByUserId(Integer userId);

    int deleteByUserIdAndProductIds(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdList);

    int checkedOrUncheckedProduct(@Param("userId") Integer userId,@Param("productId") Integer productId, @Param("checked") Integer checked);

    int selectCartProductCount(Integer userId);
}