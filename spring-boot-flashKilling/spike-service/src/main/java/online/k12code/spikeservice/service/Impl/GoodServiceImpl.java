package online.k12code.spikeservice.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.k12code.spikeservice.entity.Good;
import online.k12code.spikeservice.entity.OrderRecords;
import online.k12code.spikeservice.mapper.GoodMapper;
import online.k12code.spikeservice.mapper.OrderRecordsMapper;
import online.k12code.spikeservice.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Carl
 * @date 2023/8/25
 **/
@Service
public class GoodServiceImpl extends ServiceImpl<GoodMapper, Good> implements GoodService {

    @Resource
    private GoodMapper goodsMapper;

    @Autowired
    private OrderRecordsMapper orderRecordsMapper;

    @Override
    public void realDoSeckill(Integer goodsId, Integer userId) {
        // 扣减库存  插入订单表
        Good goods = goodsMapper.selectById(goodsId);
        int finalStock = goods.getStocks() - 1;
        if (finalStock < 0) {
            // 只是记录日志 让代码停下来   这里的异常用户无法感知
            throw new RuntimeException("库存不足：" + goodsId);
        }
        goods.setStocks(finalStock);
        goods.setUpdateTime(new Date());
        // insert 要么成功 要么报错  update 会出现i<=0的情况
        // update goods set stocks =  1 where id = 1  没有行锁
        int i = goodsMapper.updateById(goods);
        if (i > 0) {
            // 写订单表
            OrderRecords orderRecords = new OrderRecords();
            orderRecords.setGoodsId(goodsId);
            orderRecords.setUserId(userId);
            orderRecords.setCreateTime(new Date());
            // 时间戳生成订单号
            orderRecords.setOrderSn(String.valueOf(System.currentTimeMillis()));
            orderRecordsMapper.insert(orderRecords);
        }
    }
}
