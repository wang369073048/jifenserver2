package org.trc.domain.query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by george on 2017/7/23.
 */
public class UsableActivityQuery implements Serializable{

    /**
     * 受限制的奖品id
     */
    private List<Long> list;

    /**
     * 抽奖活动id
     */
    private Long luckyDrawId;


    public List<Long> getList() {
        return list;
    }

    public void setList(List<Long> list) {
        this.list = list;
    }

    public Long getLuckyDrawId() {
        return luckyDrawId;
    }

    public void setLuckyDrawId(Long luckyDrawId) {
        this.luckyDrawId = luckyDrawId;
    }
}
