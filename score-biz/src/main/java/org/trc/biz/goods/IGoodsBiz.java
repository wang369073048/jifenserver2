package org.trc.biz.goods;

import com.trc.mall.externalservice.dto.CouponDto;
import org.trc.domain.goods.GoodsDO;
import org.trc.util.Pagenation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by hzwzhen on 2017/6/22.
 */
public interface IGoodsBiz {

    /**
     * 保存
     * @param goodsDO
     * @return
     * @throws Exception
     */
    int saveGoodsDO(GoodsDO goodsDO);

    /**
     * 更新
     * @param goodsDO
     * @return
     * @throws Exception
     */
    int updateGoodsDO(GoodsDO goodsDO);

    /**
     * 删除
     * @param goodsDO GoodsDO
     * @return int
     */
    int deleteGoodsDO(GoodsDO goodsDO);

    /**
     * 多条件查询(分页)
     * @param goodsDO GoodsDO
     * @param page Pagenation<GoodsDO>
     * @return Pagenation<GoodsDO>
     */
    Pagenation<GoodsDO> queryGoodsDOListForPage(GoodsDO goodsDO, Pagenation<GoodsDO> page);

    /**
     * 多条件查询(分页)用户视角
     * @param goodsDO GoodsDO
     * @param page Pagenation<GoodsDO>
     * @return Pagenation<GoodsDO>
     */
    Pagenation<GoodsDO> queryGoodsDOListForUser(GoodsDO goodsDO, Pagenation<GoodsDO> page);

    /**
     * 根据ID查询
     * @param id Long
     * @param isUp isUp
     * @return GoodsDO
     */
    GoodsDO getGoodsDOById(Long id, Integer isUp);

    /**
     * 根据ID查询有效商品
     * @param id Long
     * @return GoodsDO
     */
    GoodsDO getEffectiveGoodsById(Long id);

    /**
     * 上架
     * @param id Long
     * @return
     */
    int upById(Long id);

    /**
     * 下架
     * @param id Long
     * @return
     */
    int downById(Long id);

    /**
     * 订单相关操作
     * @param goodsId
     * @param quantity
     * @param version
     * @return
     */
    int orderAssociationProcessing(Long goodsId, Integer quantity, int version);

    /**
     * 查看热卖商品
     * @param shopId
     * @param limit
     * @return
     */
    List<GoodsDO> getHotExchangeList(Long shopId, Long limit);

    /**
     * 查询没有推荐的商品（分页）
     * @param query 查询对象
     * @param page 分页参数
     * @return 查询到的没有推荐的商品
     */
    Pagenation<GoodsDO> queryGoodsDOListExceptRecommendForPage(GoodsDO query, Pagenation<GoodsDO> page);

    /**
     * 查看超值兑换商品
     * @param shopId
     * @return
     */
    List<GoodsDO> getValueExchangeList(Long shopId);

    /**
     * 判断商品列表是否是指定店铺所属
     * @param goodsIdArray
     * @param shopId
     * @return
     */
    boolean isOwnerOf(String[] goodsIdArray, Long shopId);

    /**
     * 检查批次号
     * @param eid
     * @return
     */
    CouponDto checkEid(String eid) throws IOException, URISyntaxException;


}
