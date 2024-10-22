package org.example.learn.redis.embedded.hello.api.cmd;

import org.example.learn.redis.embedded.util.ClassUtils;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.params.SetParams;

import java.util.Calendar;

public class Ch01StringTest extends BaseStandaloneRedisServerTest {

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

    /**
     * expire
     * 在n秒后失效
     * a relative way of specifying the TTL
     */
    @Test
    public void test031() throws InterruptedException {
        String key = "test:username";
        String value = "zhangSan";
        int secondsToExpire = 3;

        client.del(key);
        client.set(key, value);
        Long opResult = client.expire(key, secondsToExpire);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.EXPIRE, key, value, opResult, ClassUtils.getClassName(opResult));
        String opResult2 = client.get(key);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.GET, key, value, opResult2, ClassUtils.getClassName(opResult2));

        Thread.sleep((secondsToExpire + 2) * 1000);
        String opResult4 = client.get(key);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.GET, key, value, opResult4, ClassUtils.getClassName(opResult4));
        Assert.assertNull(opResult4);
    }

    /**
     * set key value ex
     * 等价于 set+expire
     */
    @Test
    public void test032() throws InterruptedException {
        String key = "test:username";
        String value = "zhangSan";
        int secondsToExpire = 3;

        client.del(key);
        String opResult = client.set(key, value, new SetParams().ex(secondsToExpire));
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.SET, key, value, opResult, ClassUtils.getClassName(opResult));

        Thread.sleep((secondsToExpire + 2) * 1000);
        String opResult2 = client.get(key);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.GET, key, value, opResult2, ClassUtils.getClassName(opResult2));
        Assert.assertNull(opResult2);
    }

    /**
     * expireAt
     * 在指定时间失效
     * an absolute way of specifying the TTL
     */
    @Test
    public void test033() throws InterruptedException {
        String key = "test:username";
        String value = "zhangSan";
        int secondsToExpire = 3;
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, secondsToExpire);

        client.del(key);
        client.set(key, value);
        Long opResult = client.expireAt(key, now.getTimeInMillis() / 1000);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.EXPIREAT, key, value, opResult, ClassUtils.getClassName(opResult));
        Thread.sleep((secondsToExpire + 2) * 1000);
        String opResult2 = client.get(key);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.GET, key, value, opResult2, ClassUtils.getClassName(opResult2));
    }

    /**
     * expireAt在过去时间
     * expireAt本身成功,但是key会立马失效
     */
    @Test
    public void test034() throws InterruptedException {
        String key = "test:username";
        String value = "zhangSan";
        int secondsToExpire = -3;
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, secondsToExpire);

        client.del(key);
        client.set(key, value);

        // expireAt本身成功
        Long opResult = client.expireAt(key, now.getTimeInMillis() / 1000);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.EXPIREAT, key, value, opResult, ClassUtils.getClassName(opResult));
        Assert.assertEquals(Long.valueOf(1L), opResult);

        String opResult2 = client.get(key);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.GET, key, value, opResult2, ClassUtils.getClassName(opResult2));
        Assert.assertNull(opResult2);
    }

    /**
     * 再次执行expire, 会更新TTL
     */
    @Test
    public void test035() throws InterruptedException {
        String key = "test:username";
        String value = "zhangSan";
        int secondsToExpire = 3;

        client.del(key);
        client.set(key, value);
        Long opResult = client.expire(key, secondsToExpire);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.EXPIRE, key, value, opResult, ClassUtils.getClassName(opResult));
        String opResult2 = client.get(key);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.GET, key, value, opResult2, ClassUtils.getClassName(opResult2));

        // 再次执行expire, 会更新TTL
        Long opResult3 = client.expire(key, secondsToExpire * 10);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.EXPIRE, key, value, opResult3, ClassUtils.getClassName(opResult3));

        Thread.sleep((secondsToExpire + 2) * 1000);
        String opResult4 = client.get(key);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.GET, key, value, opResult4, ClassUtils.getClassName(opResult4));
        Assert.assertNotNull(opResult4);
    }

    /**
     * set/getset命令会覆盖原有的所有属性信息(包括TTL)
     */
    @Test
    public void test036() throws InterruptedException {
        String key = "test:username";
        String value = "zhangSan";
        int secondsToExpire = 3;

        client.del(key);
        client.set(key, value);
        Long opResult = client.expire(key, secondsToExpire);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.EXPIRE, key, value, opResult, ClassUtils.getClassName(opResult));

        Long ttl = client.ttl(key);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.TTL, key, value, ttl, ClassUtils.getClassName(ttl));
        Thread.sleep(1000);

        // 新的set命令会覆盖原有的所有属性信息(包括TTL)
        client.set(key, value + value);
        // getset命令也会覆盖原有的所有属性信息(包括TTL)
//        client.getSet(key, value + value);

        // 上一步的set操作导致ttl丢失,所以返回-1
        Long ttl2 = client.ttl(key);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.TTL, key, value, ttl2, ClassUtils.getClassName(ttl2));
        Assert.assertEquals(Long.valueOf(-1L), ttl2);
        Assert.assertNotEquals(ttl, ttl2);

        Thread.sleep((secondsToExpire + 2) * 1000);
        String opResult2 = client.get(key);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.GET, key, value, opResult2, ClassUtils.getClassName(opResult2));
        Assert.assertNotNull(opResult2);
        Assert.assertEquals(value + value, opResult2);
    }
}
