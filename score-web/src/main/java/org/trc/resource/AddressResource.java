package org.trc.resource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.consumer.IAddressBiz;
import org.trc.domain.consumer.Address;
import org.trc.form.consumer.AddressForm;
import org.trc.util.AppResult;
import org.trc.util.Pagenation;
import org.trc.util.RedisUtil;
import org.trc.util.ResultUtil;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Date;


/**
 * Created by hzwzhen on 2017/6/6.
 */
@Component
@Path("test")
public class AddressResource {

    private Logger logger = LoggerFactory.getLogger(AddressResource.class);
    @Autowired
    private IAddressBiz addressBiz;


    @GET
    @Path("address")
    public AppResult saveAddress(){
        Address address = new Address();
        address.setUserId("201512040929176188868d2365cd444ca833046f944178d97");
        address.setProvinceCode("110000");
        address.setCityCode("110114");
        address.setAreaCode("22");
        address.setAddress("北京市");
        address.setReceiverName("zhangsan");
        address.setPhone("18012341234");
        address.setPostcode("88");
        address.setIsDefault(true);
        address.setIsDeleted(true);
        address.setCreateTime(new Date());
        addressBiz.saveAddress(address);
        return ResultUtil.createSucssAppResult("保存地址成功", "");
    }

    @GET
    @Path("updateAddress")
    public AppResult updateAddress(){
        Address address = new Address();
        address.setId(10000L);
        address.setUserId("201512040929176188868d2365cd444ca833046f944178d97");
        address.setProvinceCode("00");
        address.setCityCode("11");
        address.setAreaCode("22");
        address.setAddress("北京市");
        addressBiz.updateAddress(address);
        return ResultUtil.createSucssAppResult("更新地址成功", "");
    }

    @GET
    @Path("addressList")
    @Produces(MediaType.APPLICATION_JSON)
    public Pagenation<Address> brandPage(@BeanParam AddressForm form, @BeanParam Pagenation<Address> page) throws Exception {
        page.setPageNo(1);
        page.setPageSize(10);
        return addressBiz.addressPage(form,page);
    }

    @GET
    @Path("redis")
    public AppResult testRedis(){
        logger.info("set key result ==> {}" , RedisUtil.set("11999","1111"));
        logger.info("get key result ==> {}" , RedisUtil.get("11999"));
        if (RedisUtil.set("11999","1111")){
            return ResultUtil.createSucssAppResult("成功", "");
        }else{
            return ResultUtil.createFailAppResult("失败");
        }
    }








}
