
package com.example.restservice.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @className: SingletonService
 * @author: geeker
 * @date: 11/16/25 4:14 PM
 * @Version: 1.0
 * @description:
 */

@Service
@Scope("prototype") // 默认，可以省略
public class PrototypeService {
    private static int instanceCount = 0;
    private final int instanceId;

    public PrototypeService() {
        instanceCount++;
        this.instanceId = instanceCount;
        System.out.println("PrototypeService 构造函数被调用，实例ID: " + instanceId);
    }

    public String getInstanceInfo() {
        return String.format("PrototypeService [实例ID: %d, 总实例数: %d]", instanceId, instanceCount);
    }
}