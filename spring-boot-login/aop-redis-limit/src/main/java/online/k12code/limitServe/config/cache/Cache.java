package online.k12code.limitServe.config.cache;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Carl
 * @date 2023/8/15
 **/
public interface Cache<T> {

    /**
     * Get an item from the cache, nontransactionally
     *
     * @param key 缓存key
     * @return the cached object or <tt>null</tt>
     */
    T get(Object key);

    /**
     * Get an item from the cache, nontransactionally
     *
     * @param key 缓存key
     * @return the cached object or <tt>null</tt>
     */
    String getString(Object key);


    /**
     * multiGet
     *
     * @param keys 要查询的key集合
     * @return 集合
     */
    List multiGet(Collection keys);

    /**
     * 批量set
     *
     * @param map 键值对
     */
    void multiSet(Map map);


    /**
     * 批量删除
     *
     * @param keys 要删除的key集合
     */
    void multiDel(Collection keys);

    /**
     * Add an item to the cache, nontransactionally, with
     * failfast semantics
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    void put(Object key, T value);

    /**
     * 往缓存中写入内容
     *
     * @param key   缓存key
     * @param value 缓存value
     * @param exp   超时时间，单位为秒
     */
    void put(Object key, T value, Long exp);

    /**
     * 往缓存中写入内容
     *
     * @param key      缓存key
     * @param value    缓存value
     * @param exp      过期时间
     * @param timeUnit 过期单位
     */
    void put(Object key, T value, Long exp, TimeUnit timeUnit);

    /**
     * 删除
     *
     * @param key 缓存key
     */
    Boolean remove(Object key);

    /**
     * 删除
     *
     * @param key 缓存key
     */
    void vagueDel(Object key);

    /**
     * Clear the cache
     */
    void clear();


    /**
     * 往缓存中写入内容
     *
     * @param key       缓存key
     * @param hashKey   缓存中hashKey
     * @param hashValue hash值
     */
    void putHash(Object key, Object hashKey, Object hashValue);

    /**
     * 玩缓存中写入内容
     *
     * @param key 缓存key
     * @param map map value
     */
    void putAllHash(Object key, Map map);

    /**
     * 读取缓存值
     *
     * @param key     缓存key
     * @param hashKey map value
     * @return 返回缓存中的数据
     */
    T getHash(Object key, Object hashKey);

    /**
     * 读取缓存值
     *
     * @param key 缓存key
     * @return 缓存中的数据
     */
    Map<Object, Object> getHash(Object key);

    /**
     * 是否包含
     *
     * @param key 缓存key
     * @return 缓存中的数据
     */
    boolean hasKey(Object key);


    /**
     * 模糊匹配key
     *
     * @param pattern 模糊key
     * @return 缓存中的数据
     */
    List<Object> keys(String pattern);

    /**
     * 原生阻塞keys 不推荐使用
     *
     * @param pattern 模糊key
     * @return 缓存中的数据
     */
    List<Object> keysBlock(String pattern);

    //-----------------------------------------------redis计数---------------------------------------------

    /**
     * redis 计数器 累加
     * 注：到达liveTime之后，该次增加取消，即自动-1，而不是redis值为空
     *
     * @param key      为累计的key，同一key每次调用则值 +1
     * @param liveTime 单位秒后失效
     * @return 计数器结果
     */
    Long incr(String key, long liveTime);

    /**
     * redis 计数器 累加
     * 注：到达liveTime之后，该次增加取消，即自动-1，而不是redis值为空
     *
     * @param key 为累计的key，同一key每次调用则值 +1
     * @return 计数器结果
     */
    Long incr(String key);
    //-----------------------------------------------redis计数---------------------------------------------
}

