package com.seven.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seven.seckill.entities.pojo.Order;
import com.seven.seckill.service.OrderService;
import com.seven.seckill.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author User
* @description 针对表【t_order】的数据库操作Service实现
* @createDate 2023-03-17 16:03:35
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




