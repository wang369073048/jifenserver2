package org.trc.biz.impl.pagehome;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.trc.biz.pagehome.IBannerContentBiz;
import org.trc.domain.pagehome.Banner;
import org.trc.domain.pagehome.BannerContent;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BannerContentException;
import org.trc.exception.BannerException;
import org.trc.form.pagehome.BannerContentForm;
import org.trc.service.pagehome.IBannerContentService;
import org.trc.service.pagehome.IBannerService;
import org.trc.util.DateUtils;
import org.trc.util.Pagenation;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by hzwzhen on 2017/6/12.
 */
@Service("bannerContentBiz")
public class BannerContentBiz implements IBannerContentBiz{

    private Logger logger = LoggerFactory.getLogger(BannerContentBiz.class);
    @Autowired
    private IBannerContentService bannerContentService;
    @Autowired
    private IBannerService bannerService;
    @Override
    public int saveBannerContent(BannerContent bannerContent) {
        validateForAdd(bannerContent);
        int result = bannerContentService.insertSelective(bannerContent);
        logger.info("新增ID=>[{}]的bannerContent成功",bannerContent.getId());
        return 0;
    }

    @Override
    public int updateBannerContent(BannerContent bannerContent) {
        int result = bannerContentService.updateByPrimaryKeySelective(bannerContent);
        if(result != 1){
            logger.error("修改BannerContent异常!请求参数为:",bannerContent);
            throw new BannerException(ExceptionEnum.BANNER_UPDATE_EXCEPTION,"修改BannerContent异常!");
        }
        logger.info("修改ID=>[{}]的bannerContent成功",bannerContent.getId());
        return result;
    }

    @Override
    public BannerContent selectByIdAndShopId(BannerContent queryDO) {
        Assert.notNull(queryDO, "BannerContent不能为空!");
        Assert.notNull(queryDO.getShopId(), "店铺Id不能为空!");
        Assert.notNull(queryDO.getId(), "BannerContentId不能为空!");
        BannerContent bannerContent = bannerContentService.selectByPrimaryKey(queryDO.getId());
        if (null == bannerContent)
            throw new BannerException(ExceptionEnum.BANNER_ID_NOT_EXIST,"bannerId不存在");
        if (bannerContent.getShopId().longValue() != queryDO.getShopId().longValue())
            throw new BannerException(ExceptionEnum.BANNER_QUERY_EXCEPTION,"操作不合法");
        return bannerContent;
    }

    @Override
    public int deleteBannerContent(BannerContent bannerContent) {
        // Validate
        Assert.notNull(bannerContent, "BannerContentDO不能为空!");
        Assert.notNull(bannerContent.getId(), "ID不能为空!");
        Assert.isTrue(StringUtils.isNotBlank(bannerContent.getOperatorUserId()), "操作人ID不能为空!");
        // 删除前校验关联信息
        // 查询是否有上架Banner使用
        Banner banner = new Banner();
        banner.setIsUp(true);
        banner.setContentId(bannerContent.getId());
        int useBannerCount = bannerService.selectCount(banner);
        if (useBannerCount > 0) {
            throw new BannerContentException(ExceptionEnum.BANNERCONTENT_UPDATE_EXCEPTION ,"此内容绑定有Banner不能删除!");
        }
        //逻辑删除
        bannerContent.setIsDeleted(true);
        bannerContent.setUpdateTime(Calendar.getInstance().getTime());
        int result = bannerContentService.updateByPrimaryKeySelective(bannerContent);
        if (result != 1) {
            logger.error("删除BannerContent异常!请求参数为:"+bannerContent);
            throw new BannerContentException(ExceptionEnum.BANNERCONTENT_UPDATE_EXCEPTION, "删除BannerContent异常!");
        }
        logger.info("逻辑删除ID=>[{}]的BannerContentDO成功!",bannerContent.getId());
        return result;
    }

    @Override
    public Pagenation<BannerContent> bannerContentPage(BannerContentForm queryModel, Pagenation<BannerContent> page) {
        Example example = new Example(Banner.class);
        Example.Criteria criteria = example.createCriteria();
        if (null != queryModel.getShopId()){ //商铺ID
            criteria.andEqualTo("shopId",queryModel.getShopId());
        }
        if (StringUtils.isNotBlank(queryModel.getTitle())){ //标题
            criteria.andEqualTo("title",queryModel.getTitle());
        }
        if (StringUtil.isNotEmpty(queryModel.getStartDate())) {//开始日期
            criteria.andGreaterThanOrEqualTo("updateTime", DateUtils.parseDate(queryModel.getStartDate()));
        }
        if (StringUtil.isNotEmpty(queryModel.getEndDate())) {//截止日期
            Date endDate = DateUtils.parseDate(queryModel.getEndDate());
            criteria.andLessThan("updateTime", DateUtils.addDays(endDate, 1));
        }
        example.orderBy("createTime").desc();
        page = bannerContentService.pagination(example,page,queryModel);
        return page;
    }


    /**
     * Validate Add
     *
     * @param bannerContent
     */
    private void validateForAdd(BannerContent bannerContent) {
        Assert.notNull(bannerContent, "bannerContentDO不能为空!");
        Assert.notNull(bannerContent.getShopId(), "店铺ID不能为空!");
        Assert.isTrue(StringUtils.isNotBlank(bannerContent.getTitle()), "标题不能为空!");
        Assert.isTrue(StringUtils.isNotBlank(bannerContent.getImgUrl()), "封面图地址不能为空!");
        Assert.isTrue(StringUtils.isNotBlank(bannerContent.getTargetUrl()), "跳转地址不能为空!");
        Assert.isTrue(StringUtils.isNotBlank(bannerContent.getOperatorUserId()), "操作人ID不能为空!");
    }

}
