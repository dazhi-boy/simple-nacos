package com.dazhi.nacosjar.registry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.cloud.nacos.discovery")
public class NacosDiscoveryProperties {
    public NacosDiscoveryProperties(){}

    @Value("spring.application.name")
    private String service;

    private int port = -1;

    private static NamingService namingService;

    private String group = "DEFAULT_GROUP";

    private String clusterName = "DEFAULT";

    public String getService() {
        return service;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public NamingService namingServiceInstance() {

        if (null != namingService) {
            return namingService;
        }

        try {
//            namingService = NacosFactory.createNamingService(getNacosProperties());
        }
        catch (Exception e) {
            return null;
        }
        return namingService;
    }
}
