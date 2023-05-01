package com.iyzico.challenge.service.impl;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.ProductException;
import com.iyzico.challenge.payload.request.ProductAddRequest;
import com.iyzico.challenge.payload.request.ProductUpdateRequest;
import com.iyzico.challenge.payload.response.*;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.ProductService;
import com.iyzipay.model.BasketItem;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final Logger LOGGER = LogManager.getLogger(ProductServiceImpl.class);


    private final ProductRepository productRepository;

    private final MessageSource messageSource;

    @Override
    public ProductAddResponse addProduct(ProductAddRequest request) {
        productRepository.findByName(request.getName()).ifPresent(s -> {
            throw new ProductException("Product has already been registered. Please try upgrade Product's Stock");
        });
        ProductAddResponse response = new ProductAddResponse();
        Product product = new Product();
        try {
            product.setName(request.getName());
            product.setCategory(request.getDescription());
            product.setPrice(request.getPrice());
            product.setStock(request.getStock());
            product = productRepository.save(product);
        }catch (Exception e){
            response.setReturnCode("0");
            response.setReturnMessage(messageSource.getMessage("product.generic.exception", null, Locale.US));
            LOGGER.error(messageSource.getMessage("product.generic.exception", null, Locale.US)+e.getMessage());
            return response;
        }

        response.setName(product.getName());
        response.setReturnCode("200");
        response.setReturnMessage(response.getName() +" the product has been registered");
        LOGGER.info(response.getName() +" the product has been registered");
        return response;
    }

    @Override
    @Transactional
    public ProductUpdateResponse updateProduct(Long productId, ProductUpdateRequest request) {
        Product product = validateProduct(productId);
        ProductUpdateResponse response = new ProductUpdateResponse();
        try {
            product.setPrice(request.getPrice() != null ? request.getPrice() : product.getPrice());
            product.setStock(request.getStock() != null ? request.getStock() : product.getStock());
            product = productRepository.save(product);
        }catch (Exception e){
            response.setReturnCode("0");
            response.setReturnMessage(messageSource.getMessage("product.generic.exception", null, Locale.US));
            LOGGER.error(messageSource.getMessage("product.generic.exception", null, Locale.US)+e.getMessage());
            return response;
        }
        response.setName(product.getName());
        response.setStock(request.getStock() != null ? product.getStock() : null);
        response.setPrice(request.getPrice() != null ? product.getPrice() : null);
        response.setReturnCode("200");
        response.setReturnMessage(response.getName() +" the product has been updated. New value(s): "+response.getStock()+", "+response.getPrice());
        LOGGER.info(response.getName() +" the product has been updated. ");
        return response;
    }

    @Override
    public ProductRemoveResponse removeProduct(Long productId) {
        Product product = validateProduct(productId);
        ProductRemoveResponse response = new ProductRemoveResponse();
        try {
            productRepository.delete(product);
        }catch (Exception e){
            response.setReturnCode("0");
            response.setReturnMessage(messageSource.getMessage("product.generic.exception", null, Locale.US));
            LOGGER.error("An error has occurred in the system. "+e.getMessage());
            return response;
        }
        response.setProductId(productId);
        response.setReturnCode("200");
        response.setReturnMessage(response.getProductId() +" the product has been removed");
        LOGGER.info(response.getProductId() +" the product has been removed");
        return response;
    }

    @Override
    public ProductInfoResponse getProductInfo(Long productId) {
        Product product = validateProduct(productId);
        ProductInfoResponse response = new ProductInfoResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getCategory());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setReturnCode("200");
        response.setReturnMessage("Success");
        return response;
    }

    @Override
    public List<Product> getProductList() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    @Transactional
    public ProductStockResponse reduceStock(List<BasketItem> items) {
        ProductStockResponse response = new ProductStockResponse();
        List<Product> products = new ArrayList<>();
        String noneStockproducts;

        try {
            items.stream().forEach(s ->
                    products.add(validateProduct(Long.valueOf(s.getId()))));

            noneStockproducts = products.stream().filter(stock -> stock.getStock() == 0)
                    .map(name -> name.getName()).distinct()
                    .collect(Collectors.joining (","));

            if(!noneStockproducts.isEmpty()){
                response.setReturnCode("0");
                response.setReturnMessage(messageSource.getMessage("stock.notfound", null, Locale.US) +noneStockproducts);
                LOGGER.warn(messageSource.getMessage("stock.notfound", null, Locale.US) +noneStockproducts);
                return response;
            }

            products.stream().forEach(p -> p.setStock(p.getStock() - 1));
            productRepository.saveAll(products);
        }catch (OptimisticLockException e) {
            response.setReturnCode("0");
            response.setReturnMessage(messageSource.getMessage("stock.check", null, Locale.US));
            LOGGER.error(messageSource.getMessage("stock.check", null, Locale.US));
            return response;
        } catch (Exception e) {
            response.setReturnCode("0");
            response.setReturnMessage(messageSource.getMessage("product.generic.exception", null, Locale.US));
            LOGGER.error(messageSource.getMessage("product.generic.exception", null, Locale.US)+e.getMessage());
            return response;
        }
        response.setReturnCode("200");
        LOGGER.info("Stocks has been reduced. ");
        return response;
    }

    private Product validateProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found by id: "+id));
        return product;
    }


}
