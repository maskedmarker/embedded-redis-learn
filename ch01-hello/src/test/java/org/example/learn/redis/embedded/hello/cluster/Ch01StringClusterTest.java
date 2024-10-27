package org.example.learn.redis.embedded.hello.cluster;

import org.example.learn.redis.embedded.util.ClassUtils;
import org.junit.Test;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.params.SetParams;

public class Ch01StringClusterTest extends ClusterRedisServerTestSupport {

    /**
     * set
     * del
     */
    @Test
    public void test01() {
        String key = "test:username";
        String value = "zhangSan";

        String opResult = client.set(key, value);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.SET, key, value, opResult, ClassUtils.getClassName(opResult));

        Long opResult20 = client.del(key);
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.DEL, key, opResult20, ClassUtils.getClassName(opResult20));
        Long opResult21 = client.del(key);
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.DEL, key, opResult21, ClassUtils.getClassName(opResult21));
    }

    /**
     * setnx
     */
    @Test
    public void test021() {
        String key = "test:username";
        String value = "zhangSan";

        client.del(key);
        Long opResult = client.setnx(key, value);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.SETNX, key, value, opResult, ClassUtils.getClassName(opResult));
        Long opResult2 = client.setnx(key, value);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.SETNX, key, value, opResult2, ClassUtils.getClassName(opResult2));
    }

    /**
     * set key value params
     * 可以实现setnx相同的效果
     */
    @Test
    public void test022() {
        String key = "test:username";
        String value = "zhangSan";

        client.del(key);
        String opResult3 = client.set(key, value, new SetParams().nx());
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.SET, key, value, opResult3, ClassUtils.getClassName(opResult3));
        String opResult4 = client.set(key, value, new SetParams().nx());
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.SET, key, value, opResult4, ClassUtils.getClassName(opResult4));
    }
}
