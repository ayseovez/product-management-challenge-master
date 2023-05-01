package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.payload.request.ProductAddRequest;
import com.iyzico.challenge.payload.request.ProductUpdateRequest;
import com.iyzico.challenge.payload.response.ProductAddResponse;
import com.iyzico.challenge.payload.response.ProductInfoResponse;
import com.iyzico.challenge.payload.response.ProductRemoveResponse;
import com.iyzico.challenge.payload.response.ProductUpdateResponse;
import com.iyzico.challenge.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String endpoint = "http://localhost:8080/api/iyzico";

    @Test
    public void addProduct() throws Exception {

        ProductAddResponse response = new ProductAddResponse();
        response.setName("Airpods");
        response.setReturnCode("200");
        response.setReturnMessage("Airpods" +" the product has been registered");

        when(productService.addProduct(any(ProductAddRequest.class))).thenReturn(response);

        mockMvc.perform(post(endpoint+"/addProduct")
                .content(mapper.writeValueAsString(response))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Airpods")))
                .andExpect(jsonPath("$.returnCode", is("200")))
                .andExpect(jsonPath("$.returnMessage", is(response.getName() +" the product has been registered")));

        verify(productService, times(1)).addProduct(any(ProductAddRequest.class));
    }

    @Test
    public void updateProduct() throws Exception {
        ProductUpdateResponse response = new ProductUpdateResponse();
        response.setName("Airpods");
        response.setStock(10);
        response.setPrice(new BigDecimal("140.00"));
        response.setReturnCode("200");
        response.setReturnMessage("Airpods" +" the product has been updated. New value: "+response.getStock()+" "+response.getPrice());

        when(productService.updateProduct(any(Long.class), any(ProductUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put(endpoint+"/updateProduct/1")
                .content(mapper.writeValueAsString(response))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Airpods")))
                .andExpect(jsonPath("$.stock", is(10)))
                .andExpect(jsonPath("$.price", is(140.00)))
                .andExpect(jsonPath("$.returnCode", is("200")))
                .andExpect(jsonPath("$.returnMessage", is(response.getName() +" the product has been updated. New value: "+response.getStock()+" "+response.getPrice())));

        verify(productService, times(1)).updateProduct(any(Long.class), any(ProductUpdateRequest.class));

    }

    @Test
    public void removeProduct() throws Exception {
        ProductRemoveResponse response = new ProductRemoveResponse();
        response.setProductId(1L);
        response.setReturnCode("200");
        response.setReturnMessage("1" +" the product has been removed");

        when(productService.removeProduct(any(Long.class))).thenReturn(response);

        mockMvc.perform(delete(endpoint+"/removeProduct/1")
                .content(mapper.writeValueAsString(response))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.returnCode", is("200")))
                .andExpect(jsonPath("$.returnMessage", is("1" +" the product has been removed")));

        verify(productService, times(1)).removeProduct(any(Long.class));

    }

    @Test
    public void getProduct() throws Exception {
        //Product product = new Product(1L, "Airpods", "Electronics", new BigDecimal("140.00"), 10);
        //when(productService.getProductInfo(1L)).thenReturn(product);
        ProductInfoResponse response = new ProductInfoResponse();
        response.setId(1L);
        response.setName("Airpods");
        response.setDescription("Electronics");
        response.setPrice(new BigDecimal("140.00"));
        response.setStock(10);
        response.setReturnCode("200");
        response.setReturnMessage("Success");

        when(productService.getProductInfo(1L)).thenReturn(response);

        mockMvc.perform(get(endpoint+"/getProduct/1")
                .content(mapper.writeValueAsString(response))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Airpods")))
                .andExpect(jsonPath("$.description", is("Electronics")))
                .andExpect(jsonPath("$.price", is(140.00)))
                .andExpect(jsonPath("$.stock", is(10)))
                .andExpect(jsonPath("$.returnCode", is("200")))
                .andExpect(jsonPath("$.returnMessage", is("Success")));

        verify(productService, times(1)).getProductInfo(1L);
    }

    @Test
    public void getProductList() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(1L, "Airpods", "Electronics", new BigDecimal("140.00"), 10),
                new Product(2L, "Airpods Pro", "Electronics", new BigDecimal("150.00"), 10));

        when(productService.getProductList()).thenReturn(products);

        mockMvc.perform(get(endpoint+"/getProductList"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Airpods")))
                .andExpect(jsonPath("$[0].description", is("Electronics")))
                .andExpect(jsonPath("$[0].price", is(140.00)))
                .andExpect(jsonPath("$[0].stock", is(10)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Airpods Pro")))
                .andExpect(jsonPath("$[1].description", is("Electronics")))
                .andExpect(jsonPath("$[1].price", is(150.00)))
                .andExpect(jsonPath("$[1].stock", is(10)));

        verify(productService, times(1)).getProductList();
    }
}