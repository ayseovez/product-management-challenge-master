package com.iyzico.challenge.service;

import com.iyzico.challenge.payload.request.ProductPaymentRequest;
import com.iyzico.challenge.payload.response.ProductPaymentResponse;

public interface ProductPaymentService {
    ProductPaymentResponse productPay(ProductPaymentRequest request);

    default void pay(String a){
        System.out.println("ffvdfd");
    }

    static void paid(String dd){
        System.out.println("hh");
    }
}
