package com.example.seckilldemo.rebbitmq;


import com.example.seckilldemo.pojo.SecKillMessage;
import com.example.seckilldemo.pojo.SeckillOrder;
import com.example.seckilldemo.pojo.User;
import com.example.seckilldemo.service.IGoodsService;
import com.example.seckilldemo.service.IOrderService;
import com.example.seckilldemo.utils.JsonUtil;
import com.example.seckilldemo.vo.GoodsVo;
import com.example.seckilldemo.vo.RespBean;
import com.example.seckilldemo.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/*
* 消息接受者
*
* */
@Service
@Slf4j
public class MQReceiver {
//    @RabbitListener(queues = "queue")
//    public void receive(Object msg) {
//        System.out.println("接收到的消息" + msg);
//    }
//
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg) {
//        log.info("QUEUE01接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg) {
//        log.info("QUEUE02接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct01")
//    public void receive03(Object msg) {
//        log.info("QUEUE01接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct02")
//    public void receive04(Object msg) {
//        log.info("QUEUE02接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void receive05(Object msg) {
//        log.info("QUEUE01接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    public void receive06(Object msg) {
//        log.info("QUEUE02接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_header01")
//    public void receive07(Message message) {
//        log.info("QUEUE01接收消息 message对象" + message);
//        log.info("QUEUE01接收消息" + new String(message.getBody()));
//    }
//
//    @RabbitListener(queues = "queue_header02")
//    public void receive08(Message message) {
//        log.info("QUEUE02接收消息 message对象" + message);
//        log.info("QUEUE02接收消息" + new String(message.getBody()));
//    }

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;

    /*
    *
    * 下单操作
    * */
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        log.info("接受的信息:"+message);
        SecKillMessage secKillMessage = JsonUtil.jsonStr2Object(message, SecKillMessage.class);
        Long goodsId = secKillMessage.getGoodsId();
        User user = secKillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        /*
        * 判断是否重复抢购
        * */
        if(goodsVo.getStockCount()<1){
            return;
        }
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            return;
        }
        /*
        * 下单操作
        * */
        orderService.seckill(user,goodsVo);
    }
}
