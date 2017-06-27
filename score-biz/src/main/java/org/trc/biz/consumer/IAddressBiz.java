package org.trc.biz.consumer;

import org.trc.domain.consumer.Address;
import org.trc.form.consumer.AddressForm;
import org.trc.util.Pagenation;

/**
 * Created by hzwzhen on 2017/6/6.
 */
public interface IAddressBiz {

    /**
     * 保存地址
     * @param address
     * @throws Exception
     */
    int saveAddress(Address address);

    /**
     * 更新
     * @param address
     * @return
     * @throws Exception
     */
    int updateAddress(Address address);

    /**
     * 收货地址分页查询
     * @param queryModel
     * @param page
     * @return
     * @throws Exception
     */
    Pagenation<Address> addressPage(AddressForm queryModel, Pagenation<Address> page);
}
