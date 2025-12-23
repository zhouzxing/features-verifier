package com.example.restservice.design_pattern.strategy;

import java.math.BigDecimal;

/**
 * @className: PaymentService
 * @author: geeker
 * @date: 11/10/25 9:36 AM
 * @Version: 1.0
 * @description:
 */

public interface PaymentService {
    void pay(BigDecimal amount);
}
