package org.example.learn.redis.embedded.hello.api.cmd.key;

import org.example.learn.redis.embedded.hello.api.cmd.StandaloneRedisServerTestSupport;
import org.example.learn.redis.embedded.util.ClassUtils;
import org.junit.Test;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.params.SetParams;

public class Ch01KeyTestSupport extends StandaloneRedisServerTestSupport {

    @Test
    public void test01() {
        
    }

    @Test
    public void test11() {
        String key = "test:username";
        String value = "zhangSan";

        client.set(key, value);
        Long ttl = client.ttl(key);
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.TTL, key, ttl, ClassUtils.getClassName(ttl));
    }

    @Test
    public void test12() {
        String key = "test:username";
        String value = "zhangSan";

        client.set(key, value, new SetParams().ex(10));
        Long ttl = client.ttl(key);
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.TTL, key, ttl, ClassUtils.getClassName(ttl));
    }

    @Test
    public void test13() {
        String key = "test:username";
        String value = "zhangSan";

        client.set(key, value, new SetParams().ex(10));
        Long ttl = client.ttl(key);
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.TTL, key, ttl, ClassUtils.getClassName(ttl));

        client.expire(key, 20);
        Long ttl2 = client.ttl(key);
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.TTL, key, ttl2, ClassUtils.getClassName(ttl2));
    }

    @Test
    public void test14() {
        String key = "test:username";
        String value = "zhangSan";

        client.set(key, value, new SetParams().ex(10));
        Long ttl = client.ttl(key);
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.TTL, key, ttl, ClassUtils.getClassName(ttl));

        client.persist(key);
        Long ttl2 = client.ttl(key);
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.TTL, key, ttl2, ClassUtils.getClassName(ttl2));
    }
}
