package com.seven.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seven.seckill.entities.pojo.Goods;
import com.seven.seckill.service.GoodsService;
import com.seven.seckill.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

/**
* @author User
* @description 针对表【t_goods】的数据库操作Service实现
* @createDate 2023-03-15 17:07:00
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

}




