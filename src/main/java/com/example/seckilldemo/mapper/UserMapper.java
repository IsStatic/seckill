package com.example.seckilldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckilldemo.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 *  Mapper 接口
 *
 * @author 梁
 * @date 2022-03-21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
