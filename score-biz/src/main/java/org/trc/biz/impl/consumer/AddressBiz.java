package org.trc.biz.impl.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.biz.consumer.IAddressBiz;
import org.trc.domain.consumer.Address;
import org.trc.form.consumer.AddressForm;
import org.trc.service.consumer.IAddressService;
import org.trc.util.Pagenation;
import tk.mybatis.mapper.entity.Example;

/**
 * Created by hzwzhen on 2017/6/6.
 */
@Service("addressBiz")
public class AddressBiz implements IAddressBiz{

    @Autowired
    private IAddressService addressService;

    @Override
    public int saveAddress(Address address) {
        return addressService.insertSelective(address);
    }

    @Override
    public int updateAddress(Address address){
        return addressService.updateByPrimaryKeySelective(address);
    }

    @Override
    public Pagenation<Address> addressPage(AddressForm queryModel, Pagenation<Address> page){
        Example example = new Example(Address.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("createTime").desc();
        Pagenation<Address> pagenation = addressService.pagination(example, page, queryModel);
        return pagenation;
    }
}
