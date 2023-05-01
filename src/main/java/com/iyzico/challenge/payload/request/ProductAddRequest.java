package com.iyzico.challenge.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductAddRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
}
