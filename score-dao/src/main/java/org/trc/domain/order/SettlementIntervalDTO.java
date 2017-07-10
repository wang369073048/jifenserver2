package org.trc.domain.order;



import org.apache.commons.lang3.StringUtils;
import org.trc.util.DateUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by george on 2017/3/17.
 */
public class SettlementIntervalDTO implements Serializable{

    private String startAccountDay;

    private String endAccountDay;

    public String getStartAccountDay() {
        return startAccountDay;
    }

    public void setStartAccountDay(String startAccountDay) {
        this.startAccountDay = startAccountDay;
    }

    public Date getStartTime() {
        if(StringUtils.isNotBlank(startAccountDay)){
                Date startTime = DateUtils.parseDate(startAccountDay);
                return startTime;

        }
        return null;
    }

    public String getEndAccountDay() {
        return endAccountDay;
    }

    public void setEndAccountDay(String endAccountDay) {
        this.endAccountDay = endAccountDay;
    }

    public Date getEndTime() {
        if(StringUtils.isNotBlank(endAccountDay)){
            Date endTime = DateUtils.parseDate(endAccountDay);
            endTime = DateUtils.addDays(endTime,1);
            System.out.println(endTime);
            return endTime;
        }
        return null;
    }

}
