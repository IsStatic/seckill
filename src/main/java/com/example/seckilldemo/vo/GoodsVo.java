package com.example.seckilldemo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.seckilldemo.pojo.Goods;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/*
*
* 商品返回对象
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true)
public class GoodsVo extends Goods{
    /** 秒杀商品ID **/
    private Long id;

    /** 商品ID **/
    private Long goodsId;

    /** 秒杀价 **/
    private BigDecimal seckillPrice;

    /** 库存数量 **/
    private Integer stockCount;

    /** 秒杀开始时间 **/
    private Date startDate;

    /** 秒杀结束时间 **/
    private Date endDate;
}
