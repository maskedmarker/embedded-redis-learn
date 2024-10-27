package org.example.learn.redis.embedded.hello.cluster;

import redis.embedded.RedisCluster;

import java.util.Arrays;
import java.util.List;

public class RedisClusterStarter {

    public static void main(String[] args) {

        final List<Integer> group1 = Arrays.asList(6667, 6668);
        final List<Integer> group2 = Arrays.asList(6387, 6379);

        // creates a cluster with quorum size of 2 and 3 replication groups, each with one master and one slave
        RedisCluster cluster = RedisCluster.builder()
                .serverPorts(group1).replicationGroup("master1", 1)
                .serverPorts(group2).replicationGroup("master2", 1)
                .ephemeralServers().replicationGroup("master3", 1)
                .build();
        cluster.start();

        Runtime.getRuntime().addShutdownHook(new Thread(cluster::stop));

        System.out.println("-------------");
    }
}
