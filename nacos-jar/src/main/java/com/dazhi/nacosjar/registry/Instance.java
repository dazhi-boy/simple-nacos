package com.dazhi.nacosjar.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Instance {
    /**
     * unique id of this instance.
     */
    private String instanceId;

    /**
     * instance ip.
     */
    private String ip;

    /**
     * instance port.
     */
    private int port;

    private String app;
    /**
     * cluster information of instance.
     */
    private String clusterName;

    /**
     * Service information of instance.
     */
    private String serviceName;

    private Map<String, String> metadata = new HashMap<String, String>();

    public String generateInstanceId() {
        return getIp() + "#" + getPort() + "#" + getClusterName() + "#" + getServiceName();
    }

    public String generateInstanceId(Set<String> currentInstanceIds) {
        return generateInstanceId();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String toIpAddr() {
        return getIp() + ":" + getPort();
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
