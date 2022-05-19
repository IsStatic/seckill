package com.example.seckilldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckilldemo.pojo.Goods;
import com.example.seckilldemo.vo.GoodsVo;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author 梁
 * @date 2022-03-21
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    /*
    * 获取商品列表
    * */
//    @Select({"        SELECT g.id,\n" +
//            "               g.goods_name,\n" +
//            "               g.goods_title,\n" +
//            "               g.goods_img,\n" +
//            "               g.goods_detail,\n" +
//            "               g.goods_price,\n" +
//            "               g.goods_stock,\n" +
//            "               t.seckill_price,\n" +
//            "               t.stock_count,\n" +
//            "               t.start_date,\n" +
//            "               t.end_date\n" +
//            "        FROM t_goods g\n" +
//            "                 left join t_seckill_goods t on g.id = t.goods_id"})
//    @Select("findGoodsVo")
    List<GoodsVo> findGoodsVo();

    /*
    * 获取商品详情
    * */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
