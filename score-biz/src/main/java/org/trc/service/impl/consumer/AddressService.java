package org.trc.service.impl.consumer;

import org.springframework.stereotype.Service;
import org.trc.domain.consumer.Address;
import org.trc.service.consumer.IAddressService;
import org.trc.service.impl.BaseService;

/**
 * Created by hzwzhen on 2017/6/6.
 */
@Service("addressService")
public class AddressService extends BaseService<Address,Long> implements IAddressService{
}
