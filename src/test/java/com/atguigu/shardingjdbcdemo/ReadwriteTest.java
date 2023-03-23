package com.atguigu.shardingjdbcdemo;

import com.atguigu.shardingjdbcdemo.mapper.OrderMapper;
import com.atguigu.shardingjdbcdemo.mapper.UserMapper;
import com.atguigu.shardingjdbcdemo.model.Order;
import com.atguigu.shardingjdbcdemo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class ReadwriteTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 写入数据. master===3306; 同步到 slave1; slave2;
     */
    @Test
    public void insertUser01(){
        //  创建一个用户对象
        User user = new User();
        user.setUname("admin");
        userMapper.insert(user);
    }

    /**
     * 没有添加事务：写入应该在master; 读取应该在slave
     * 有了事务新增，查询都是操作的主库; master;
     */
    @Transactional
    @Test
    public void insertUser02(){
        //  创建一个用户对象
//        User user = new User();
//        user.setUname("li4");
//        userMapper.insert(user);

        //  获取数据：
        List<User> userList = userMapper.selectList(null);
        System.out.println(userList);

    }

    /**
     * 查询测试：slave 从库
     */
    @Test
    public void selectList(){
        //  获取数据：
        List<User> userList01 = userMapper.selectList(null);
        List<User> userList02 = userMapper.selectList(null);
    }
    @Test
    public void selectAll(){
        for (int i = 0; i < 5; i++) {
            System.out.println(i+":"+userMapper.selectList(null));
        }
    }

    /**
     * 插入数据
     */
    @Test
    @Transactional
    public void testInsertOrderAndUser(){
        //  插入用户数据
        User user = new User();
        user.setUname("atguigu");
        userMapper.insert(user);

        //  插入order 数据
        Order order = new Order();
        order.setUserId(1L);
        order.setOrderNo(UUID.randomUUID().toString().substring(0,20));
        order.setAmount(new BigDecimal("100"));
        orderMapper.insert(order);

    }

    /**
     * 获取数据
     */
    @Test
    public void selectOrderAndUser(){
        //  从db_user.t_user 表中获取数据.
        List<User> userList = userMapper.selectList(null);
        //  从db_order.t_order 表中获取数据.
        List<Order> orderList = orderMapper.selectList(null);
    }

}
