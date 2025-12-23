package com.example.restservice.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.restservice.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.executor.BatchResult;

import java.util.List;

/**
 * @className: UserMapper
 * @author: geeker
 * @date: 11/17/25 3:25 PM
 * @Version: 1.0
 * @description:
 */
//@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Insert({
            "<script>",
            "INSERT INTO sys_user (id,name,age,email) VALUES ",
            "<foreach collection='users' item='user' separator=','>",
            "(#{user.id}, #{user.name}, #{user.age}, #{user.email})",
            "</foreach>",
            "ON DUPLICATE KEY UPDATE name=VALUES(name), age=values(age),email='aa@aa.com'",
            "</script>"
    })
    Integer insertOrUpdateAdv(List<User> users);

    Integer insertOrUpdatePrimaryKey(List<User> users);
    Integer insertOrUpdateUniqueKey(List<User> users);
}
