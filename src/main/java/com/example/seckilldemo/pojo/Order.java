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
@Data
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@TableName("t_order")
@ApiModel(value = "", description = "")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单ID **/
    @ApiModelProperty("订单ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 用户ID **/
    @ApiModelProperty("用户ID")
    private Long userId;

    /** 商品ID **/
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /** 收获地址ID **/
    @ApiModelProperty("收获地址ID")
    private Long deliveryAddrId;

    /** 商品名字 **/
    @ApiModelProperty("商品名字")
    private String goodsName;

    /** 商品数量 **/
    @ApiModelProperty("商品数量")
    private Integer goodsCount;

    /** 商品价格 **/
    @ApiModelProperty("商品价格")
    private BigDecimal goodsPrice;

    /** 1 pc,2 android, 3 ios **/
    @ApiModelProperty("1 pc,2 android, 3 ios")
    private Integer orderChannel;

    /** 订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退货，5已完成 **/
    @ApiModelProperty("订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退货，5已完成")
    private Integer status;

    /** 订单创建时间 **/
    @ApiModelProperty("订单创建时间")
    private Date createDate;

    /** 支付时间 **/
    @ApiModelProperty("支付时间")
    private Date payDate;


}
