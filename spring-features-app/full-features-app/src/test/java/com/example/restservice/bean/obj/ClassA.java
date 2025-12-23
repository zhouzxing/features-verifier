package com.example.restservice.bean.obj;

import lombok.Data;

/**
 * @className: ClassA
 * @author: geeker
 * @date: 9/7/25 8:31 PM
 * @Version: 1.0
 * @description:
 */
@Data
public class ClassA {
    private String a = "ClassA";
    private ClassB classB = new ClassB();

    @Data
    class ClassB {
        private String b = "ClassB";
    }
}
