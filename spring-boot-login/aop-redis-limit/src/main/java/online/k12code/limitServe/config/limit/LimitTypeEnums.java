package online.k12code.limitServe.config.limit;

/**
 * redis 限流类型
 * @author Carl
 * @date 2023/8/15
 **/
public enum LimitTypeEnums {
    /**
     * 自定义key(即全局限流)
     */
    CUSTOMER,
    /**
     * 根据请求者IP（IP限流）
     */
    IP
}
