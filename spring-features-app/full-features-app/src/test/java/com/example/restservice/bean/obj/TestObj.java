package com.example.restservice.bean.obj;

import org.junit.Test;

/**
 * @className: Test
 * @author: geeker
 * @date: 11/15/25 7:50 AM
 * @Version: 1.0
 * @description:
 */

public class TestObj {

    @Test
    public void test() {
        OuterClass outerClass = new OuterClass();
        OuterClass.InnerClass innerClass = outerClass.new InnerClass();

        OuterClass.StaticInnerClass staticInnerClass = new OuterClass.StaticInnerClass();
    }

    class TypeConvert {
        public void test(int i){
            System.out.println("int i:\t" + i);
        }
        public void test(String i){
            System.out.println("String i:\t" + i);
        }
    }


    @Test
    public void testTypeConvert() {
        TypeConvert typeConvert = new TypeConvert();
        char c = 'a';
        byte b = (byte) c;
        int i = b;
        i = c;

        long l = b;
        double d = l;
        float f = (float) d;
        f = l;


        Character character = 'A';
        typeConvert.test(c);
        typeConvert.test(character);


    }


}
