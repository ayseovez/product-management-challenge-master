package com.iyzico.challenge.service.impl;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.payload.request.ProductAddRequest;
import com.iyzico.challenge.payload.request.ProductUpdateRequest;
import com.iyzico.challenge.payload.response.*;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzipay.model.BasketItem;
import junit.framework.TestCase;
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
import java.util.Locale;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceImplTest extends TestCase {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    MessageSource messageSource;

    @Mock
    ProductRepository productRepository;

    Product product;

    @Before
    public void setUp(){
        product = new Product();
        product.setId(1L);
        product.setName("Macbook");
        product.setCategory("Electronics");
        product.setPrice(BigDecimal.valueOf(200.00));
        product.setStock(1);
        product.setVersion(0L);
    }


    @Test
    public void testAddProduct() {
        ProductAddRequest request = new ProductAddRequest();
        request.setName("Macbook");
        request.setDescription("Elec");
        request.setPrice(BigDecimal.valueOf(300.00));
        request.setStock(10);

        Product product2 = null;
        product2.setName(request.getName());
        product2.setCategory(request.getDescription());
        product2.setPrice(request.getPrice());
        product2.setStock(request.getStock());
        product2.setVersion(0L);

        Mockito.when(productRepository.save(product2)).thenReturn(product2);
        ProductAddResponse result = productService.addProduct(request);

        Assert.assertEquals(result.getName(), product2.getName());

    }

    @Test
    public void testUpdateProduct() {

        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setPrice(BigDecimal.valueOf(30000));
        request.setStock(15);

        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Mockito.when(productRepository.save(product)).thenReturn(product);
        ProductUpdateResponse result = productService.updateProduct(1L, request);

        Assert.assertEquals(result.getStock(), product.getStock());

    }

    @Test
    public void testRemoveProduct() {

        Mockito.when(productRepository.save(product)).thenReturn(product);
        ProductRemoveResponse result = productService.removeProduct(1L);

        Assert.assertEquals(result.getProductId(), product.getId());

    }

    @Test
    public void testGetProductInfo() {
        Long id = 1L;

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        ProductInfoResponse result = productService.getProductInfo(id);

        Assert.assertEquals(result.getId(), id);

    }

    @Test
    public void testGetProductList() {

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Macbook Pro");
        product2.setCategory("Elec");
        product2.setPrice(BigDecimal.valueOf(300.00));
        product2.setStock(10);
        product2.setVersion(0L);

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> result = productService.getProductList();

        Assert.assertEquals(result.get(0).getId(), product.getId());

    }

    @Test
    public void testReduceStock(){
        List<BasketItem> basketItems = new ArrayList<BasketItem>();
        BasketItem firstBasketItem = new BasketItem();
        firstBasketItem.setId("1");
        basketItems.add(firstBasketItem);

        BasketItem secondBasketItem = new BasketItem();
        secondBasketItem.setId("2");
        basketItems.add(secondBasketItem);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Macbook Pro");
        product2.setCategory("Elec");
        product2.setPrice(BigDecimal.valueOf(300.00));
        product2.setStock(0);
        product2.setVersion(0L);

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        Mockito.when(messageSource.getMessage("stock.notfound", null, Locale.US)).thenReturn("Selected product/s are out of stock: ");

        Mockito.when(productRepository.saveAll(productList)).thenReturn(productList);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(product2));

        assertEquals(0, product.getVersion().intValue());
        assertEquals(0, product2.getVersion().intValue());

        ProductStockResponse result = productService.reduceStock(basketItems);

        Assert.assertEquals(result.getReturnCode(), "200");
        /* when stock reset */
        //Assert.assertEquals(result.getReturnCode(), "0");

    }

}