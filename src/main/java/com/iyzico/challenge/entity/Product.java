package com.iyzico.challenge.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "PRODUCT")
public class Product {

    public Product() {
    }

    public Product(Long id, String name, String category, BigDecimal price, Integer stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer stock;
    @Version
    private Long version;
}
