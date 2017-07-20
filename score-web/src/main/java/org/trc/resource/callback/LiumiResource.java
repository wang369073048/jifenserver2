package org.trc.resource.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.util.AppResult;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

import static org.trc.util.ResultUtil.createSucssAppResult;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/12
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.Liumi.ROOT)
//@TxAop
public class LiumiResource {

    private Logger logger = LoggerFactory.getLogger(LiumiResource.class);

    @Autowired
    private IAuthBiz authBiz;

    /**
     * 订单通知
     * @param containerRequestContext
     * @return
     * @throws IOException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(ScoreAdminConstants.Route.Liumi.ORDER_NOTICE)
    public AppResult orderNotice(@Context ContainerRequestContext containerRequestContext) throws IOException {
        logger.info("begin read");
        InputStream inStream = containerRequestContext.getEntityStream(); // 取HTTP请求流
        int size = containerRequestContext.getLength(); // 取HTTP请求流长度
        byte[] buffer = new byte[size]; // 用于缓存每次读取的数据
        byte[] in_b = new byte[size]; // 用于存放结果的数组
        int count = 0;
        int rbyte = 0;
        // 循环读取
        while (count < size) {
            rbyte = inStream.read(buffer); // 每次实际读取长度存于rbyte中 sflj
            for (int i = 0; i < rbyte; i++) {
                in_b[count + i] = buffer[i];
            }
            count += rbyte;
        }
        logger.info("result:" + new String(in_b, 0, in_b.length));
        return createSucssAppResult("订单通知成功!", "");
    }
}
