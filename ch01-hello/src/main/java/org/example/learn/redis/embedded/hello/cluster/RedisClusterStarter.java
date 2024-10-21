package org.example.learn.redis.embedded.hello.cluster;

import redis.embedded.RedisCluster;
import redis.embedded.util.JedisUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RedisClusterStarter {

    public static void main(String[] args) {
        final List<Integer> sentinels = Arrays.asList(26739, 26912);
        final List<Integer> group1 = Arrays.asList(6667, 6668);
        final List<Integer> group2 = Arrays.asList(6387, 6379);

        //creates a cluster with 3 sentinels, quorum size of 2 and 3 replication groups, each with one master and one slave
        RedisCluster cluster = RedisCluster.builder().sentinelPorts(sentinels).quorumSize(2)
                .serverPorts(group1).replicationGroup("master1", 1)
                .serverPorts(group2).replicationGroup("master2", 1)
                .ephemeralServers().replicationGroup("master3", 1)
                .build();
        cluster.start();
    }
}
