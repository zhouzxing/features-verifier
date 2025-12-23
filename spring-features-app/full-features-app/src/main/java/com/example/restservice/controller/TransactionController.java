package com.example.restservice.controller;

import com.example.restservice.design_pattern.strategy.DesignPatterService;
import com.example.restservice.design_pattern.strategy.PaymentService;
import com.example.restservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @className: PayController
 * @author: geeker
 * @date: 11/10/25 5:12 PM
 * @Version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private DesignPatterService designPatterService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/pay")
    public void pay(String type, BigDecimal amount) {
        //designPatterService.pay(type,amount);
        transactionService.pay(type,amount);
    }

    @GetMapping("/bigPay")
    public void pay(BigDecimal amount) {
        //designPatterService.pay(type,amount);
        transactionService.bigPay(amount);
    }


}
