package com.iyzico.challenge.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductUpdateRequest {
    private String name;
    private BigDecimal price;
    private Integer stock;
}
