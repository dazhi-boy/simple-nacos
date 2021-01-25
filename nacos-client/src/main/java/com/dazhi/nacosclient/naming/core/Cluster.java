package com.dazhi.nacosclient.naming.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    public Cluster(String clusterName, Service service) {
        this.setName(clusterName);
        this.service = service;
    }

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

    public Collection<? extends Instance> allIPs() {
        List<Instance> allInstances = new ArrayList<>();
        allInstances.addAll(ephemeralInstances);
        return allInstances;
    }
    // 更新实例列表
    public void updateIps(List<Instance> ips) {
        Set<Instance> toUpdateInstances = ephemeralInstances;
        HashMap<String, Instance> oldIpMap = new HashMap<>(toUpdateInstances.size());

        for (Instance ip : toUpdateInstances) {
            oldIpMap.put(ip.generateInstanceId(), ip);
        }
        // 就是将旧的和新的去重后放一块
        List<Instance> updatedIPs = updatedIps(ips, oldIpMap.values());

        List<Instance> newIPs = subtract(ips, oldIpMap.values());
        toUpdateInstances = new HashSet<>(ips);
        //这里将整理好的保存到变量中
        ephemeralInstances = toUpdateInstances;
    }

    private List<Instance> updatedIps(Collection<Instance> newInstance, Collection<Instance> oldInstance) {

        List<Instance> intersects = (List<Instance>) CollectionUtils.intersection(newInstance, oldInstance);
        Map<String, Instance> stringIpAddressMap = new ConcurrentHashMap<>(intersects.size());
        // 去重
        for (Instance instance : intersects) {
            stringIpAddressMap.put(instance.getIp() + ":" + instance.getPort(), instance);
        }

        Map<String, Integer> intersectMap = new ConcurrentHashMap<>(newInstance.size() + oldInstance.size());
        Map<String, Instance> updatedInstancesMap = new ConcurrentHashMap<>(newInstance.size());
        Map<String, Instance> newInstancesMap = new ConcurrentHashMap<>(newInstance.size());

        for (Instance instance : oldInstance) {
            if (stringIpAddressMap.containsKey(instance.getIp() + ":" + instance.getPort())) {
                intersectMap.put(instance.toString(), 1);
            }
        }

        for (Instance instance : newInstance) {
            if (stringIpAddressMap.containsKey(instance.getIp() + ":" + instance.getPort())) {

                if (intersectMap.containsKey(instance.toString())) {
                    intersectMap.put(instance.toString(), 2);
                } else {
                    intersectMap.put(instance.toString(), 1);
                }
            }

            newInstancesMap.put(instance.toString(), instance);

        }

        for (Map.Entry<String, Integer> entry : intersectMap.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            if (value == 1) {
                if (newInstancesMap.containsKey(key)) {
                    updatedInstancesMap.put(key, newInstancesMap.get(key));
                }
            }
        }

        return new ArrayList<>(updatedInstancesMap.values());
    }

    private List<Instance> subtract(Collection<Instance> oldIp, Collection<Instance> ips) {
        Map<String, Instance> ipsMap = new HashMap<>(ips.size());
        for (Instance instance : ips) {
            ipsMap.put(instance.getIp() + ":" + instance.getPort(), instance);
        }

        List<Instance> instanceResult = new ArrayList<>();

        for (Instance instance : oldIp) {
            if (!ipsMap.containsKey(instance.getIp() + ":" + instance.getPort())) {
                instanceResult.add(instance);
            }
        }
        return instanceResult;
    }
}
