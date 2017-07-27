package org.trc.biz.impl.score;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.trc.biz.score.IScoreConverterBiz;
import org.trc.constants.ScoreCst;
import org.trc.domain.score.ScoreConverter;
import org.trc.domain.score.ScoreConverterFlow;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.ConverterException;
import org.trc.exception.ScoreConverterException;
import org.trc.service.score.IScoreConverterFlowService;
import org.trc.service.score.IScoreConverterService;
import org.trc.util.ConvertUtil;

/**
 * Created by hzwzhen on 2017/6/14.
 */
@Service("scoreConverterBiz")
public class ScoreConverterBiz implements IScoreConverterBiz{

    Logger logger = LoggerFactory.getLogger(ScoreConverterBiz.class);

    @Autowired
    private IScoreConverterService scoreConverterService;
    @Autowired
    private IScoreConverterFlowService scoreConverterFlowService;
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public int saveScoreConverter(ScoreConverter scoreConverter) {
        try{
            Assert.notNull(scoreConverter, "积分兑换规则不能为空");
            Assert.isTrue(StringUtils.isNotEmpty(scoreConverter.getChannelCode()), "积分兑换渠道不能为空");
            Assert.isTrue(StringUtils.isNotEmpty(scoreConverter.getExchangeCurrency()), "积分兑换外币不能为空");
            validate(scoreConverter);
            //查询该币种是否已存在
            ScoreConverter query = new ScoreConverter();
            query.setIsDeleted(0);
            query.setExchangeCurrency(scoreConverter.getExchangeCurrency());
            ScoreConverter entity = scoreConverterService.selectOne(query);
            if (null != entity) {
                throw new ConverterException(ExceptionEnum.CONVERTER_SAVE_EXCEPTION, "该币种是否已存在!");
            }
            //插入配置规则
            int result = scoreConverterService.insertSelective(scoreConverter);
            //TODO 插入流水
            insertScoreConverterFlow(scoreConverter);
            return result;
        }catch (IllegalArgumentException e) {
            logger.error("新增ScoreConverter参数校验异常!",e);
            throw new ConverterException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (ConverterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("新增"+scoreConverter.getExchangeCurrency()+"兑换规则失败!",e);
            throw new ConverterException(ExceptionEnum.CONVERTER_SAVE_EXCEPTION, "新增"+scoreConverter.getExchangeCurrency()+"兑换规则失败!");
        }
    }

    private void insertScoreConverterFlow(ScoreConverter scoreConverter) {
        try {
            //插入流水
            ScoreConverterFlow flow = ConvertUtil.convert(scoreConverter, new ScoreConverterFlow());
            scoreConverter.setIsDeleted(0);
            ScoreConverter converter = scoreConverterService.selectOne(scoreConverter);
            flow.setConverterId(converter.getId());
            flow.setOperatedBy(converter.getCreateBy());
            flow.setOperatedTime(converter.getCreateTime());
            scoreConverterFlowService.insertSelective(flow);
        } catch (Exception e) {
            logger.error("新增"+scoreConverter.getExchangeCurrency()+"ScoreConverterFlow失败!",e);
            throw new ScoreConverterException(ExceptionEnum.INSERT_EXCEPTION, "新增"+scoreConverter.getExchangeCurrency()+"ScoreConverterFlow失败!");
        }
    }

    @Override
    public int updateScoreConverter(ScoreConverter scoreConverter) {
        try {
            Assert.notNull(scoreConverter, "积分兑换规则不能为空");
            Assert.notNull(scoreConverter.getId(), "积分兑换规则id不能为空");
            validate(scoreConverter);
            //更新配置规则
            int result = scoreConverterService.updateByPrimaryKeySelective(scoreConverter);
            if(result != 1){
                throw new ConverterException(ExceptionEnum.CONVERTER_UPDATE_EXCEPTION, "根据ID=>[" + scoreConverter.getId() + "]修改兑换规则失败!");
            }
            // TODO 插入流水
            insertScoreConverterFlow(scoreConverter);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("根据ID=>[" + scoreConverter.getId() + "]修改兑换规则参数校验失败!",e);
            throw new ConverterException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (ConverterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("根据ID=>[" + scoreConverter.getId() + "]修改兑换规则失败!",e);
            throw new ConverterException(ExceptionEnum.CONVERTER_UPDATE_EXCEPTION,"根据ID=>[" + scoreConverter.getId() + "]修改兑换规则失败!");
        }
    }

    @Override
    public ScoreConverter selectOne(ScoreConverter scoreConverter) {
        try{
            Assert.notNull(scoreConverter,"积分兑换规则不能为空");
            Assert.isTrue(StringUtils.isNotBlank(scoreConverter.getExchangeCurrency()),"外币不能为空");
            scoreConverter =  scoreConverterService.selectOne(scoreConverter);
            if (scoreConverter == null) {
                throw new ConverterException(ExceptionEnum.CONVERTER_QUERY_EXCEPTION,"查询结果为空!");
            }
            return scoreConverter;
        }catch (IllegalArgumentException e) {
            logger.error("根据currency=>[" + scoreConverter.getExchangeCurrency() + "]查询兑换规则参数校验失败!",e);
            throw new ConverterException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        }catch (ConverterException e) {
            throw e;
        }catch (Exception e) {
            logger.error("根据currency=>[" + scoreConverter.getExchangeCurrency() + "]查询兑换规则失败!",e);
            throw new ConverterException(ExceptionEnum.CONVERTER_QUERY_EXCEPTION,"根据currency=>[" + scoreConverter.getExchangeCurrency() + "]查询兑换规则失败!");
        }
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public int deleteScoreConverter(ScoreConverter scoreConverter) {
        try {
            Assert.notNull(scoreConverter, "积分兑换规则不能为空");
            Assert.notNull(scoreConverter.getId(), "积分兑换规则id不能为空");
            Assert.isTrue(StringUtils.isNotEmpty(scoreConverter.getCreateBy()), "操作人不能为空");
            //更新配置规则
            scoreConverter.setIsDeleted(1);
            int result = scoreConverterService.updateByPrimaryKeySelective(scoreConverter);
            if (result != 1) {
                throw new ConverterException(ExceptionEnum.CONVERTER_UPDATE_EXCEPTION, "根据ID=>[" + scoreConverter.getId() + "]删除兑换规则失败!");
            }
            //TODO 插入流水
            insertScoreConverterFlow(scoreConverter);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("根据ID=>[" + scoreConverter.getId() + "]删除兑换规则参数校验失败!",e);
            throw new ConverterException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (ConverterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("根据ID=>[" + scoreConverter.getId() + "]删除兑换规则失败!",e);
            throw new ConverterException(ExceptionEnum.CONVERTER_UPDATE_EXCEPTION,"根据ID=>[" + scoreConverter.getId() + "]删除兑换规则失败!");
        }
    }

    @Override
    public ScoreConverter selectScoreConverterById(Long id) {
        try {
            Assert.notNull(id, "积分兑换规则id不能为空");
            ScoreConverter scoreConverter = new ScoreConverter();
            scoreConverter.setId(id);
            scoreConverter.setIsDeleted(0);
            scoreConverter = scoreConverterService.selectOne(scoreConverter);
            if (null == scoreConverter) {
                throw new ConverterException(ExceptionEnum.CONVERTER_QUERY_EXCEPTION,"查询结果为空!");
            }
            return scoreConverter;
        } catch (IllegalArgumentException e) {
            logger.error("根据ID=>[" + id + "]查询兑换规则参数校验失败!",e);
            throw new ConverterException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (ConverterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("根据ID=>[" + id + "]查询兑换规则失败!",e);
            throw new ConverterException(ExceptionEnum.CONVERTER_QUERY_EXCEPTION,"根据ID=>[" + id + "]查询兑换规则失败!");
        }
    }

    @Override
    public ScoreConverter getScoreConvertByCurrency(String currency) {
        try {
            Assert.isTrue(StringUtils.isNotEmpty(currency), "外币不能为空");
            ScoreConverter scoreConverter = new ScoreConverter();
            scoreConverter.setExchangeCurrency(currency);
            scoreConverter.setIsDeleted(0);
            scoreConverter = scoreConverterService.selectOne(scoreConverter);
            if (null == scoreConverter) {
                throw new ConverterException(ExceptionEnum.CONVERTER_QUERY_EXCEPTION, "查询结果为空!");
            }
            return scoreConverter;
        } catch (IllegalArgumentException e) {
            logger.error("根据currency=>[" + currency + "]查询兑换规则参数校验失败!",e);
            throw new ConverterException(ExceptionEnum.PARAM_CHECK_EXCEPTION,e.getMessage());
        } catch (ConverterException e) {
            throw e;
        } catch (Exception e) {
            logger.error("根据currency=>[" + currency + "]查询兑换规则失败!",e);
            throw new ConverterException(ExceptionEnum.CONVERTER_QUERY_EXCEPTION,"根据currency=>[" + currency + "]查询兑换规则失败!");
        }
    }

    private void validate(ScoreConverter scoreConverter){
        Assert.notNull(scoreConverter.getScore(), "积分兑换积分数量不能为空");
        Assert.notNull(scoreConverter.getAmount(), "积分兑换外币数量不能为空");
        if (ScoreCst.Direction.bothway.equals(scoreConverter.getDirection())) {
            Assert.notNull(scoreConverter.getPersonEverydayInLimit(), "每人每天可兑入限额不能为空");
            Assert.notNull(scoreConverter.getChannelEverydayInLimit(), "渠道每天可兑入限额不能为空");
            Assert.notNull(scoreConverter.getPersonEverydayOutLimit(), "每人每天可兑出限额不能为空");
            Assert.notNull(scoreConverter.getChannelEverydayOutLimit(), "渠道每天可兑出限额不能为空");
        }else if (ScoreCst.Direction.entranceOnly.equals(scoreConverter.getDirection())) {
            Assert.notNull(scoreConverter.getPersonEverydayInLimit(), "每人每天可兑入限额不能为空");
            Assert.notNull(scoreConverter.getChannelEverydayInLimit(), "渠道每天可兑入限额不能为空");
        }else if (ScoreCst.Direction.exitOnly.equals(scoreConverter.getDirection())) {
            Assert.notNull(scoreConverter.getPersonEverydayOutLimit(), "每人每天可兑出限额不能为空");
            Assert.notNull(scoreConverter.getChannelEverydayOutLimit(), "渠道每天可兑出限额不能为空");
        }
        Assert.isTrue(StringUtils.isNotEmpty(scoreConverter.getCreateBy()), "操作人不能为空");
    }


}
