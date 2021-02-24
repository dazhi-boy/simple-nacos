package com.dazhi.config.ribbon;

import com.netflix.loadbalancer.Server;
import org.springframework.cloud.netflix.ribbon.DefaultServerIntrospector;

import java.util.Map;

public class NacosServerIntrospector extends DefaultServerIntrospector {
    @Override
    public Map<String, String> getMetadata(Server server) {
        if (server instanceof NacosServer) {
            return ((NacosServer) server).getMetadata();
        }
        return super.getMetadata(server);
    }

    @Override
    public boolean isSecure(Server server) {
        if (server instanceof NacosServer) {
            return Boolean.valueOf(((NacosServer) server).getMetadata().get("secure"));
        }

        return super.isSecure(server);
    }
}
