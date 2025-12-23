package com.example.restservice.bean.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @className: RequestBaseBody
 * @author: geeker
 * @date: 11/17/25 1:24 PM
 * @Version: 1.0
 * @description:
 */
@Data
@Accessors(chain = true)
public class RequestBaseBody {
    int intValue;
    String stringValue;
    float floatValue;
    double doubleValue;
    BigDecimal bigDecimalValue;
    boolean booleanValue;
    Date dateValue;

    Object[] array;
    List<Object> list;
    Map<Object,Object> map;

}
