package org.trc.service.aclUnit;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.trc.biz.impower.IAclResourceBiz;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by hzdaa on 2017/9/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)  //标记测试运行的环境
@ContextConfiguration(locations = {"classpath:config/resource-context.xml"}) //配合spring测试  可以引入多个配置文件
public class AclTest {
    Logger logger = LoggerFactory.getLogger(AclTest.class);

    @Resource
    private IAclResourceBiz jurisdictionBiz;

    @Test
    public void testAclUserAccreditInfoBiz(){

        List<Map<String,Object>> list = jurisdictionBiz.getHtmlJurisdiction("828167F0BD11411AB28C4085F15A38B0");

        System.out.println(JSON.toJSONString(list));

    }

}
