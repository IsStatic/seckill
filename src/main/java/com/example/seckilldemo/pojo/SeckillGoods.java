package com.example.seckilldemo.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 *
 * @author 梁
 * @date 2022-03-21
 */
@Getter
@Setter
@TableName("t_seckill_goods")
@ApiModel(value = "", description = "")
public class SeckillGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 秒杀商品ID **/
    @ApiModelProperty("秒杀商品ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 商品ID **/
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /** 秒杀价 **/
    @ApiModelProperty("秒杀价")
    private BigDecimal seckillPrice;

    /** 库存数量 **/
    @ApiModelProperty("库存数量")
    private Integer stockCount;

    /** 秒杀开始时间 **/
    @ApiModelProperty("秒杀开始时间")
    private Date startDate;

    /** 秒杀结束时间 **/
    @ApiModelProperty("秒杀结束时间")
    private Date endDate;


}
