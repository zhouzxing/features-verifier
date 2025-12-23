package com.example.restservice.service.impl;

import com.example.restservice.service.ResourceService;
import org.springframework.stereotype.Service;

/**
 * @className: ResourceServiceImp
 * @author: geeker
 * @date: 12/18/25 9:58 AM
 * @Version: 1.0
 * @description:
 */
@Service
public class ResourceServiceImp implements ResourceService {
    @Override
    public String resource() {
        return "resource";
    }
}
