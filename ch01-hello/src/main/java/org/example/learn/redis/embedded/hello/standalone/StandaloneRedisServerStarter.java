package org.example.learn.redis.embedded.hello.standalone;

import redis.embedded.RedisServer;

import java.io.IOException;

public class StandaloneRedisServerStarter {

    public static void main(String[] args) throws IOException {
        RedisServer redisServer = new RedisServer(6379);
        redisServer.start();
        // do some work
        redisServer.stop();
    }
}
