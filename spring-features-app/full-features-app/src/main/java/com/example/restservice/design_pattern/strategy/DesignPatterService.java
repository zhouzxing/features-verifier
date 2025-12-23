package com.example.restservice.design_pattern.strategy;

import com.example.restservice.annotation.aspet.ProcessMethod;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Map;

/**
 * @className: design_pattern
 * @author: geeker
 * @date: 11/10/25 9:36 AM
 * @Version: 1.0
 * @description:
 */
@Service
public class DesignPatterService {

//    @Autowired
//    private AliPay aliPay;
//    @Autowired
//    private BankPay bankPay;
//    @Autowired
//    private WechatPay wechatPay;

//    @Autowired
//    private Map<String,PaymentService> paymentService = Map.of
//            ("AliPay",aliPay,"BankPay",bankPay,"WechatPay",wechatPay);

    // Spring 自动注入
//    @Autowired
//    private Map<String, PaymentService> paymentService;
//    @Autowired
    private Map<String,PaymentService> paymentServiceMap;

//    @Autowired
//    public design_pattern(AliPay aliPay, WechatPay wechatPay, BankPay bankPay) {
//        paymentServiceMap = Map.of(
//                "AliPay",aliPay,
//                "WechatPay",wechatPay,
//                "BankPay",bankPay
//        );
//    }

//    @Autowired
    private Map<String,PaymentService> paymentServiceMapAnnotation;
    // 自定义注解
//    @Autowired
//    public design_pattern1(List<PaymentService> paymentServiceList) {
//        paymentServiceMapAnnotation = new HashMap<>(paymentServiceList.size());
//        for(PaymentService paymentService : paymentServiceList) {
//            PaymentType annotation = paymentService.getClass().getAnnotation(PaymentType.class);
//            if (annotation != null) {
//                String key = (String) annotation.value();
//                paymentServiceMapAnnotation.put(key,paymentService);
//            }
//        }
//    }

//    @Autowired
//    public design_pattern(List<PaymentService> paymentServiceList) {
//        paymentServiceMapAnnotation = new HashMap<>(paymentServiceList.size());
//        for (PaymentService paymentService : paymentServiceList) {
//            PaymentType annotation = paymentService.getClass().getAnnotation(PaymentType.class);
//            String name = paymentService.getClass().getSimpleName();
//            paymentServiceMapAnnotation.put(annotation!=null? annotation.value():name, paymentService);
//            paymentServiceMap.put(name,paymentService);
//        }
//    }

    public DesignPatterService(Map<String,PaymentService> paymentServiceMap, Map<String,PaymentService> paymentServiceMapAnnotation) {
        this.paymentServiceMap = paymentServiceMap;
        this.paymentServiceMapAnnotation = paymentServiceMapAnnotation;
    }

    @PostConstruct
    public void test() {
//        System.out.println("PostConstruct" + paymentServiceMap);
//        System.out.println("PostConstruct" + paymentServiceMapAnnotation);
    }

    @ProcessMethod(type = "支付处理",retry = 5,logged = true)
    public void pay(String type, BigDecimal amount) {
        // type 类型
        PaymentService paymentService = paymentServiceMap.get(type);
        if (paymentService == null) {
            throw new IllegalArgumentException("不支持支付方式: "+type);
        }
        paymentService.pay(amount);
    }


    @ProcessMethod(type = "退款处理",retry = 3,logged = true)
    public void refund(String type, BigDecimal amount) {
        // type 类型
        PaymentService paymentService = paymentServiceMap.get(type);
        if (paymentService == null) {
            throw new IllegalArgumentException("不支持支付方式: "+type);
        }
        // paymentService.pay(amount);
    }

    @ProcessMethod(type = "加密处理", logged=false)
    public String encode(String msg) {
        return Base64.getEncoder().encodeToString(msg.getBytes());
    }


}
