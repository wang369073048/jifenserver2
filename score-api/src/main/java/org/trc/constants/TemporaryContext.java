package org.trc.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangzhen
 */
public class TemporaryContext {

    public static final Map<String,String> EXCHANGE_CURRENCY_SHOP_DATA;

    public static final Map<Long,String> SHOP_DATA;

    public static final Map<Long,String> SHOP_EXCHANGE_CURRENCY;

    static{
        //init EXCHANGE_CURRENCY_SHOP_DATA
        EXCHANGE_CURRENCY_SHOP_DATA = new HashMap<String,String>();
        EXCHANGE_CURRENCY_SHOP_DATA.put("TCOIN","泰然金融");
        EXCHANGE_CURRENCY_SHOP_DATA.put("ecard","泰然城");
        //init SHOP_DATA
        SHOP_DATA = new HashMap<Long,String>();
        SHOP_DATA.put(1l,"泰然金融");
        SHOP_DATA.put(2l,"泰然城");
        //init SHOP_EXCHANGE_CURRENCY
        SHOP_EXCHANGE_CURRENCY = new HashMap<Long,String>();
        SHOP_EXCHANGE_CURRENCY.put(1l,"TCOIN");
        SHOP_EXCHANGE_CURRENCY.put(2l,"ecard");
    }

    public static String getShopNameById(Long shopId){
        return SHOP_DATA.get(shopId);
    }

    public static String getShopNameByExchangeCurrency(String exchangeCurrency){
        return EXCHANGE_CURRENCY_SHOP_DATA.get(exchangeCurrency);
    }

    public static String getExchangeCurrencyById(Long shopId){
        return SHOP_EXCHANGE_CURRENCY.get(shopId);
    }

}
