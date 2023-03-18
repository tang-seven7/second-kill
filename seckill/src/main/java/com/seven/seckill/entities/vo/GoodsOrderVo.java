package com.seven.seckill.entities.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GoodsOrderVo {
    private Long userId;
    private Long goodsId;
    private String goodsName;
    private BigDecimal goodsPrice;
}
