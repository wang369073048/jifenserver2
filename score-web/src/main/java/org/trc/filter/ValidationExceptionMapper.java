package org.trc.filter;

import com.alibaba.fastjson.JSON;
import org.trc.util.CommonConstants;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Set;

/**
 * 
 * Created by wangzhen
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException>
{
    @Override
    public Response toResponse(ConstraintViolationException exception)
    {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        ErrorDto errorDto = new ErrorDto();
        for (ConstraintViolation<?> vio : violations)
        {
            //所有没有通过Validation验证的, 都视为参数错误
            errorDto.setCode(CommonConstants.ErrorCode.ERROR_ILLEGAL_PARAMETER.getCode());
            errorDto.setDescription(vio.getMessage());
            break;
        }
        
        //参数错误统一返回400
        return Response.status(Status.BAD_REQUEST).entity(JSON.toJSON(errorDto).toString()).build();
    }
}
