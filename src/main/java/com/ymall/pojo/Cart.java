package com.ymall.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


/**
 * 这里getter,setter AllArgsConstructor, NoArgsConstructor 都是lombok框架提供的注解，旨在简化代码
 * setter (添加字段setter方法)
 * getter (添加字段getter方法)
 * AllArgsConstructor (添加所有字段的全参构造器)
 * NoArgsConstructor  (添加无参构造器)
 *
 * pojo 包里的基本都是这样,有些还有EqualAndHashCode是生成equal 和 hashcode方法
 * (可以指定特定字段或者排除特定字段参与，默认全字段参与)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;



}