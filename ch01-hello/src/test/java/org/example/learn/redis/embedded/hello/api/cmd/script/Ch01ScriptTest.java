package org.example.learn.redis.embedded.hello.api.cmd.script;

import org.example.learn.redis.embedded.hello.api.cmd.BaseStandaloneRedisServerTest;
import org.example.learn.redis.embedded.util.ClassUtils;
import org.example.learn.redis.embedded.util.ScriptConstants;
import org.junit.Test;
import redis.clients.jedis.Protocol;

import java.util.Arrays;
import java.util.Collections;

public class Ch01ScriptTest extends BaseStandaloneRedisServerTest {

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
}
