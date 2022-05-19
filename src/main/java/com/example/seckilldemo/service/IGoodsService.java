package com.example.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckilldemo.pojo.Goods;
import com.example.seckilldemo.vo.GoodsVo;

import java.util.List;

/**
 * 
 *
 * @author 梁
 * @date 2022-03-21
 */
public interface IGoodsService extends IService<Goods> {
    /*
    * 获取商品列表
    * */
    List<GoodsVo> findGoodsVo();

    /*
    * 获取商品详情
    * */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
