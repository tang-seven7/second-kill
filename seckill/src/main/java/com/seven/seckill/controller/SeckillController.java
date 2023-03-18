package com.seven.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seven.seckill.entities.pojo.Goods;
import com.seven.seckill.entities.pojo.Order;
import com.seven.seckill.entities.pojo.SeckillGoods;
import com.seven.seckill.entities.vo.GoodsOrderVo;
import com.seven.seckill.entities.vo.ResponseEnum;
import com.seven.seckill.entities.vo.ResponseResult;
import com.seven.seckill.exception.GlobalException;
import com.seven.seckill.service.GoodsService;
import com.seven.seckill.service.SeckillGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import static com.seven.seckill.config.RabbitMqConfig.EXCHANGE_ORDER;

@RestController
@Slf4j
public class SeckillController{
    @Resource
    private GoodsService goodsService;
    @Resource
    private SeckillGoodsService seckillGoodsService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private RedissonClient redisson;

    //秒杀下单
    @PostMapping("/order")
    public ResponseResult<?> secondKillOrder(@RequestBody GoodsOrderVo goodsOrderVo, HttpServletRequest request){
//        log.info("查看数据库库存");
//        SeckillGoods seckillGoods = seckillGoodsService.getById(goodsOrderVo.getGoodsId());
//        if (seckillGoods.getStockCount()<1){
//            //throw new GlobalException(ResponseEnum.NULL_ERROR);
//            return new ResponseResult<>(ResponseEnum.NULL_ERROR);
//        }
//        //查看用户是否已有订单，进行限量购买
//        Order order = orderService.
//                getOne(new QueryWrapper<Order>().eq("user_id", goodsOrderVo.getUserId())
//                        .eq("goods_id", goodsOrderVo.getGoodsId()));
//        if (order!=null){
//            //throw new GlobalException(ResponseEnum.REPEAT_ERROR);
//            return new ResponseResult<>(ResponseEnum.REPEAT_ERROR);
//        }
//        为避免频繁的数据库访问，已弃用

        //限流：计数器算法，基于redis实现
        //下方代码对单个用户进行限流，单个用户每s仅可访问5次
        String url = String.valueOf(request.getRequestURL());
        Integer flag = (Integer) redisTemplate.opsForValue().get(url+":"+goodsOrderVo.getUserId());
        if (flag==null){
            //设置计数器，5秒过期
            redisTemplate.opsForValue().set(url+":"+goodsOrderVo.getUserId(),1, 1, TimeUnit.SECONDS);
        }else if (flag < 10){
            //请求数+1
            redisTemplate.opsForValue().increment(url+":"+goodsOrderVo.getUserId());
        }else {
            //限流
            log.warn("请求过于频繁");
            throw new GlobalException(ResponseEnum.SYSTEM_ERROR);
        }

        //从redis取缓存，限制用户购买量
        Order order = (Order) redisTemplate.opsForValue().
                get("SeckillOrder:"+goodsOrderVo.getGoodsId()+"-"+goodsOrderVo.getUserId());
        if (order!=null){
            throw new GlobalException(ResponseEnum.REPEAT_ERROR);
        }

        //获取redission分布式锁
        String lockKey = UUID.randomUUID().toString();
        RLock lock = redisson.getLock(lockKey);
        try {
            //开启锁
            lock.lock();

            synchronized(this){
                //redis预减库存
                if(redisTemplate.opsForValue().get(("seckillGoods:"+ goodsOrderVo.getGoodsId()))==null){
                    return new ResponseResult<>(ResponseEnum.NULL_ERROR);
                }else {
                    int count = (int)redisTemplate.opsForValue().get(("seckillGoods:"+ goodsOrderVo.getGoodsId()));
                    if (count<1){
                        log.info("库存不足");
                        return new ResponseResult<>(ResponseEnum.NULL_ERROR);
                    }
                    //使用decrement原子操作进行递减库存
                    redisTemplate.opsForValue().decrement("seckillGoods:"+ goodsOrderVo.getGoodsId());
                    //count = count-1
                    //redisTemplate.opsForValue().set("seckillGoods:"+ goodsOrderVo.getGoodsId(),count);
                }

                //rabbitmq异步处理：创建订单
                String messageId = "goods:"+goodsOrderVo.getGoodsId()+"-user:"+goodsOrderVo.getUserId();
                rabbitTemplate.convertAndSend(EXCHANGE_ORDER,"order_route", goodsOrderVo, new CorrelationData(messageId));

                //注意：此处数据库减库存不可用rabbitmq异步处理，会由于多条请求同时对同一行数据操作使得库存不正确
                log.info("扣减数据库库存");
                SeckillGoods seckillGoods = seckillGoodsService.
                        getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsOrderVo.getGoodsId()));
                if (seckillGoods.getStockCount()>0) {
                    seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
                    seckillGoodsService.update(seckillGoods,
                            new QueryWrapper<SeckillGoods>().eq("goods_id", goodsOrderVo.getGoodsId())
                                    //当库存大于0才可更新
                                    .gt("stock_count", 0));
                }

            }
        }catch (Exception e){
            log.warn("系统错误，稍后重试");
        }
        finally {
            //关闭锁
            lock.unlock();
        }
        return ResponseResult.success();
    }

    //添加秒杀商品
    @PostMapping("/add")
    public ResponseResult<?> addGoods(@RequestBody SeckillGoods seckillGoods){
        if (seckillGoods.getStockCount() > goodsService.getOne(new QueryWrapper<Goods>()
                .eq("id",seckillGoods.getGoodsId())).getGoodsStock()){
            return new ResponseResult<>(ResponseEnum.NULL_ERROR.getCode(),"库存不足");
        }
        boolean result = seckillGoodsService.save(seckillGoods);
        if (result){
            //添加库存至redis
            redisTemplate.opsForValue().set("seckillGoods:"+seckillGoods.getGoodsId(),seckillGoods.getStockCount());
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }
}
