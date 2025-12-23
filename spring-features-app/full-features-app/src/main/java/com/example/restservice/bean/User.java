package com.example.restservice.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @className: User
 * @author: geeker
 * @date: 10/16/25 3:00 PM
 * @Version: 1.0
 * @description:
 */
@Data
@TableName("sys_user")
@Accessors(chain = true)
public class User {

    private Long id;
    private String name;
    private Integer age;
    private String email;
}
