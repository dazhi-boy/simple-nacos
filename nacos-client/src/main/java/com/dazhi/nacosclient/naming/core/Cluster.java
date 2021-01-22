package com.dazhi.nacosclient.naming.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

public class Cluster {
    /**
     * Name of belonging service.
     */
    private String serviceName;

    @JsonIgnore
    private Service service;

    /**
     * Name of cluster.
     */
    private String name;

    @JsonIgnore
    private Set<Instance> ephemeralInstances = new HashSet<>();

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Instance> getEphemeralInstances() {
        return ephemeralInstances;
    }

    public void setEphemeralInstances(Set<Instance> ephemeralInstances) {
        this.ephemeralInstances = ephemeralInstances;
    }

    public void init() {
        //在这里进行健康监测
    }
}
