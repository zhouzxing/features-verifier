package com.example.restservice.design_pattern.strategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: PayConfig
 * @author: geeker
 * @date: 11/10/25 11:54 AM
 * @Version: 1.0
 * @description:
 */
@Configuration
//@Configurable
public class PayConfig {

    @Bean
    public Map<String, PaymentService> paymentServiceMap(AliPay aliPay, WechatPay wechatPay, BankPay bankPay) {
        return Map.of(
                "AliPay", aliPay,
                "WechatPay", wechatPay,
                "BankPay", bankPay
        );
    }

    @Bean
    public Map<String, PaymentService> paymentServiceMapAnnotation(List<PaymentService> paymentServiceList) {
        Map paymentServiceMapAnnotation = new HashMap<>(paymentServiceList.size());
        for (PaymentService paymentService : paymentServiceList) {
            PaymentType annotation = paymentService.getClass().getAnnotation(PaymentType.class);
            String name = paymentService.getClass().getSimpleName();
            paymentServiceMapAnnotation.put(annotation!=null? annotation.value():name, paymentService);
        }
        return paymentServiceMapAnnotation;
    }


}
