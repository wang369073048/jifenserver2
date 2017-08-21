package org.trc.biz.impl.luckydraw;

import com.txframework.core.jdbc.PageRequest;
import com.txframework.util.Assert;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.biz.luckydraw.IWinningRecordBiz;
import org.trc.biz.order.INewOrderBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.domain.dto.WinningRecordDTO;
import org.trc.domain.luckydraw.ActivityDetailDO;
import org.trc.domain.luckydraw.WinningRecordDO;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.order.OrdersExtendDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BusinessException;
import org.trc.exception.OrderException;
import org.trc.service.luckydraw.IWinningRecordService;
import org.trc.service.shop.IShopService;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/7
 */
@Service("winningRecordBiz")
public class WinningRecordBiz implements IWinningRecordBiz{

    Logger logger = LoggerFactory.getLogger(WinningRecordBiz.class);
    @Autowired
    private IWinningRecordService winningRecordService;
    @Autowired
    private INewOrderBiz newOrderBiz;
    @Autowired
    private IShopBiz shopBiz;
    @Override
    public int insertWinningRecord(WinningRecordDO winningRecord) {
        return 0;
    }

    @Override
    public List<Long> limitedLottery(WinningRecordDO winningRecord) {
        return null;
    }

    @Override
    public Map<Long, Integer> selectPlatformActivityPrizesLimit(WinningRecordDO winningRecord) {
        return null;
    }

    @Override
    public Map<Long, Integer> selectActivityPrizesLimit(WinningRecordDO winningRecord) {
        return null;
    }

    @Override
    public List<WinningRecordDTO> listWinningRecord(WinningRecordDTO param) {
        List<WinningRecordDTO> winningRecordDTOs = winningRecordService.listByParams(param);
        for(WinningRecordDTO item : winningRecordDTOs){
            item.setOrderAddress(newOrderBiz.getOrderAddressByOrderNum(item.getOrderNum()));
        }
        return winningRecordDTOs;
    }

    @Override
    public Pagenation<WinningRecordDTO> queryWinningRecord(WinningRecordDTO param, Pagenation<WinningRecordDTO> pageRequest) {
        try {
            Assert.notNull(pageRequest, "分页参数不能为空");
            Assert.notNull(param, "传入参数不能为空");
            pageRequest = winningRecordService.selectByParams(param, pageRequest);
            List<WinningRecordDTO> list = pageRequest.getInfos();
            for(WinningRecordDTO item : list){
                if(StringUtils.isNotBlank(item.getOrderNum())) {
                    OrdersDO ordersDO = new OrdersDO();
                    ordersDO.setOrderNum(item.getOrderNum());
                    try {
                        OrdersDO order = newOrderBiz.selectByParams(ordersDO);
                        item.setOrderAddress(order.getOrderAddressDO());
                    } catch (OrderException e){
                        logger.debug("用户未选择收货地址!");
                    }
                }
            }
            return pageRequest;
        } catch (IllegalArgumentException e) {
            logger.error("多条件查询WinningRecordDTO校验参数异常!",e);
            throw new BusinessException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (Exception e) {
            logger.error("多条件查询WinningRecordDTO信息异常!", e);
            throw new BusinessException(ExceptionEnum.QUERY_LIST_EXCEPTION,"多条件查询WinningRecordDTO信息异常!");
        }
    }

    @Override
    public List<ActivityDetailDO> listActivityDetail(ActivityDetailDO param) {
        List<ActivityDetailDO> result = winningRecordService.listActivityDetailByParams(param);
        for(ActivityDetailDO activityDetail : result){
            activityDetail.setShopName(shopBiz.getShopDOById(activityDetail.getShopId()).getShopName());
        }
        return result;
    }

    @Override
    public Pagenation<ActivityDetailDO> queryActivityDetail(ActivityDetailDO param, Pagenation<ActivityDetailDO> pageRequest) {
        try {
            Assert.notNull(pageRequest, "分页参数不能为空");
            Assert.notNull(param, "传入参数不能为空");
            pageRequest = winningRecordService.selectActivityDetailByParams(param, pageRequest);
            List<ActivityDetailDO> list = pageRequest.getInfos();
            for(ActivityDetailDO activityDetail : list){
                activityDetail.setShopName(shopBiz.getShopDOById(activityDetail.getShopId()).getShopName());
            }
            return pageRequest;
        } catch (IllegalArgumentException e) {
            logger.error("多条件查询ActivityDetailDO校验参数异常!",e);
            throw new BusinessException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (Exception e) {
            logger.error("多条件查询ActivityDetailDO信息异常!", e);
            throw new BusinessException(ExceptionEnum.QUERY_LIST_EXCEPTION,"多条件查询ActivityDetailDO信息异常!");
        }
    }

    @Override
    public Integer countWinningRecord(WinningRecordDO param) {
        return null;
    }

    @Override
    public WinningRecordDTO getWinningRecord(WinningRecordDO param) {
        WinningRecordDO winningRecord = winningRecordService.selectOneForUpdate(param);
        OrdersDO ordersDO = new OrdersDO();
        ordersDO.setOrderNum(winningRecord.getOrderNum());
        OrdersDO order = newOrderBiz.selectByParams(ordersDO);
        OrdersExtendDO paramOe = new OrdersExtendDO();
        paramOe.setOrderNum(winningRecord.getOrderNum());
        OrdersExtendDO ordersExtend = newOrderBiz.getOrdersExtendByOrderNum(paramOe);
        WinningRecordDTO result = new WinningRecordDTO();
        result.setPlatform(winningRecord.getPlatform());
        result.setPrizeName(winningRecord.getPrizeName());
        result.setNumberOfPrizes(winningRecord.getNumberOfPrizes());
        result.setGoodsType(winningRecord.getGoodsType());
        result.setGoodsNo(winningRecord.getGoodsNo());
        result.setState(winningRecord.getState());
        result.setLotteryPhone(winningRecord.getLotteryPhone());
        if(null!=ordersExtend) {
            result.setRemark(ordersExtend.getRemark());
        }
        if(null != order) {
//            result.setOrderAddress(order.getOrderAddressDO());
//            result.setLogisticsDO(order.getLogisticsDO());

            if(order.getOrderAddressDO() != null) {
                result.setProvinceCode(order.getOrderAddressDO().getProvinceCode());
                result.setCityCode(order.getOrderAddressDO().getCityCode());
                result.setAreaCode(order.getOrderAddressDO().getAreaCode());
                result.setAddress(order.getOrderAddressDO().getAddress());
                result.setProvince(order.getOrderAddressDO().getProvince());
                result.setCity(order.getOrderAddressDO().getCity());
                result.setArea(order.getOrderAddressDO().getArea());
                result.setReceiverName(order.getOrderAddressDO().getReceiverName());
                result.setReceiverPhone(order.getOrderAddressDO().getPhone());
                result.setPostcode(order.getOrderAddressDO().getPostcode());
            }
            if(order.getLogisticsDO() != null){
                result.setCompanyName(order.getLogisticsDO().getCompanyName());
                result.setShipperCode(order.getLogisticsDO().getShipperCode());
                result.setLogisticsNum(order.getLogisticsDO().getLogisticsNum());
                result.setFreight(order.getLogisticsDO().getFreight());
            }


        }

        return result;
    }

    @Override
    public int updateState(WinningRecordDO param) {
        return 0;
    }

    @Override
    public WinningRecordDO selectOne(WinningRecordDO param) {
        return null;
    }
}
