package com.example.restservice.service;

import com.example.restservice.annotation.aspet.BigAmount;
import com.example.restservice.annotation.aspet.ProcessMethod;
import com.example.restservice.design_pattern.strategy.DesignPatterService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @className: TransactionService
 * @author: geeker
 * @date: 11/10/25 5:36 PM
 * @Version: 1.0
 * @description:
 */
@Service
public class TransactionService {

    @Autowired
    DesignPatterService designPatterService;

    @ProcessMethod(logged = false,retry = 3,type = "大额支付")
    @BigAmount(condition = "#amount>100")
    public void pay(String type, BigDecimal amount) {
        designPatterService.pay(type, amount);
    }

    @ProcessMethod(logged = false,retry = 3,type = "大额支付")
    @BigAmount(condition = "#amount>100")
    public void bigPay(BigDecimal amount) {
        designPatterService.pay("aliPay", amount);
    }

}
