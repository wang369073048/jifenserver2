package org.trc.biz.impl.luckydraw;

import com.txframework.core.jdbc.PageRequest;
import com.txframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.luckydraw.ILuckyDrawBiz;
import org.trc.domain.luckydraw.ActivityDetailDO;
import org.trc.domain.luckydraw.ActivityPrizesDO;
import org.trc.domain.luckydraw.LuckyDrawDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BusinessException;
import org.trc.service.luckydraw.IActivityPrizesService;
import org.trc.service.luckydraw.ILuckyDrawService;

import java.util.*;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
@Service("luckyDrawBiz")
public class LuckyDrawBiz implements ILuckyDrawBiz{
    @Autowired
    private ILuckyDrawService luckyDrawService;
    @Autowired
    private IActivityPrizesService activityPrizesService;
    @Override
    public LuckyDrawDO getLuckyDraw(LuckyDrawDO luckyDraw) {
        return null;
    }

    @Override
    public void insertLuckyDraw(LuckyDrawDO luckyDraw) {
        luckyDrawService.insertSelective(luckyDraw);
    }

    @Override
    public PageRequest<LuckyDrawDO> queryLuckyDraw(LuckyDrawDO luckyDraw, PageRequest<LuckyDrawDO> pageRequest) {
        return null;
    }

    @Override
    public ActivityDetailDO slyderAdventures(LuckyDrawDO param, String userId, String phone) {
        return null;
    }

    @Override//TODO
    public void updateLuckyDraw(LuckyDrawDO luckyDraw) {
//        try {
//            LuckyDrawDO editEntity = _validateLuckyDraw(luckyDraw);
//            luckyDrawService.updateByPrimaryKeySelective(editEntity);
//            if(null != editEntity.getPlatform()){
//                _saveBatch(luckyDraw.getShopId(), luckyDraw.getId(), luckyDraw.getActivityPrizesList());
//            }
//        } catch (IllegalArgumentException e) {
//            logger.error("更新抽奖活动校验参数异常!",e);
//            throw new BusinessException(BusinessException.ABNORMAL_PARAMETER,e.getMessage());
//        } catch (BusinessException e) {
//            throw e;
//        } catch (Exception e) {
//            logger.error("更新抽奖活动异常!",e);
//            throw new BusinessException(BusinessException.UPDATE_EXCEPTION,e.getMessage());
//        }

    }

    private LuckyDrawDO _validateLuckyDraw(LuckyDrawDO luckyDraw) {
        Date now = Calendar.getInstance().getTime();
        if(null == luckyDraw.getId()) {
            Assert.isTrue(null != luckyDraw && null != luckyDraw.getStartTime() && luckyDraw.getStartTime().after(now), "抽奖活动起始时间不能为空，且不能早于当前时间!");
            Assert.isTrue(null != luckyDraw.getEndTime() && luckyDraw.getStartTime().before(luckyDraw.getEndTime()), "抽奖活动结束时间不能为空，且不能早于开始时间!");
        }
        Assert.isTrue(null != luckyDraw.getShopId(), "抽奖活动所属店铺不能为空！");
        Assert.isTrue(null != luckyDraw.getFreeLotteryTimes() && luckyDraw.getFreeLotteryTimes() >= 0, "免费抽奖次数不合法!");
        Assert.isTrue(null != luckyDraw.getExpenditure() && luckyDraw.getExpenditure() > 0, "扣减积分数量不合法!");
        Assert.isTrue(null != luckyDraw.getDailyDrawLimit() && luckyDraw.getDailyDrawLimit() > -2, "没人每日抽奖次数限制不合法!");
        if(null != luckyDraw.getId()) {
            LuckyDrawDO param = new LuckyDrawDO();
            param.setId(luckyDraw.getId());
            param.setShopId(luckyDraw.getShopId());
            LuckyDrawDO entity = luckyDrawService.getLuckyDraw(param);
            if(!entity.getStartTime().equals(luckyDraw.getStartTime())){
                throw new BusinessException(ExceptionEnum.ERROR_ILLEGAL_OPERATION,"编辑时不允许修改抽奖活动起始时间!");
            }
            if(luckyDraw.getEndTime().before(entity.getEndTime())){
                throw new BusinessException(ExceptionEnum.ERROR_ILLEGAL_OPERATION,"编辑时结束时间不能早于原结束时间!");
            }
            if(entity.getStartTime().before(now)){
                param.setEndTime(luckyDraw.getEndTime());
                return param;
            }else{
                return luckyDraw;
            }
        }else{
            return luckyDraw;
        }

    }

