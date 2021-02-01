package com.dazhi.nacosjar.registry;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Map;

public class NacosRegistration implements Registration, ServiceInstance {
    private NacosDiscoveryProperties nacosDiscoveryProperties;
    private ApplicationContext context;

    public NacosRegistration(
                             ApplicationContext context) {
        System.out.println("_+_+_NacosRegistration");
//        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        this.context = context;
    }

    @PostConstruct
    public void init() {
        System.out.println("_+_+_+_+_+_+_+_+_+_+_+__+");
    }

    @Override
    public String getServiceId() {
//        return nacosDiscoveryProperties.getService();
        return "nacos-jar";
    }

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public Map<String, String> getMetadata() {
        return null;
    }
}
