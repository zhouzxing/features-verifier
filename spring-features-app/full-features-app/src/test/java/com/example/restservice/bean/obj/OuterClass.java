package com.example.restservice.bean.obj;

import org.junit.Test;

import java.io.Serializable;

/**
 * @className: OuterClass
 * @author: geeker
 * @date: 11/15/25 7:49 AM
 * @Version: 1.0
 * @description:
 */
// final 禁止继承
public class OuterClass {

    final private class PrivateInnerClass {

    }


    final class InnerClass {

    }

    class MultiInnerClass implements Serializable, Cloneable {

    }

    static class StaticInnerClass {

    }

    @Test
    public void test() {
        new InnerClass();
        new StaticInnerClass();

    }
}
