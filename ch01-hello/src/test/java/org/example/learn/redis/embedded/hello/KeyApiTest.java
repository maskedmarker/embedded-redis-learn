package org.example.learn.redis.embedded.hello;


import org.example.learn.redis.embedded.hello.constant.RedisConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.time.Instant;

public class KeyApiTest {

    private static final Logger LOG = LoggerFactory.getLogger(KeyApiTest.class);

    RedisServer redisServer;

    private Jedis jedis;

    private static final String KEY = "test:key";
    private static final String VALUE = "changjiang";

    @Before
    public void setup() throws IOException {
        redisServer = new RedisServer(6379);
        redisServer.start();

        jedis = new Jedis(RedisConstants.HOST, RedisConstants.PORT);
    }

    @After
    public void destroy() {
        redisServer.stop();
    }

    @Test
    public void testKey() {
        //删除所以的key是否成功
        LOG.info("清空所有key, 执行操作的结果: {}", jedis.flushAll());

        //执行操作的结果返回类型是Boolean
        LOG.info("EXISTS {}, 执行操作的结果: {}", KEY, jedis.exists(KEY));

        LOG.info("SET {} {}, 执行操作的结果: {}", KEY, VALUE, jedis.set(KEY, VALUE));

        LOG.info("TYPE {}, 执行操作的结果: {}", KEY, jedis.type(KEY));

        //执行操作的结果返回类型是Long,表示有多少个key被删除了
        LOG.info("DEL {}, 执行操作的结果: {}", KEY, jedis.del(KEY));

        //执行操作的结果返回类型是Set
        LOG.info("KEYS *, 执行操作的结果: {}", jedis.keys("*"));

    }

    @Test
    public void testTTL() {
        jedis.del("expireKey");
        jedis.set("expireKey", "0001");

        //执行操作的结果表示 1: the timeout was set. 0: the timeout was not set(key已经过期了)
        LOG.info("设置key的超时时间, 执行操作的结果: {}", jedis.expire("expireKey", 10));
        LOG.info("设置key的超时时间, 执行操作的结果: {}", jedis.expire("expireKey", 10));

        LOG.info("设置key在某个时间点超时, 执行操作的结果: {}", jedis.expireAt("expireKey", Instant.now().getEpochSecond() + 8));

        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Integer reply, returns the remaining time to live in seconds of a key that has an EXPIRE.
        //等于0表示已经失效了,等于-1/-2表示没有设置失效时间或key不存在,具体参考api的注释
        LOG.info("查看键username:0001的存活时间, 剩余存活时间(seconds): {}", jedis.ttl("expireKey"));
    }

    @Test
    public void testPTTL() {
        jedis.del("expireKey");
        jedis.set("expireKey", "0001");
        LOG.info("设置key的超时时间, 执行操作的结果: {}", jedis.expire("expireKey", 10));

        LOG.info("设置key在某个时间点超时, 执行操作的结果: {}", jedis.expireAt("expireKey", Instant.now().getEpochSecond() + 8));

        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //pttl的单位是毫秒,精度更高precise
        LOG.info("查看键username:0001的存活时间, 剩余存活时间(seconds): {}", jedis.pttl("expireKey"));
    }
}
