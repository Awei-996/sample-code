import jdk.jfr.DataAmount;

/**
 * @author Carl
 * @date 2023/8/18
 **/


public class MsgModel {

    private String orderSn;//订单编号
    private Integer userId;//用户id
    private String desc;//下单 物流 短信

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public MsgModel(String orderSn, Integer userId, String desc) {
        this.orderSn = orderSn;
        this.userId = userId;
        this.desc = desc;
    }

    public MsgModel() {
    }

    @Override
    public String toString() {
        return "MsgModel{" +
                "orderSn='" + orderSn + '\'' +
                ", userId=" + userId +
                ", desc='" + desc + '\'' +
                '}';
    }
}
