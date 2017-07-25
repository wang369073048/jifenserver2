package org.trc.domain.dto;

import org.trc.domain.score.ScoreChange;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/25
 */
public class ScoreChangeDTO extends ScoreChange{

    public String userPhone;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
