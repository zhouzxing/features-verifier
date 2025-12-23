package com.example.restservice.service;

import com.example.restservice.annotation.LogMonitor;

/**
 * @className: IAopService
 * @author: geeker
 * @date: 11/16/25 3:25 PM
 * @Version: 1.0
 * @description:
 */

public interface IAopWithMethodService {
    @LogMonitor(name = "save task")
    void save();
}
