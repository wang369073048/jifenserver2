package org.trc.filter;

import org.springframework.stereotype.Component;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

/**
 * Created by hzwzhen on 2017/6/27.
 */
@Component
public class AuthorizationFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty("userId", "201512040929176188868d2365cd444ca833046f944178d97");
    }
}
