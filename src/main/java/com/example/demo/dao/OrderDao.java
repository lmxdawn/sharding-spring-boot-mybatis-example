package com.example.demo.dao;

import com.example.demo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDao {
    
    // 根据用户id查询列表
    List<Order> listByUid(@Param(value = "uid") Long uid,
                          @Param("offset") Integer offset,
                          @Param("limit") Integer limit);
    
    // 根据 order_id 和 uid 查询
    Order findByOrderIdAndUid(@Param(value = "uid") Long uid,
                              @Param("orderId") Long orderId);
    
    
    // 插入
    boolean insertOrder(Order order);
    
    
    // 更新
    boolean updateOrder(Order order);
    
    
    // 删除
    boolean deleteByOrderId(Long orderId);
    
    
}
