package com.dazhi.config.ribbon;

import com.dazhi.nacosjar.registry.NacosDiscoveryProperties;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NacosServerList extends AbstractServerList<NacosServer> {
    private NacosDiscoveryProperties discoveryProperties;

    private String serviceId = "nacos-jar-test";

    /*public NacosServerList(){
        System.out.println("-----------------------NacosServerList is create");
    }*/
    public NacosServerList(NacosDiscoveryProperties discoveryProperties) {
        this.discoveryProperties = discoveryProperties;
        System.out.println("-----------------------NacosServerList is create with properties");
    }

    @Override
    public List<NacosServer> getInitialListOfServers() {
        System.out.println("------------------------在这里获取所有的getServersInit");
        return null;
    }

    @Override
    public List<NacosServer> getUpdatedListOfServers() {
        System.out.println("------------------------"+discoveryProperties.getServerAddr());
        System.out.println("------------------------在这里获取所有的service");
        return null;
    }

    public String getServiceId() {
        return serviceId;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        this.serviceId = iClientConfig.getClientName();
    }
}