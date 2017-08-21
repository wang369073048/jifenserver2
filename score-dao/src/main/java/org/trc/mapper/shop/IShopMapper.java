package org.trc.mapper.shop;

import org.trc.domain.shop.ShopDO;
import org.trc.util.BaseMapper;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/5
 */
public interface IShopMapper extends BaseMapper<ShopDO>{
	
	/**
	* 多条件查询表信息(分页)
	* @param shopDO ShopDO
	* @param pageRequest PageRequest<ShopDO>
	* @return List<ShopDO>
	*/
	List<ShopDO> selectListByParams(ShopDO shopDO, Pagenation<ShopDO> pageRequest);
}
