package org.trc.handler;

import com.alibaba.fastjson.JSONObject;

import javax.annotation.Priority;
import javax.naming.AuthenticationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by huyan on 2016/11/21
 */
@Priority(Integer.MIN_VALUE + 5)
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
