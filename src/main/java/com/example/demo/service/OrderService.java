package com.example.demo.service;

import com.example.demo.entity.Order;

import java.util.List;

public interface OrderService {
    
    // 根据用户id查询列表
    List<Order> listByUid(Long uid, Integer page, Integer limit);
    
    // 根据 order_id 和 uid 查询
    Order findByOrderIdAndUid(Long uid, Long orderId);
    
    // 插入
    boolean insertOrder(Order order);
    
    
    // 更新
    boolean updateOrder(Order order);
    
    
    // 删除
    boolean deleteByOrderId(Long orderId);

}
