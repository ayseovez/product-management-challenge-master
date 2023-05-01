package com.iyzico.challenge.repository;

import com.iyzico.challenge.entity.ProductPay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPaymentRepository extends JpaRepository<ProductPay, Long> {
}
