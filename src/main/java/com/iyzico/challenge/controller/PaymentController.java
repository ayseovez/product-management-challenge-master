package com.iyzico.challenge.controller;

import com.iyzico.challenge.payload.request.ProductPaymentRequest;
import com.iyzico.challenge.payload.response.ProductPaymentResponse;
import com.iyzico.challenge.service.ProductPaymentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/iyzicoPay")
public class PaymentController {

    private final ProductPaymentService productPaymentService;

    @PostMapping("/payment")
    public ProductPaymentResponse productPay(@RequestBody ProductPaymentRequest request){
        ProductPaymentResponse response = productPaymentService.productPay(request);
        return response;
    }
}
