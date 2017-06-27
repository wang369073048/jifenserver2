package org.trc.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.pagehome.IBannerBiz;
import org.trc.domain.pagehome.Banner;

import java.util.Calendar;

/**
 * Created by hzwzhen on 2017/6/9.
 */
@RunWith(SpringJUnit4ClassRunner.class) //标记测试运行的环境
@ContextConfiguration(locations = {"classpath:config/resource-context.xml"}) //配合spring测试  可以引入多个配置文件
//@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = true) 过时,下面为替代写法
@Rollback(value = true)
@Transactional(transactionManager = "transactionManager")
public class BannerBizTest {
    Logger logger = LoggerFactory.getLogger(BannerBizTest.class);
    @Autowired
    private IBannerBiz bannerBiz;

    @Test
    public void testSaveBanner(){
        Banner banner = new Banner();
        banner.setShopId(1L);
        banner.setName("518");
        banner.setContentId(1123L);
        banner.setIsUp(true);
        banner.setSort(98);
        banner.setDescription("测试");
        banner.setOperatorUserId("userId-1");
        banner.setIsDeleted(false);
        banner.setCreateTime(Calendar.getInstance().getTime());
        banner.setUpdateTime(Calendar.getInstance().getTime());
        try {
            int count = bannerBiz.saveBanner(banner);
            Assert.assertTrue(count > 0);
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
        }
    }

    @Test
    public void deleteBanner() {
        int count = bannerBiz.deleteById(22L);
        Assert.assertEquals(1,count);
    }
}
