package com.seven.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seven.seckill.entities.pojo.SeckillGoods;
import com.seven.seckill.service.SeckillGoodsService;
import com.seven.seckill.mapper.SeckillGoodsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author User
* @description 针对表【t_seckill_goods】的数据库操作Service实现
* @createDate 2023-03-15 17:08:07
*/
@Service
@Slf4j
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods>
    implements SeckillGoodsService, InitializingBean{
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;

    //初始化Bean时，将秒杀商品存入redis缓存
    @Override
    public void afterPropertiesSet() throws Exception {
        if (seckillGoodsMapper.selectList(null)!=null){
            List<SeckillGoods> list = seckillGoodsMapper.selectList(null);
            for (SeckillGoods goods:list){
                redisTemplate.opsForValue().set("seckillGoods:"+goods.getGoodsId(),goods.getStockCount());
            }
        }
    }
}




