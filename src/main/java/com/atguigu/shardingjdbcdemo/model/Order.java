package com.atguigu.shardingjdbcdemo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@TableName("t_order")
@Data
public class Order {
    @TableId(type = IdType.AUTO)
//    @TableId(type = IdType.ASSIGN_ID) // 1638355585655230466|1638355862030393346  分布式Id
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal amount;
}