package online.k12code.spikeservice.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.k12code.spikeservice.entity.OrderRecords;
import online.k12code.spikeservice.mapper.OrderRecordsMapper;
import online.k12code.spikeservice.service.OrderRecordsService;
import org.springframework.stereotype.Service;

/**
 * @author Carl
 * @date 2023/8/25
 **/
@Service
public class OrderRecordsServiceImpl extends ServiceImpl<OrderRecordsMapper, OrderRecords> implements OrderRecordsService {
}
