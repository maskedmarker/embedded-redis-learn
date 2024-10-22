package org.example.learn.redis.embedded.hello.standalone;

import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * 以单机版启动redis-server
 */
public class StandaloneRedisServerStarter {

    public static void main(String[] args) throws IOException {
        RedisServer redisServer = new RedisServer(6379);
        // 实际是将redis运行在单独的进程中
        redisServer.start();

        // 如果不使用shutdownHook,jvm进程结束时,redis-server进程还在继续运行
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("正在关闭redis-server");
            redisServer.stop();
        }));
    }
}
