package org.trc.biz.impl.pagehome;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.trc.biz.pagehome.IBannerBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.pagehome.Banner;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BannerException;
import org.trc.form.pagehome.BannerForm;
import org.trc.service.pagehome.IBannerService;
import org.trc.util.DateUtils;
import org.trc.util.Pagenation;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hzwzhen on 2017/6/9.
 */
@Service("bannerBiz")
public class BannerBiz implements IBannerBiz {
    private Logger logger = LoggerFactory.getLogger(BannerBiz.class);
    @Autowired
    private IBannerService bannerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveBanner(Banner banner){
      try{
          validateForAdd(banner);
          banner.setIsUp(false); //默认为不上架
          banner.setCreateTime(Calendar.getInstance().getTime());
          banner.setIsDeleted(false);
          int addResult  = bannerService.insertSelective(banner);
          if(addResult != 1){
              logger.error("新增Banner异常!请求参数为:"+ banner);
              throw new BannerException(ExceptionEnum.BANNER_SAVE_EXCEPTION,"新增Banner异常!");
          }
          logger.info("新增ID=>[" + banner.getId() + "]的Banner成功");
          banner.setSort(banner.getId().intValue());
          int upateResult = bannerService.updateByPrimaryKeySelective(banner);
          if(upateResult != 1){
              logger.error("修改Banner异常!请求参数为:"+ banner);
              throw new BannerException(ExceptionEnum.BANNER_UPDATE_EXCEPTION,"修改Banner异常!");
          }
          return upateResult;
      }catch (BannerException e){
          throw e;
      }catch (IllegalArgumentException e){
          logger.error("新增Banner校验参数异常!", e);
          throw new BannerException(ExceptionEnum.PARAM_CHECK_EXCEPTION, e.getMessage());
      }catch (Exception e) {
          logger.error("新增Banner信息异常!请求参数为:"+banner, e);
          throw new BannerException(ExceptionEnum.BANNER_SAVE_EXCEPTION, "新增Banner信息异常!");
      }
    }

    @Override
    public int updateBanner(Banner banner){
        int result = bannerService.updateByPrimaryKeySelective(banner);
        if(result != 1){
            logger.error("修改Banner异常!请求参数为:"+ banner);
            throw new BannerException(ExceptionEnum.BANNER_UPDATE_EXCEPTION,"修改Banner异常!");
        }
        return result;
    }

    @Override
    public Banner selectOne(Banner banner) {
        return bannerService.selectOne(banner);
    }


    @Override
    public Pagenation<Banner> bannerPage(BannerForm queryModel, Pagenation<Banner> page) {
        Example example = new Example(Banner.class);
        Example.Criteria criteria = example.createCriteria();
        if (null != queryModel.getShopId()){ //商铺ID
            criteria.andEqualTo("shopId",queryModel.getShopId());
        }
        if (StringUtils.isNotBlank(queryModel.getName())){ //banner名称
            criteria.andEqualTo("name",queryModel.getName());
        }
        if (null !=queryModel.getIsUp()){ //banner名称
            criteria.andEqualTo("isUp",queryModel.getIsUp());
        }
        if (queryModel.getStartTime() != null) {//开始日期
            criteria.andGreaterThanOrEqualTo("updateTime", new Date(queryModel.getStartTime()));
        }
        if (queryModel.getEndTime() != null) {//截止日期
            Date endTime = new Date(queryModel.getEndTime());
            criteria.andLessThan("updateTime", DateUtils.addDays(endTime, 1));
        }
        criteria.andEqualTo("type",queryModel.getType());
        example.orderBy("sort").asc();
        page = bannerService.pagination(example,page,queryModel);
        return page;
    }

    @Override
    public Banner  selectByIdAndShopId(BannerForm form) {
        Assert.notNull(form, "Banner不能为空!");
        Assert.notNull(form.getShopId(), "店铺Id不能为空!");
        Assert.notNull(form.getId(), "BannerId不能为空!");
        Banner banner = bannerService.selectByPrimaryKey(form.getId());
        if (null == banner)
            throw new BannerException(ExceptionEnum.BANNER_ID_NOT_EXIST,"bannerId不存在");
        if (banner.getShopId().longValue() != form.getShopId().longValue())
            throw new BannerException(ExceptionEnum.BANNER_QUERY_EXCEPTION,"操作不合法");
        return banner;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int exchangeSort(Banner bannerA, Banner bannerB){
        Assert.notNull(bannerA, "bannerA不能为空!");
        Assert.notNull(bannerB, "bannerB不能为空!");
        Integer tempSort = bannerA.getSort();
        bannerA.setSort(bannerB.getSort());
        bannerB.setSort(tempSort);
        int resultA = bannerService.updateByPrimaryKeySelective(bannerA);
        if (resultA != 1) {
            throw new BannerException(ExceptionEnum.BANNER_UPDATE_EXCEPTION, String.format("交换Banner排序BannerIdA=>[%s],BannerIdB=>[%s]异常!",bannerA.getId(),bannerB.getId()));
        }
        int resultB = bannerService.updateByPrimaryKeySelective(bannerB);
        if (resultB != 1) {
            throw new BannerException(ExceptionEnum.BANNER_UPDATE_EXCEPTION, String.format("交换Banner排序BannerIdA=>[%s],BannerIdB=>[%s]异常!",bannerA.getId(),bannerB.getId()));
        }
        logger.info(String.format("交换Banner排序BannerIdA=>[%s],BannerIdB=>[%s]成功!",bannerA.getId(),bannerB.getId()));
        return 1;
    }

    @Override
    public int deleteById(Long id) {
        return bannerService.deleteByPrimaryKey(id);
    }

    private void validateForAdd(Banner bannerDO) {
        Assert.notNull(bannerDO, "banner不能为空!");
        Assert.notNull(bannerDO.getShopId(), "店铺ID不能为空!");
        Assert.isTrue(StringUtils.isNotBlank(bannerDO.getName()), "banner名称不能为空!");
        Assert.isTrue(StringUtils.isNotBlank(bannerDO.getOperatorUserId()), "操作人ID不能为空!");
    }

}
