package com.atguigu.shardingjdbcdemo.mapper;

import com.atguigu.shardingjdbcdemo.model.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}