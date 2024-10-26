package org.example.learn.redis.embedded.hello.api.cmd;

import org.example.learn.redis.embedded.hello.constant.RedisServerConstants;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import java.io.IOException;

public class StandaloneRedisServerTestSupport {

    protected static final Logger logger = LoggerFactory.getLogger(StandaloneRedisServerTestSupport.class);

    protected RedisServer redisServer;

    protected Jedis client;

    @Before
    public void setup() throws IOException {
        redisServer = new RedisServer(RedisServerConstants.PORT);
        redisServer.start();
        // 如果不使用shutdownHook,jvm进程结束时,redis-server进程还在继续运行
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("正在关闭redis-server");
            redisServer.stop();
        }));

        client = new Jedis(RedisServerConstants.HOST, RedisServerConstants.PORT);
    }

    @After
    public void destroy() {
        // 因为添加了shutdownHook,会自动回收进程资源
//        redisServer.stop();
    }
}
