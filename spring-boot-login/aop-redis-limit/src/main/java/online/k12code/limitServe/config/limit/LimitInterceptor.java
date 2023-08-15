package online.k12code.limitServe.config.limit;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import online.k12code.limitServe.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;

/**
 * @author Carl
 * @date 2023/8/15
 **/
@Aspect
@Configuration
@Slf4j
public class LimitInterceptor {


    private RedisTemplate<String, Serializable> redisTemplate;

    private DefaultRedisScript<Long> limitScript;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setLimitScript(DefaultRedisScript<Long> limitScript) {
        this.limitScript = limitScript;
    }

    @Before("@annotation(limitPointAnnotation)")
    public void interceptor(LimitPoint limitPointAnnotation) throws Exception {
        // 获取限制类型
        LimitTypeEnums limitTypeEnums = limitPointAnnotation.limitType();

        String key;
        // 获取时间区间 限制条件
        int limitPeriod = limitPointAnnotation.period();
        int limitCount = limitPointAnnotation.limit();
        if (limitTypeEnums == LimitTypeEnums.CUSTOMER) {
            key = limitPointAnnotation.key();
        } else {
            key = limitPointAnnotation.key() + IpUtils
                    .getIpAddress(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        }
        // ImmutableList 线程安全，高效
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(limitPointAnnotation.prefix(), key));
        try {
            Number count = redisTemplate.execute(limitScript, keys, limitCount, limitPeriod);
            assert count != null;
            log.info("限制请求{}, 当前请求{},缓存key{}", limitCount, count.intValue(), key);
            //如果缓存里没有值，或者他的值小于限制频率
            if (count.intValue() > limitCount) {
                throw new Exception("限制请求");
            }
        }
        //如果从redis中执行都值判定为空，则这里跳过
        catch (NullPointerException e) {
            return;
        } catch (Exception e) {
            log.error("限流异常", e);
            throw new Exception("限制请求");
        }
    }

}
