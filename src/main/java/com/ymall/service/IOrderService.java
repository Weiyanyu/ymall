package com.ymall.service;

import com.github.pagehelper.PageInfo;
import com.ymall.common.ServerResponse;
import com.ymall.vo.OrderVo;

import java.util.Map;

public interface IOrderService {
    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallBack(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, long orderNo);

    ServerResponse createOrder(Integer userId, Integer shippingId);

    ServerResponse<String> cancel(Integer userId, Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);

    ServerResponse<OrderVo> manageDetail(Long orderNo);

    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse<String> manageSendGoods(Long orderNo);
}
