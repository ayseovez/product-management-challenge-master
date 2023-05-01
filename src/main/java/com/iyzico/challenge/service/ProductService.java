package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.payload.request.ProductAddRequest;
import com.iyzico.challenge.payload.request.ProductUpdateRequest;
import com.iyzico.challenge.payload.response.*;
import com.iyzipay.model.BasketItem;

import java.util.List;

public interface ProductService {
    ProductAddResponse addProduct(ProductAddRequest request);
    ProductUpdateResponse updateProduct(Long productId, ProductUpdateRequest request);
    ProductRemoveResponse removeProduct(Long productId);
    ProductInfoResponse getProductInfo(Long id);
    List<Product> getProductList();

    ProductStockResponse reduceStock(List<BasketItem> items);
}
