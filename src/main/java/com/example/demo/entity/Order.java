package com.example.demo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Order {
    
    private Long id;
    
    private Long orderId;
    
    private Long uid;
    
    private Integer money;
    
    private Integer status;
    
    private Date createTime;
    
    private Date modifiedTime;
    
}
