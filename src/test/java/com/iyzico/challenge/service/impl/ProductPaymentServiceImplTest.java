package com.iyzico.challenge.service.impl;

import com.iyzico.challenge.entity.ProductPay;
import com.iyzico.challenge.payload.request.ProductPaymentRequest;
import com.iyzico.challenge.payload.response.ProductPaymentResponse;
import com.iyzico.challenge.payload.response.ProductStockResponse;
import com.iyzico.challenge.repository.ProductPaymentRepository;
import com.iyzico.challenge.util.Mapper;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductPaymentServiceImplTest {

    @InjectMocks
    ProductPaymentServiceImpl productPaymentService;

    @Mock
    ProductServiceImpl productService;

    @Mock
    MessageSource messageSource;

    @Mock
    ProductPaymentRepository productPaymentRepository;

    @Mock
    Mapper mapper;

    ProductPay productPay;
    CreatePaymentRequest requestPayment;
    ProductPaymentRequest request;

    @Before
    public void setUp() throws Exception {
        productPay = new ProductPay();
        productPay.setConversationId("12345678");
        productPay.setCustomerIdentityNumber("12345678900");
        productPay.setPrice(new BigDecimal("20.00"));
        productPay.setPaidPrice(new BigDecimal("24.00"));
        productPay.setTime("2021-01-01 02:02:03");
        productPay.setPaymentStatus(Status.SUCCESS);

        request = new ProductPaymentRequest();
        request.setCustomerIdentityNumber("12345678900");
        request.setName("Alex");
        request.setSurname("Treb");
        request.setEmail("alex.trb@gmail.com");
        request.setMobile("5557891212");
        request.setInstallment("1");

        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName("Alex Treb");
        paymentCard.setCardNumber("5528790000000008");
        paymentCard.setExpireMonth("12");
        paymentCard.setExpireYear("2030");
        paymentCard.setCvc("123");
        paymentCard.setRegisterCard(0);
        request.setPaymentCard(paymentCard);

        List<BasketItem> basketItems = new ArrayList<BasketItem>();
        BasketItem firstBasketItem = new BasketItem();
        firstBasketItem.setId("1");
        firstBasketItem.setName("Airpods");
        firstBasketItem.setCategory1("Collectibles");
        firstBasketItem.setCategory2("Accessories");
        firstBasketItem.setItemType(BasketItemType.PHYSICAL.name());
        firstBasketItem.setPrice(new BigDecimal("20.00"));
        basketItems.add(firstBasketItem);
        request.setBasketItems(basketItems);


        requestPayment = new CreatePaymentRequest();
        requestPayment.setLocale(Locale.TR.getValue());
        requestPayment.setConversationId("12345678");
        requestPayment.setPrice(productPay.getPrice());
        requestPayment.setPaidPrice(productPay.getPaidPrice());
        requestPayment.setCurrency(Currency.TRY.name());
        requestPayment.setInstallment(Integer.valueOf(request.getInstallment()));
        requestPayment.setBasketId("1");
        requestPayment.setPaymentChannel(PaymentChannel.WEB.name());
        requestPayment.setPaymentGroup(PaymentGroup.PRODUCT.name());
        requestPayment.setPaymentCard(paymentCard);

        Buyer buyer = new Buyer();
        buyer.setId(request.getCustomerIdentityNumber());
        buyer.setName(request.getName());
        buyer.setSurname(request.getSurname());
        buyer.setGsmNumber(request.getMobile());
        buyer.setEmail(request.getEmail());
        buyer.setIdentityNumber(request.getCustomerIdentityNumber());
        buyer.setLastLoginDate("2021-01-01 02:02:03");
        buyer.setRegistrationDate("2021-01-01 02:02:03");
        buyer.setRegistrationAddress("Istanbul");
        buyer.setIp("85.34.78.112");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        buyer.setZipCode("34567");
        requestPayment.setBuyer(buyer);

        Address shippingAddress = new Address();
        shippingAddress.setContactName("Alex Treb");
        shippingAddress.setCity("Istanbul");
        shippingAddress.setCountry("Turkey");
        shippingAddress.setAddress("Istanbul");
        shippingAddress.setZipCode("34567");
        requestPayment.setShippingAddress(shippingAddress);
        requestPayment.setBillingAddress(shippingAddress);
        requestPayment.setBasketItems(request.getBasketItems());


    }

    @Test
    public void productPay() {

        Mockito.when(mapper.mapperEntity(request)).thenReturn(productPay);
        Mockito.when(productPaymentRepository.save(productPay)).thenReturn(productPay);
        Mockito.when(mapper.mapperToPaymentRequest(request,productPay)).thenReturn(requestPayment);
        Mockito.when(messageSource.getMessage("product.generic.exception", null, java.util.Locale.US)).thenReturn("An error has occurred in the system. ");

        Options options = new Options();
        options.setApiKey("sandbox-Edr075BZdgLGqmHYrsdx9tV3HKIulWM7");
        options.setSecretKey("sandbox-xFhP4fZaRB2qCwGqEdvqySVV7eZmuMOP");
        options.setBaseUrl("https://sandbox-api.iyzipay.com");
        Mockito.when(mapper.getOptions()).thenReturn(options);


        ProductStockResponse response = new ProductStockResponse();
        response.setReturnCode("200");
        Mockito.when(productService.reduceStock(request.getBasketItems())).thenReturn(response);


        ProductPaymentResponse result = productPaymentService.productPay(request);
        Assert.assertEquals(result.getReturnCode(), "200");
    }
}