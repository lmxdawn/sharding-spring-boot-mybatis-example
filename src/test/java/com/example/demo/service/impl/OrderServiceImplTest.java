package com.example.demo.service.impl;

import com.example.demo.BaseApplicationTest;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

public class OrderServiceImplTest extends BaseApplicationTest {
    
    @Resource
    private OrderService orderService;
    
    @Test
    public void listByUid() {
        
        Long uid = 1L;
        Integer page = 1;
        Integer limit = 4;
        List<Order> orders = orderService.listByUid(uid, page, limit);
        
        for (Order order : orders) {
            System.out.println(order);
        }
        
    }
    
    @Test
    public void findByOrderIdAndUid() {
        
        Long uid = 1L;
        Long orderId = 373243535220015105L;
        Order order = orderService.findByOrderIdAndUid(uid, orderId);
        
        System.out.println(order);
        
    }
    
    @Test
    public void insertOrder() {
        
        
        for (long i = 1; i <= 8; i++) {
            Long uid = i;
            Integer money = 11;
            
            Long orderId = (Long) new SnowflakeShardingKeyGenerator().generateKey();
            
            Order order = new Order();
            order.setOrderId(orderId);
            order.setUid(uid);
            order.setMoney(money);
            order.setStatus(0);
            
            boolean b = orderService.insertOrder(order);
            
            assertTrue(b);
        }
        
    }
    
    @Test
    public void updateOrder() {
        
        long count1 = 2; // 库的数量
        
        long count2 = 4; // 表的数量
        
        for (long i = 1; i <= 8; i++) {
            
            long s1 = i % count1;
            
            long s2 = i / count1 % count2;
            
            System.out.println("库: " + s1 + ", 表: " + s2);
            
        }
        
        // double bigDecimal = 1.0 / 2;
        //
        // int o = (int) (bigDecimal % 4);
        //
        // System.out.println(o);
        
    }
    
    @Test
    public void deleteByOrderId() {
    }
}