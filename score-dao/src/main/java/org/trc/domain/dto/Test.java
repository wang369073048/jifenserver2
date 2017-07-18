package org.trc.domain.dto;

import com.alibaba.fastjson.JSON;
import org.trc.domain.order.OrdersDO;
import org.trc.util.FatherToChildUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/14
 */
public class Test {
    public static void main(String[] args) {
        OrdersDO ordersDO1 = new OrdersDO();
        ordersDO1.setBarcode("1");
        ordersDO1.setCouponCode("11");
        OrdersDO ordersDO2 = new OrdersDO();
        ordersDO2.setBarcode("2");
        ordersDO2.setCouponCode("22");
        List<OrdersDO> list = new ArrayList<>();
        list.add(ordersDO1);
        list.add(ordersDO2);
        for(int i = 0;i<list.size();i++){
            OrdersDO ordersDO = list.get(i);
            OrderDTO orderDTO = new OrderDTO();
            FatherToChildUtils.fatherToChild(ordersDO,orderDTO);
            orderDTO.setAddress(ordersDO.getCouponCode());
            list.set(i,orderDTO);
        }
        System.out.println(JSON.toJSON(list));


    }
}
