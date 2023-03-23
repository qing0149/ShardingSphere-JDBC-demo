package com.atguigu.shardingjdbcdemo.mapper;

import com.atguigu.shardingjdbcdemo.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}