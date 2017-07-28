package org.trc.domain.query;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by george on 2017/6/1.
 */
public class DateQuery implements Serializable{

    private Date operateTimeMin;

    private Date operateTimeMax;

    public Date getOperateTimeMin() {
        return operateTimeMin;
    }

    public void setOperateTimeMin(Date operateTimeMin) {
        this.operateTimeMin = operateTimeMin;
    }

    public Date getOperateTimeMax() {
        return operateTimeMax;
    }

    public void setOperateTimeMax(Date operateTimeMax) {
        this.operateTimeMax = operateTimeMax;
    }
}
