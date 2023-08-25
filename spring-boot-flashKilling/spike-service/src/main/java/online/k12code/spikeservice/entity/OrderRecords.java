package online.k12code.spikeservice.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Carl
 * @date 2023/8/25
 **/
@Data
public class OrderRecords {

    private Integer id;

    private Integer userId;

    private String orderSn;

    private Integer goodsId;

    private Date createTime;
}
