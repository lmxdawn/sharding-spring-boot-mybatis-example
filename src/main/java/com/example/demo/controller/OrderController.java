package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/order")
@RestController
public class OrderController {
    
    @Resource
    private OrderService orderService;
    
    /**
     * 获取订单列表
     * @param uid
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/lists")
    public List<Order> lists(@RequestParam("uid") Long uid,
                             @RequestParam("page") Integer page,
                             @RequestParam("limit") Integer limit
    ) {
    
        List<Order> orders = orderService.listByUid(uid, page, limit);
        
        
        return orders;
    
    }
    
    /**
     * 查看订单详情
     * @param uid
     * @param orderId
     * @return
     */
    @GetMapping("/detail")
    public Order detail(@RequestParam("uid") Long uid,
                        @RequestParam("orderId") Long orderId) {
    
        Order byOrderIdAndUid = orderService.findByOrderIdAndUid(uid, orderId);
        
        return byOrderIdAndUid;
    
    }
    
    /**
     * 创建订单
     * @param uid
     * @return
     */
    @GetMapping("/create")
    public String create(@RequestParam("uid") Long uid) {
    
        Integer money = 100;
    
        Long orderId = (Long) new SnowflakeShardingKeyGenerator().generateKey();
    
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUid(uid);
        order.setMoney(money);
        order.setStatus(0);
    
        boolean b = orderService.insertOrder(order);
        
        return b ? "success" : "error";
    
    }
    
    
}
