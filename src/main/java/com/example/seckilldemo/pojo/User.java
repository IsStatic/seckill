package com.example.seckilldemo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author 梁
 * @date 2022-03-21
 */
@Setter
@Getter
@TableName("t_user")
@ApiModel(value = "", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID 手机号码 **/
    @ApiModelProperty("用户ID 手机号码")
      private Long id;

    private String nickname;

    /** MD5二次加密 **/
    @ApiModelProperty("MD5二次加密")
    private String pasword;

    private String slat;

    /** 头像 **/
    @ApiModelProperty("头像")
    private String head;

    /** 注册时间 **/
    @ApiModelProperty("注册时间")
    private Date registerDate;

    /** 最后一次登录时间 **/
    @ApiModelProperty("最后一次登录时间")
    private Date lastLoginDate;

    /** 登录次数 **/
    @ApiModelProperty("登录次数")
    private Integer loginCount;


}
