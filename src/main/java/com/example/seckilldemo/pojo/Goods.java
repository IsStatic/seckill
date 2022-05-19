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

/**
 * 
 *
 * @author 梁
 * @date 2022-03-21
 */
@Data
@TableName("t_goods")
@ApiModel(value = "", description = "")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 商品id **/
    @ApiModelProperty("商品id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 商品名称 **/
    @ApiModelProperty("商品名称")
    private String goodsName;

    /** 商品标题 **/
    @ApiModelProperty("商品标题")
    private String goodsTitle;

    /** 商品图片 **/
    @ApiModelProperty("商品图片")
    private String goodsImg;

    /** 商品描述 **/
    @ApiModelProperty("商品描述")
    private String goodsDetail;

    /** 商品价格 **/
    @ApiModelProperty("商品价格")
    private BigDecimal goodsPrice;

    /** 商品库存,-1表示没有限制 **/
    @ApiModelProperty("商品库存,-1表示没有限制")
    private Integer goodsStock;


}
