package org.example.learn.redis.embedded.hello.cluster;

import org.example.learn.redis.embedded.hello.constant.RedisServerConstants;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.embedded.RedisCluster;
import redis.embedded.RedisServer;
import redis.embedded.util.JedisUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ClusterRedisServerTestSupport {

    protected static final Logger logger = LoggerFactory.getLogger(ClusterRedisServerTestSupport.class);

    private RedisCluster cluster;
    private Set<String> jedisSentinelHosts;

    protected JedisCluster client;

    @Before
    public void setup() throws IOException {
        //creates a cluster with 3 sentinels, quorum size of 2 and 3 replication groups, each with one master and one slave
        cluster = RedisCluster.builder().ephemeral().sentinelCount(3).quorumSize(2)
                .replicationGroup("master1", 1)
                .replicationGroup("master2", 1)
                .replicationGroup("master3", 1)
                .build();
        cluster.start();

        //retrieve ports on which sentinels have been started, using a simple Jedis utility class
        jedisSentinelHosts = JedisUtil.sentinelHosts(cluster);

        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort(RedisServerConstants.HOST, RedisServerConstants.PORT));
        nodes.add(new HostAndPort(RedisServerConstants.HOST, RedisServerConstants.PORT));
        nodes.add(new HostAndPort(RedisServerConstants.HOST, RedisServerConstants.PORT));
        client = new JedisCluster(nodes);
    }

    @After
    public void destroy() {
        // 因为添加了shutdownHook,会自动回收进程资源
//        redisServer.stop();
    }
}
