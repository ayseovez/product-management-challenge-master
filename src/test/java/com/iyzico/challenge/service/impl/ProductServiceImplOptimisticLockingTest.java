package com.iyzico.challenge.service.impl;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.payload.response.ProductStockResponse;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplOptimisticLockingTest extends TestCase {

    @Autowired
    private ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @Before
    public void setUp() throws Exception{
        productRepository.save(new Product(1L, "Airpods", "Electronics", new BigDecimal("140.00"), 1));
        productRepository.save(new Product(2L, "Airpods Pro", "Electronics", new BigDecimal("150.00"), 10));
    }

    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void reduceStock() {
        Product product1 = productRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Product not found by id: "));
        Product product2 = productRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Product not found by id: "));

        product1.setStock(12);
        product2.setStock(9);

        assertEquals(0, product1.getVersion().intValue());
        assertEquals(0, product2.getVersion().intValue());

        product1 = productRepository.save(product1);
        product2 = productRepository.save(product2);

        System.out.println(product1);
        System.out.println(product2);

    }


}