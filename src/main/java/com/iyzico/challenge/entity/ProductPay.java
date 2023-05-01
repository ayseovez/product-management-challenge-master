package com.iyzico.challenge.entity;

import com.iyzipay.model.Status;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "PRODUCTPAY")
public class ProductPay {

    @Id
    @GeneratedValue
    private Long id;
    private String conversationId;
    private String customerIdentityNumber;
    private BigDecimal price;
    private BigDecimal paidPrice;
    private String time;
    private Status paymentStatus;
}
