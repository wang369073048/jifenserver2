package org.trc.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.trc.util.RedisUtil;

/**
 * Created by hzwzhen on 2017/6/9.
 */
@RunWith(SpringJUnit4ClassRunner.class) //标记测试运行的环境
@ContextConfiguration(locations = {"classpath:config/resource-context.xml"}) //配合spring测试  可以引入多个配置文件
public class RedisUtilTest {

    @Test
    public void testRedis(){
        Assert.assertTrue(RedisUtil.set("11","22"));
        Assert.assertEquals("33",RedisUtil.get("11"));
    }
}
