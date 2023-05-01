package com.iyzico.challenge.service.impl;

import com.iyzico.challenge.entity.ProductPay;
import com.iyzico.challenge.payload.request.ProductPaymentRequest;
import com.iyzico.challenge.payload.response.ProductPaymentResponse;
import com.iyzico.challenge.payload.response.ProductStockResponse;
import com.iyzico.challenge.repository.ProductPaymentRepository;
import com.iyzico.challenge.service.ProductPaymentService;
import com.iyzico.challenge.util.Mapper;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.Locale;

@Service
@AllArgsConstructor
@Transactional
public class ProductPaymentServiceImpl implements ProductPaymentService {
    private final Logger LOGGER = LogManager.getLogger(ProductPaymentServiceImpl.class);

    private final ProductServiceImpl productService;

    private final ProductPaymentRepository productPaymentRepository;

    private final MessageSource messageSource;

    private final Mapper mapper;

    @Override
    public ProductPaymentResponse productPay(ProductPaymentRequest request) {
        ProductPaymentResponse response = new ProductPaymentResponse();
        ProductStockResponse result = new ProductStockResponse();
        try {
            ProductPay productPay = productPaymentRepository.save(mapper.mapperEntity(request));
            CreatePaymentRequest requestPayment = mapper.mapperToPaymentRequest(request, productPay);
            Options options = mapper.getOptions();

            Payment payment = Payment.create(requestPayment, options);
            if (!payment.getStatus().equals(Status.SUCCESS.getValue())){
                response.setReturnCode("0");
                response.setReturnMessage(messageSource.getMessage("product.generic.exception", null, Locale.US));
                LOGGER.error("Payment service has been returned failure ");
                return response;
            }
            result = productService.reduceStock(request.getBasketItems());


        }catch (OptimisticLockException e) {
            response.setReturnCode(result.getReturnCode());
            response.setReturnMessage(result.getReturnMessage());
            LOGGER.error(result.getReturnMessage());
            return response;
        }catch (Exception e){
            response.setReturnCode("0");
            response.setReturnMessage(messageSource.getMessage("product.generic.exception", null, Locale.US));
            LOGGER.error(messageSource.getMessage("product.generic.exception", null, Locale.US)+e.getMessage());
            return response;
        }
        response.setReturnCode(result.getReturnCode().equals("0") ? "0" : "200");
        response.setReturnMessage(result.getReturnCode().equals("0") ? result.getReturnMessage() : "Your payment has been successfully completed.");
        LOGGER.info(response.getReturnMessage());
        return response;
    }


}
