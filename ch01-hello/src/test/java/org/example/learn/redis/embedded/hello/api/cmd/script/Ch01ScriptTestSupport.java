package org.example.learn.redis.embedded.hello.api.cmd.script;

import org.example.learn.redis.embedded.hello.api.cmd.StandaloneRedisServerTestSupport;
import org.example.learn.redis.embedded.util.ClassUtils;
import org.example.learn.redis.embedded.util.ScriptConstants;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Protocol;

import java.util.Arrays;
import java.util.Collections;

public class Ch01ScriptTestSupport extends StandaloneRedisServerTestSupport {

    /**
     * eval的返回结果类型跟lua脚本的返回结果有关系
     */
    @Test
    public void test01() {
        String key = "test:lua";

        String script = "return 1";
        Object opResult = client.eval(script, Collections.singletonList(key), Collections.emptyList());
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.EVAL, key, opResult, ClassUtils.getClassName(opResult));
        String script2 = "return '1'";
        Object opResult2 = client.eval(script2, Collections.singletonList(key), Collections.emptyList());
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.EVAL, key, opResult2, ClassUtils.getClassName(opResult2));
        // 不写return语句，或者return nil,就对应返回null
        String script3 = "return nil";
        Object opResult3 = client.eval(script3, Collections.singletonList(key), Collections.emptyList());
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.EVAL, key, opResult3, ClassUtils.getClassName(opResult3));
    }

    @Test
    public void test02() {
        String key = "test:username";
        String value = "zhangSan";
        String value2 = "liSi";

        client.set(key, value);
        String opResult = client.set(key, value);
        logger.info("命令: {} {} {}, 执行操作的结果: {}({})", Protocol.Command.SET, key, value, opResult, ClassUtils.getClassName(opResult));
        Object opResult2 = client.eval(ScriptConstants.COMPARE_AND_SET, Collections.singletonList(key), Arrays.asList(value, value2));
        logger.info("命令: {} {} {} {}, 执行操作的结果: {}({})", Protocol.Command.EVAL, key, value, value2, opResult2, ClassUtils.getClassName(opResult2));
        Object opResult3 = client.eval(ScriptConstants.COMPARE_AND_SET, Collections.singletonList(key), Arrays.asList(value, value2));
        logger.info("命令: {} {} {} {}, 执行操作的结果: {}({})", Protocol.Command.EVAL, key, value, value2, opResult3, ClassUtils.getClassName(opResult3));
    }

    @Test
    public void test21() {
        String key = "test:username";
        String value = "zhangSan";

        String script = "redis.call('set', KEYS[1], ARGV[1]); redis.call('expire', KEYS[1], 10); return redis.call('ttl', KEYS[1])";
        Object opResult = client.eval(script, Collections.singletonList(key), Arrays.asList(value));
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.EVAL, key, opResult, ClassUtils.getClassName(opResult));

        Long ttl = client.ttl(key);
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.TTL, key, ttl, ClassUtils.getClassName(ttl));
    }

    /**
     * secondsToExpire为负值,导致key立马失效,即不存在,当key不存在时,返回-2
     * 在script中依然保持这个特性
     */
    @Test
    public void test22() {
        String key = "test:username";
        String value = "zhangSan";

        String script = "redis.call('set', KEYS[1], ARGV[1]); redis.call('expire', KEYS[1], -10); return redis.call('ttl', KEYS[1])";
        Object opResult = client.eval(script, Collections.singletonList(key), Arrays.asList(value));
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.EVAL, key, opResult, ClassUtils.getClassName(opResult));

        Long ttl = client.ttl(key);
        logger.info("命令: {} {}, 执行操作的结果: {}({})", Protocol.Command.TTL, key, ttl, ClassUtils.getClassName(ttl));
        // secondsToExpire为负值,导致key立马失效,即不存在,当key不存在时,返回-2
        Assert.assertEquals(Long.valueOf(-2L), ttl);
    }
}
