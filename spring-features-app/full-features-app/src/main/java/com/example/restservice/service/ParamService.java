package com.example.restservice.service;

import com.example.restservice.annotation.aspet.ParamDeal;
import com.example.restservice.bean.User;
import org.springframework.stereotype.Service;

/**
 * @className: ParamService
 * @author: geeker
 * @date: 12/22/25 3:11 PM
 * @Version: 1.0
 * @description:
 */
@Service
public class ParamService {

    @ParamDeal()
    public String param(String name, User user) {
        return user.getName();
    }
}
