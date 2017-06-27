package org.trc.util;

import com.alibaba.fastjson.JSONObject;

import javax.ws.rs.core.Response;

/**
 * Created by hzwzhen on 2017/6/7.
 */
public class CustomAck {
    public static final String ENCODING = "UTF-8";
    public static final String ERROR_CUSTOM_CODE = "99";

    public CustomAck() {
    }

    public static Response customError(String description) {
        return customError("99", description);
    }

    public static Response customError(String code, String description) {
        JSONObject json = new JSONObject();
        JSONObject error = new JSONObject();
        error.put("code", code);
        error.put("description", description);
        json.put("error", error);
        return Response.status(Response.Status.BAD_REQUEST).entity(json.toString()).type("application/json").encoding("UTF-8").build();
    }

    public static void main(String[] args) {
        JSONObject json = new JSONObject();
        JSONObject error = new JSONObject();
        error.put("code", "12");
        error.put("description", "123");
        json.put("error", error);
        System.out.println(json.toString());
    }
}
