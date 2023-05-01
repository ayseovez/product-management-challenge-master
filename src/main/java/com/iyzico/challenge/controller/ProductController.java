package com.iyzico.challenge.controller;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.payload.request.ProductAddRequest;
import com.iyzico.challenge.payload.request.ProductUpdateRequest;
import com.iyzico.challenge.payload.response.ProductAddResponse;
import com.iyzico.challenge.payload.response.ProductInfoResponse;
import com.iyzico.challenge.payload.response.ProductRemoveResponse;
import com.iyzico.challenge.payload.response.ProductUpdateResponse;
import com.iyzico.challenge.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/iyzico")
public class ProductController {

    private final ProductService productService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addProduct")
    public ProductAddResponse addProduct(@RequestBody ProductAddRequest request){
        ProductAddResponse response = productService.addProduct(request);
        return response;
    }

    @PutMapping ("/updateProduct/{id}")
    public ProductUpdateResponse updateProduct(@PathVariable(value = "id") Long productId, @RequestBody ProductUpdateRequest request){
        ProductUpdateResponse response = productService.updateProduct(productId, request);
        return response;
    }

    @DeleteMapping("/removeProduct/{id}")
    public ProductRemoveResponse removeProduct(@PathVariable(value = "id") Long productId){
        ProductRemoveResponse response = productService.removeProduct(productId);
        return response;
    }

    @GetMapping("/getProduct/{productId}")
    public ProductInfoResponse getProduct(@PathVariable Long productId){
        ProductInfoResponse response = productService.getProductInfo(productId);
        return response;
    }

    @GetMapping("/getProductList")
    public List<Product> getProductList(){
        return productService.getProductList();
    }
}