    @Override
    public void setDeliveryAddress(Long addressId, String userId, String orderNum) {

    }

    @Override
    public void deleteEntity(LuckyDrawDO luckyDraw) {

    }

    @Override
    public int freeLotteryTimes(Long luckyDrawId, String platform, String userId) {
        return 0;
    }


//    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
//    private int _saveBatch(Long shopId, Long luckyDrawId, List<ActivityPrizesDO> activityPrizesList) {
//        //1、奖品数据处理
//        int totalWinningProbability = 0;
//        List<Long> idList = new ArrayList<>();
//        for(ActivityPrizesDO item : activityPrizesList){
//            totalWinningProbability += item.getWinningProbability();
//            if(null != item.getId()) {
//                idList.add(item.getId());
//            }else{
//                item.setLuckyDrawId(luckyDrawId);
//                item.setShopId(shopId);
//            }
//        }
//        //2、归属权限判断
//        Map<String,Object> params = new HashMap();
//        params.put("shopId", shopId);
//        params.put("luckyDrawId", luckyDrawId);
//        params.put("list", idList);
//        if(idList.size()>0) {
//            int count = activityPrizesService.checkActivityPrizes(params);
//            if (count != idList.size()) {
//                throw new BusinessException(BusinessException.ABNORMAL_PARAMETER, "参数检查异常!");
//            }
//        }
//        //3、编辑活动时，移除奖品
//        activityPrizesMapper.deleteActivityPrizes(params);
//        //4、中奖总几率判断
//        if(totalWinningProbability != 10000){
//            throw new BusinessException(LuckyDrawException.WINNING_PROBABILITY_ILLEGAL, "中奖几率不合法!");
//        }else{
//            for(ActivityPrizesDO item : activityPrizesList){
//                if(null == item.getId()){
//                    if(null == item.getPrizeType()){
//                        throw new BusinessException(BusinessException.ABNORMAL_PARAMETER, "参数检查异常!");
//                    }
//                    switch(item.getPrizeType()){
//                        case LuckyDraw.PrizyType.SCORE:
//                            item.setGoodsType(LuckyDraw.GoodsType.VIRTUAL);
//                            break;
//                        case LuckyDraw.PrizyType.TCOIN:
//                            item.setGoodsType(LuckyDraw.GoodsType.VIRTUAL);
//                            break;
//                        case LuckyDraw.PrizyType.GOODS:
//                            GoodsDO prizy = goodsService.getPrizeGoodsById(item.getGoodsId());
//                            CategoryDO category = categoryService.getCategoryDOById(prizy.getCategory());
//                            if(1==category.getIsVirtual().intValue()){
//                                item.setGoodsType(LuckyDraw.GoodsType.VIRTUAL);
//                            }else{
//                                item.setGoodsType(LuckyDraw.GoodsType.MATERIAL);
//                            }
//                            break;
//                        case LuckyDraw.PrizyType.TKY_FOR_PARTICIPATING:
//                            item.setGoodsType(LuckyDraw.GoodsType.TKY_FOR_PARTICIPATING);
//                            break;
//                        default:
//                            break;
//                    }
//                    activityPrizesMapper.insertActivityPrizes(item);
//                }else{
//                    activityPrizesMapper.updateActivityPrizes(item);
//                }
//            }
//        }
//        return activityPrizesList.size();
//    }
}
