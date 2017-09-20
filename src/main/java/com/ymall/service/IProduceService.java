package com.ymall.service;

import com.github.pagehelper.PageInfo;
import com.ymall.common.ServerResponse;
import com.ymall.pojo.Product;
import com.ymall.vo.ProductDetailVO;

public interface IProduceService {
    ServerResponse saveOrUpdateProduce(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVO> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVO> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
