package com.atguigu.shardingjdbcdemo.controller;

import com.atguigu.shardingjdbcdemo.mapper.UserMapper;
import com.atguigu.shardingjdbcdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userController")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("selectAll")
    public List<User> selectAll(){
        return userMapper.selectList(null);
    }
}
