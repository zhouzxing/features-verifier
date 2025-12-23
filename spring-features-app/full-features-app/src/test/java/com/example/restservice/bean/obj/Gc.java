package com.example.restservice.bean.obj;

import org.junit.Test;

/**
 * @className: Gc
 * @author: geeker
 * @date: 11/15/25 8:17 AM
 * @Version: 1.0
 * @description:
 */

public class Gc {
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("App call finalize");
    }

    @Test
    public void test() throws Throwable {
        Gc gc = new Gc();
        gc.finalize();
    }
}
