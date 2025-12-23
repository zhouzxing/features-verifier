package com.example.restservice.design_pattern.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @className: AliPay
 * @author: geeker
 * @date: 11/10/25 9:37 AM
 * @Version: 1.0
 * @description:
 */
@Component
@PaymentType("AliPay")
public class AliPay implements PaymentService {
    @Override
    public void pay(BigDecimal amount) {
        System.out.println("Ali Pay");
    }
}
