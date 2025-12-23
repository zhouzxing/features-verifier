package com.example.restservice.service.impl;

import com.example.restservice.annotation.LogMonitor;
import com.example.restservice.service.IAopWithNoMethodService;
import org.springframework.stereotype.Service;

/**
 * @className: AopServiceImp
 * @author: geeker
 * @date: 11/16/25 3:25 PM
 * @Version: 1.0
 * @description:
 */
@Service
public class AopWithNoMethodServiceImp implements IAopWithNoMethodService {

    @Override
    @LogMonitor(name = "Another save")
    public void save() {

    }
}
