package com.atguigu.shardingjdbcdemo.mapper;

import com.atguigu.shardingjdbcdemo.model.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

}