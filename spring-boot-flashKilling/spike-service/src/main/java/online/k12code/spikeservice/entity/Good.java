package online.k12code.spikeservice.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Carl
 * @date 2023/8/25
 **/
@Data
public class Good {
    private Integer id;

    private String goodsName;

    private BigDecimal price;

    private Integer stocks;

    private Integer status;

    private String pic;

    private Date createTime;

    private Date updateTime;
}
