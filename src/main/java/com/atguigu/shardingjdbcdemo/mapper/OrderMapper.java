package com.atguigu.shardingjdbcdemo.mapper;

import com.atguigu.shardingjdbcdemo.model.Order;
import com.atguigu.shardingjdbcdemo.model.OrderVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    //  mybatis: 可以使用xml 编写sql 语句;
    //  @Select("select * from t_order0")  @Delete()
    @Select({"SELECT o.order_no, SUM(i.price * i.count) AS amount",
            "FROM t_order o JOIN t_order_item i ON o.order_no = i.order_no",
            "GROUP BY o.order_no"})
    List<OrderVo> getOrderAmount();


}