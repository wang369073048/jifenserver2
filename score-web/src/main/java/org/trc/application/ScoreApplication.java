package org.trc.application;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.trc.filter.AuthorizationFilter;

/**
 * Created by hzwzhen on 2017/6/7.
 */
public class ScoreApplication extends ResourceConfig{

    public ScoreApplication(){
        register(AuthorizationFilter.class);
        register(JacksonJsonProvider.class);
        register(MultiPartFeature.class);
        packages("org.trc.resource");
        //property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        //property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
    }
}
