package org.trc.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.trc.biz.consumer.IAddressBiz;
import org.trc.domain.consumer.Address;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hzwzhen on 2017/6/6.
 */
@RunWith(SpringJUnit4ClassRunner.class) //标记测试运行的环境
@ContextConfiguration(locations = {"classpath:config/resource-context.xml"}) //配合spring测试  可以引入多个配置文件
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = false)
public class AddressBizTest {
    @Autowired
    IAddressBiz addressBiz;

    /**
     * 保存
     */
    @Test
    public void saveAddress(){
        Address address = new Address();
        address.setUserId("201512040929176188868d2365cd444ca833046f944178d97");
        address.setProvinceCode("110000");
        address.setCityCode("110114");
        address.setAreaCode("140101");
        address.setAddress("北京市");
        address.setReceiverName("zhangsan");
        address.setPhone("18012341234");
        address.setPostcode("88");
        address.setIsDefault(false);
        address.setIsDeleted(false);
        address.setCreateTime(Calendar.getInstance().getTime());
        try {
            Assert.assertEquals(1,addressBiz.saveAddress(address));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新
     */
    @Test
    public void updateAddress(){
        Address address = new Address();
        address.setId(39L);
        address.setUpdateTime(new Date());
        try {
            Assert.assertEquals(1,addressBiz.updateAddress(address));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
