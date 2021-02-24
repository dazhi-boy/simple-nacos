package com.dazhi.config.ribbon;

import com.dazhi.nacosjar.registry.Instance;
import com.netflix.loadbalancer.Server;

import java.util.Map;

public class NacosServer extends Server {

    private Instance instance;

    private Map<String, String> metadata;

    public NacosServer(final Instance instance) {
        super(instance.getIp(), instance.getPort());
        this.instance = instance;
        this.metadata = instance.getMetadata();
    }

    public NacosServer(String host, int port) {
        super(host, port);
    }

    public NacosServer(String scheme, String host, int port) {
        super(scheme, host, port);
    }

    public NacosServer(String id) {
        super(id);
    }

    public Instance getInstance() {
        return instance;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }
}
