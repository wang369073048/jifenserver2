package org.trc.biz.score;

import org.trc.domain.pagehome.Banner;
import org.trc.domain.score.ScoreConverter;

/**
 * 积分兑换规则管理
 * Created by hzwzhen on 2017/6/14.
 */
public interface IScoreConverterBiz {

    /**
     * 保存
     * @param scoreConverter
     * @return
     * @throws Exception
     */
    int saveScoreConverter(ScoreConverter scoreConverter);

    /**
     * 更新
     * @param scoreConverter
     * @return
     * @throws Exception
     */
    int updateScoreConverter(ScoreConverter scoreConverter);


    /**
     * 查询单个对象
     * @param scoreConverter
     * @return
     */
    ScoreConverter selectOne(ScoreConverter scoreConverter);

    /**
     * 逻辑删除
     * @param scoreConverter
     * @return
     */
    int deleteScoreConverter(ScoreConverter scoreConverter);

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    ScoreConverter selectScoreConverterById(Long id);

    /**
     * 通过币种查询
     * @param currency
     * @return
     */
    ScoreConverter getScoreConvertByCurrency(String currency);
}
