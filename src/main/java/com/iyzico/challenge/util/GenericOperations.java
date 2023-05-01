package com.iyzico.challenge.util;

import com.iyzipay.model.BasketItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Component
public class GenericOperations {

    public String generatorNumber(){
        Random random = new Random();

        int num = random.ints().findFirst().getAsInt();
        String randomNumber = String.valueOf(Math.abs(num));

        return randomNumber;
    }

    public BigDecimal sumPriceCalculation(List<BasketItem> basketItems){
        BigDecimal sumPrice = basketItems.stream()
                .map(x -> x.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sumPrice;
    }

    public BigDecimal priceCalculation(BigDecimal sumPrice, BigDecimal installment){
        BigDecimal pricePayable = installment != BigDecimal.ZERO ?
                sumPrice.multiply(installment.add(BigDecimal.valueOf(0.2))) : sumPrice;

        return pricePayable;
    }
}
