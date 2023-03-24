package com.atguigu.shardingjdbcdemo;

import com.atguigu.shardingjdbcdemo.mapper.DictMapper;
import com.atguigu.shardingjdbcdemo.mapper.OrderItemMapper;
import com.atguigu.shardingjdbcdemo.mapper.OrderMapper;
import com.atguigu.shardingjdbcdemo.mapper.UserMapper;
import com.atguigu.shardingjdbcdemo.model.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private DictMapper dictMapper;

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
    @Transactional // 是说明测试数据---- 保护数据; rollback; commit;
    //  @Rollback(value = false)
    @Test
    public void insertUser02(){
        //  创建一个用户对象
        User user = new User();
        user.setUname("li4");
        userMapper.insert(user);

        //  获取数据：
        List<User> userList = userMapper.selectList(null);

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

    /**
     * 插入数据
     */
    @Test
    public void testInsertOrder(){
        Order order = new Order();
        order.setUserId(1l);
        order.setAmount(new BigDecimal("100"));
        order.setOrderNo("atguigu01");
        orderMapper.insert(order);
    }

    /**
     * 水平分片：分库插入数据测试
     */
    @Test
    public void testInsertOrderDatabaseStrategy(){

        for (long i = 0; i < 4; i++) {
            Order order = new Order();
            order.setOrderNo("ATGUIGU001");
            order.setUserId(i + 1);
            order.setAmount(new BigDecimal(100));
            orderMapper.insert(order);
        }
    }

    /**
     * 水平分片：分表插入数据测试
     */
    @Test
    public void testInsertOrderTableStrategy(){

        for (long i = 1; i < 5; i++) {

            Order order = new Order();
            order.setOrderNo("ATGUIGU" + i); // 分表：1234
            order.setUserId(1L);    // 分库：service-order1
            order.setAmount(new BigDecimal(100));
            orderMapper.insert(order);
        }

        for (long i = 5; i < 9; i++) {

            Order order = new Order();
            order.setOrderNo("ATGUIGU" + i); // 分表：5678
            order.setUserId(2L);    //  分库：service-order0
            order.setAmount(new BigDecimal(100));
            orderMapper.insert(order);
        }
    }

    /**
     * 测试哈希取模
     */
    @Test
    public void testHash(){

        //注意hash取模的结果是整个字符串hash后再取模，和数值后缀是奇数还是偶数无关
        System.out.println("ATGUIGU001".hashCode() % 2);
        System.out.println("ATGUIGU0011".hashCode() % 2);
    }

    /**
     * 水平分片：查询所有记录
     * 查询了两个数据源，每个数据源中使用UNION ALL连接两个表
     */
    @Test
    public void testShardingSelectAll(){

        List<Order> orders = orderMapper.selectList(null);
        orders.forEach(System.out::println);
    }

    /**
     * 水平分片：根据user_id查询记录
     * 查询了一个数据源，每个数据源中使用UNION ALL连接两个表
     */
    @Test
    public void testShardingSelectByUserId(){

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id", 1L);
        List<Order> orders = orderMapper.selectList(orderQueryWrapper);
        orders.forEach(System.out::println);
    }


    /**
     * 测试关联表插入
     */
    @Test
    public void testInsertOrderAndOrderItem(){

        for (long i = 1; i < 3; i++) {

            Order order = new Order();
            order.setOrderNo("ATGUIGU" + i); // 分表字段
            order.setUserId(1L);    //  分库字段
            orderMapper.insert(order);

            for (long j = 1; j < 3; j++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderNo("ATGUIGU" + i);    // 分表字段
                orderItem.setUserId(1L);     //  分库字段
                orderItem.setPrice(new BigDecimal(10));
                orderItem.setCount(2);
                orderItemMapper.insert(orderItem);
            }
        }

        for (long i = 5; i < 7; i++) {

            Order order = new Order();
            order.setOrderNo("ATGUIGU" + i);
            order.setUserId(2L);
            orderMapper.insert(order);

            for (long j = 1; j < 3; j++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderNo("ATGUIGU" + i);
                orderItem.setUserId(2L);
                orderItem.setPrice(new BigDecimal(1));
                orderItem.setCount(3);
                orderItemMapper.insert(orderItem);
            }
        }
    }


    /**
     * 测试关联表查询
     */
    @Test
    public void testGetOrderAmount(){
        //  查询所有数据.
        List<OrderVo> orderAmountList = orderMapper.getOrderAmount();
        orderAmountList.forEach(System.out::println);
    }

    /**
     * 插入数据 多个节点
     */
    @Test
    public void testBroadcast(){

        Dict dict = new Dict();
        dict.setDictType("type1");
        dictMapper.insert(dict);
    }

    /**
     * 查询操作，只从一个节点获取数据
     * 随机负载均衡规则
     */
    @Test
    public void testSelectBroadcast(){

        List<Dict> dicts = dictMapper.selectList(null);
        dicts.forEach(System.out::println);
    }
}
