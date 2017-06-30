package org.trc.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.domain.goods.CardCouponsDO;
import org.trc.service.goods.ICouponsService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
@RunWith(SpringJUnit4ClassRunner.class) //标记测试运行的环境
@ContextConfiguration(locations = {"classpath:config/resource-context.xml"}) //配合spring测试  可以引入多个配置文件
//@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = true) 过时,下面为替代写法
@Rollback(value = false)
@Transactional(transactionManager = "transactionManager")
public class CardCouponsBizTest {
    @Autowired
    private ICouponsService couponsService;
    @Autowired
    private ICouponsBiz couponsBiz;
    @Test
    public void testDelete_01(){
        CardCouponsDO cardCouponsDO = new CardCouponsDO();
        cardCouponsDO.setBatchNumber("Y3uWAGmtSST2MXXu");
        cardCouponsDO.setShopId(1L);
        int result = couponsService.deleteByBatchNumber(cardCouponsDO);
        Assert.assertEquals(1,result);
    }

    @Test
    public void testSelect_02(){
        CardCouponsDO cardCouponsDO = new CardCouponsDO();
        cardCouponsDO.setBatchNumber("Y3uWAGmtSST2MXXu");
        cardCouponsDO.setShopId(1L);
        cardCouponsDO = couponsService.selectOne(cardCouponsDO);
        Assert.assertEquals("Y3uWAGmtSST2MXXu",cardCouponsDO.getBatchNumber());
    }
}
