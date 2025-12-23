package com.example.restservice.bean.obj;

/**
 * @className: InterfaceClass
 * @author: geeker
 * @date: 11/15/25 8:00 AM
 * @Version: 1.0
 * @description:
 */

public class InterfaceClass implements InterfaceViaiable {
    public static void main(String[] args) {
        InterfaceClass interfaceClass = new InterfaceClass();
        System.out.println(interfaceClass.k);
        System.out.println(InterfaceClass.k);
        System.out.println(InterfaceViaiable.k);

        //interfaceClass.k = 10;

    }
}
