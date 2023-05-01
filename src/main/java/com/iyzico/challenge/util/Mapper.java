package com.iyzico.challenge.util;

import com.iyzico.challenge.entity.ProductPay;
import com.iyzico.challenge.payload.request.ProductPaymentRequest;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class Mapper {

/*    @Value("${iyzico.pay.test.apiKey}")
    private String apiKey;

    @Value("${iyzico.pay.test.secretKey}")
    private String secretKey;

    @Value("${iyzico.pay.test.baseUrl}")
    private String baseUrl;*/

    @Autowired
    GenericOperations genericOperations;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime now = LocalDateTime.now();

    public ProductPay mapperEntity(ProductPaymentRequest request){
        BigDecimal sumPrice = genericOperations.sumPriceCalculation(request.getBasketItems());
        BigDecimal pricePayable = genericOperations.priceCalculation(sumPrice, new BigDecimal(request.getInstallment()));
        ProductPay productPay = new ProductPay();
        productPay.setConversationId(genericOperations.generatorNumber());
        productPay.setCustomerIdentityNumber(request.getCustomerIdentityNumber());
        productPay.setPrice(sumPrice);
        productPay.setPaidPrice(pricePayable);
        productPay.setTime(dtf.format(now));
        productPay.setPaymentStatus(Status.SUCCESS);
        return productPay;
    }

    public CreatePaymentRequest mapperToPaymentRequest(ProductPaymentRequest request, ProductPay productPay){

        CreatePaymentRequest requestPayment = new CreatePaymentRequest();
        requestPayment.setLocale(Locale.TR.getValue());
        requestPayment.setConversationId(genericOperations.generatorNumber());
        requestPayment.setPrice(productPay.getPrice());
        requestPayment.setPaidPrice(productPay.getPaidPrice());
        requestPayment.setCurrency(Currency.TRY.name());
        requestPayment.setInstallment(Integer.valueOf(request.getInstallment()));
        requestPayment.setBasketId(productPay.getId().toString());
        requestPayment.setPaymentChannel(PaymentChannel.WEB.name());
        requestPayment.setPaymentGroup(PaymentGroup.PRODUCT.name());

        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName(request.getPaymentCard().getCardHolderName());
        paymentCard.setCardNumber(request.getPaymentCard().getCardNumber());
        paymentCard.setExpireMonth(request.getPaymentCard().getExpireMonth());
        paymentCard.setExpireYear(request.getPaymentCard().getExpireYear());
        paymentCard.setCvc(request.getPaymentCard().getCvc());
        paymentCard.setRegisterCard(request.getPaymentCard().getRegisterCard());
        requestPayment.setPaymentCard(paymentCard);

        Buyer buyer = new Buyer();
        buyer.setId(request.getCustomerIdentityNumber());
        buyer.setName(request.getName());
        buyer.setSurname(request.getSurname());
        buyer.setGsmNumber(request.getMobile());
        buyer.setEmail(request.getEmail());
        buyer.setIdentityNumber(request.getCustomerIdentityNumber());
        buyer.setLastLoginDate(dtf.format(now));
        buyer.setRegistrationDate(dtf.format(now));
        buyer.setRegistrationAddress(request.getAddress().getAddress());
        buyer.setIp("85.34.78.112");
        buyer.setCity(request.getAddress().getCity());
        buyer.setCountry(request.getAddress().getCountry());
        buyer.setZipCode(request.getAddress().getZipCode());
        requestPayment.setBuyer(buyer);

        Address shippingAddress = new Address();
        shippingAddress.setContactName(request.getAddress().getContactName());
        shippingAddress.setCity(request.getAddress().getCity());
        shippingAddress.setCountry(request.getAddress().getCountry());
        shippingAddress.setAddress(request.getAddress().getAddress());
        shippingAddress.setZipCode(request.getAddress().getZipCode());
        requestPayment.setShippingAddress(shippingAddress);
        requestPayment.setBillingAddress(shippingAddress);

        requestPayment.setBasketItems(request.getBasketItems());

        return requestPayment;
    }

    public Options getOptions(){
        Options options = new Options();
        options.setApiKey("sandbox-Edr075BZdgLGqmHYrsdx9tV3HKIulWM7");
        options.setSecretKey("sandbox-xFhP4fZaRB2qCwGqEdvqySVV7eZmuMOP");
        options.setBaseUrl("https://sandbox-api.iyzipay.com");

        return options;
    }
}
