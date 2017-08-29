package org.trc.filter;

import com.alibaba.fastjson.JSONObject;

import javax.naming.AuthenticationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/29
 */
@Provider
public class AuthenticationExeptionMapper implements ExceptionMapper<AuthenticationException> {
    @Override
    public Response toResponse(AuthenticationException e) {
        JSONObject result = new JSONObject();
        JSONObject error = new JSONObject();
        error.put("code",9);
        error.put("description",e.getMessage());
        result.put("error",error);
        return Response.status(Response.Status.UNAUTHORIZED).entity(result.toJSONString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
