package com.example.restservice.bean.obj;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

/**
 * @className: GsonTest
 * @author: geeker
 * @date: 9/7/25 8:32 PM
 * @Version: 1.0
 * @description:
 */

public class GsonTest {

    @Test
    public void test() {
        Gson gson = new Gson();
        ClassA classA = new ClassA();
        ClassA.ClassB classB = classA.new ClassB();


        System.out.println(gson.toJson(classA));
        System.out.println(gson.toJson(classB));

        ClassA classA1 = gson.fromJson(gson.toJson(classA), ClassA.class);
        System.out.println(classA1);

        ClassA.ClassB classB1 = gson.fromJson(gson.toJson(classB), ClassA.ClassB.class);
        System.out.println(classB1);

        ClassA.ClassB classB2 = gson.fromJson("{\"b\":\"ClassB\"}", ClassA.ClassB.class);
        System.out.println(classB2);

    }
}
