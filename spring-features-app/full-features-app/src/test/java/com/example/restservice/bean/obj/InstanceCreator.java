package com.example.restservice.bean.obj;

import java.lang.reflect.Type;

/**
 * @className: InstanceCreator
 * @author: geeker
 * @date: 9/7/25 8:41 PM
 * @Version: 1.0
 * @description:
 */

public class InstanceCreator implements com.google.gson.InstanceCreator<ClassA.ClassB> {

    private final ClassA instance;

    public InstanceCreator(ClassA instance) {
        this.instance = instance;
    }

    @Override
    public ClassA.ClassB createInstance(Type type) {
        return instance.new ClassB();
    }
}
