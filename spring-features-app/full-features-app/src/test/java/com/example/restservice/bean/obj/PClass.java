package com.example.restservice.bean.obj;

/**
 * @className: PClass
 * @author: geeker
 * @date: 11/16/25 12:12 PM
 * @Version: 1.0
 * @description:
 */

public class PClass {
    private float x = 1.0f;

    private float getX() {
        return x;
    }
}

class SubPClass extends PClass {
    private float x = 1.0f;
    private float getX() {
        return x;
    }
}

class Main {

    public static void main(String[] args) {
        SubPClass subPClass = new SubPClass();

    }
}
