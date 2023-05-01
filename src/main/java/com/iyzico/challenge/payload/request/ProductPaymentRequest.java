package com.iyzico.challenge.payload.request;

import com.iyzipay.model.Address;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.PaymentCard;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class ProductPaymentRequest {
    @NotEmpty(message = "The ID is required.")
    private String customerIdentityNumber;
    @NotEmpty(message = "The name is required.")
    private String name;
    @NotEmpty(message = "The surname is required.")
    private String surname;
    @NotEmpty(message = "The email is required.")
    private String email;
    @NotEmpty(message = "The mobile number is required.")
    private String mobile;
    private Address address;
    private PaymentCard paymentCard;
    private String installment;
    private List<BasketItem> basketItems;
}
