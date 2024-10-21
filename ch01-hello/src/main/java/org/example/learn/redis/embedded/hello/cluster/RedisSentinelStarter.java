package org.example.learn.redis.embedded.hello.cluster;

import redis.embedded.RedisCluster;
import redis.embedded.util.JedisUtil;

import java.util.Set;

public class RedisSentinelStarter {

    public static void main(String[] args) {
        RedisCluster cluster;
        Set<String> jedisSentinelHosts;
        //creates a cluster with 3 sentinels, quorum size of 2 and 3 replication groups, each with one master and one slave
        cluster = RedisCluster.builder().ephemeral().sentinelCount(3).quorumSize(2)
                .replicationGroup("master1", 1)
                .replicationGroup("master2", 1)
                .replicationGroup("master3", 1)
                .build();
        cluster.start();

        //retrieve ports on which sentinels have been started, using a simple Jedis utility class
        jedisSentinelHosts = JedisUtil.sentinelHosts(cluster);
    }
}
