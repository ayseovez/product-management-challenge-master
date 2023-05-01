package com.iyzico.challenge.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductUpdateResponse extends BaseResponse{
    private String name;
    private Integer stock;
    private BigDecimal price;
}
