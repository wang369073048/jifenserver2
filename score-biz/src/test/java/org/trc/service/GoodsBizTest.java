package org.trc.service;

import com.trc.mall.externalservice.TrCouponAck;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.trc.biz.goods.IGoodsBiz;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/1
 */
@RunWith(SpringJUnit4ClassRunner.class) //标记测试运行的环境
@ContextConfiguration(locations = {"classpath:config/resource-context.xml"}) //配合spring测试  可以引入多个配置文件
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = false)
public class GoodsBizTest {
    @Autowired
    private IGoodsBiz goodsBiz;

    @Test
    public void test001_checkEid(){
        String eid = "123zasd1s-sas1z-1sx1";
        TrCouponAck trCouponAck = goodsBiz.checkEid(eid);
        trCouponAck.getPackageFrom();
        trCouponAck.getPackageTo();
        System.out.println(trCouponAck.getPackageFrom());
        System.out.println(trCouponAck.getPackageTo());
    }
}
