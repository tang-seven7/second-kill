package com.seven.seckill.listener;

import com.alibaba.fastjson.JSON;
import com.seven.seckill.entities.pojo.Order;
import com.seven.seckill.entities.vo.GoodsOrderVo;
import com.seven.seckill.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import static com.seven.seckill.config.RabbitMqConfig.QUEUE_ORDER;

@Slf4j
@Component
public class OrderListener {
    @Resource
    private OrderService orderService;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @RabbitListener(queues = QUEUE_ORDER)
    public void receiveOrderMsg(Message message){
        log.info("订单队列接收到消息："+new String(message.getBody()));
        GoodsOrderVo goodsOrderVo = JSON.parseObject(new String(message.getBody()), GoodsOrderVo.class);
        //创建商品订单
        Order order = new Order();
        BeanUtils.copyProperties(goodsOrderVo,order);
        order.setGoodsCount(1);
        order.setTotalPrice(goodsOrderVo.getGoodsPrice());
        boolean result = orderService.save(order);
        if (result){
            //将订单缓存
            redisTemplate.opsForValue().
                    set("SeckillOrder:"+goodsOrderVo.getGoodsId()+"-"+goodsOrderVo.getUserId(),order);
        }

    }
}
