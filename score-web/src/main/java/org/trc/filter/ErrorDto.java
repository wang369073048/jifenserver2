package org.trc.filter;

import com.alibaba.fastjson.annotation.JSONField;
import com.tairanchina.md.base.model.BaseDto;

/**
 *  Created by wangzhen
 */
public class ErrorDto extends BaseDto {
    /**
     *
     */
    private static final long serialVersionUID = -5172138375687562376L;

    private Error error;

    @JSONField(serialize = false)
    private transient int code;

    @JSONField(serialize = false)
    private transient String description;

    public ErrorDto() {
        this.error = new Error();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        error.setCode(code);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        error.setDescription(description);
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class Error {
        private int code;
        private String description;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}