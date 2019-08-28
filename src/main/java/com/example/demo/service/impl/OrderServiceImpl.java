package com.example.demo.service.impl;

import com.example.demo.dao.OrderDao;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import com.example.demo.utils.PageUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Resource
    private OrderDao orderDao;
    
    @Override
    public List<Order> listByUid(Long uid, Integer page, Integer limit) {
        Integer offset = PageUtils.createOffset(page, limit);
        
        return orderDao.listByUid(uid, offset, limit);
    }
    
    @Override
    public Order findByOrderIdAndUid(Long uid, Long orderId) {
        return orderDao.findByOrderIdAndUid(uid, orderId);
    }
    
    @Override
    public boolean insertOrder(Order order) {
        
        order.setCreateTime(new Date());
        order.setModifiedTime(new Date());
        
        return orderDao.insertOrder(order);
    }
    
    @Override
    public boolean updateOrder(Order order) {
        order.setModifiedTime(new Date());
        return orderDao.updateOrder(order);
    }
    
    @Override
    public boolean deleteByOrderId(Long orderId) {
        return orderDao.deleteByOrderId(orderId);
    }
}
