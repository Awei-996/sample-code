package online.k12code.spikeservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import online.k12code.spikeservice.entity.Good;

/**
 * @author Carl
 * @date 2023/8/25
 **/
public interface GoodService extends IService<Good> {
    void realDoSeckill(Integer goodsId, Integer userId);
}
