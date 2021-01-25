package com.dazhi.nacosclient.naming.core;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Service {
    /**
     * service name.
     */
    private String name;

    private String namespaceId;

    private Map<String, Cluster> clusterMap = new HashMap<>();

    /**
     * Service group to classify services into different sets.
     */
    private String groupName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(String namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Map<String, Cluster> getClusterMap() {
        return clusterMap;
    }

    public void setClusterMap(Map<String, Cluster> clusterMap) {
        this.clusterMap = clusterMap;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void init() {
        for (Map.Entry<String, Cluster> entry : clusterMap.entrySet()) {
            entry.getValue().setService(this);
            entry.getValue().init();
        }
    }

    public List<Instance> allIPs() {
        List<Instance> result = new ArrayList<>();
        for (Map.Entry<String, Cluster> entry : clusterMap.entrySet()) {
            result.addAll(entry.getValue().allIPs());
        }

        return result;
    }

    public void onChange(String key, Instances instances) {
        /*for (Instance instance : instances.getInstanceList()) {

        }*/
        updateIPs(instances.getInstanceList(), key);
    }

    private void updateIPs(List<Instance> instanceList, String key) {
        Map<String, List<Instance>> ipMap = new HashMap<>(clusterMap.size());
        for (String clusterName : clusterMap.keySet()) {
            ipMap.put(clusterName, new ArrayList<>());
        }
        for (Instance instance : instanceList) {
            if (!clusterMap.containsKey(instance.getClusterName())) {
                Cluster cluster = new Cluster(instance.getClusterName(), this);
                cluster.init();
                getClusterMap().put(instance.getClusterName(), cluster);
            }
            List<Instance> clusterIPs = ipMap.get(instance.getClusterName());
            if (clusterIPs == null) {
                clusterIPs = new LinkedList<>();
                ipMap.put(instance.getClusterName(), clusterIPs);
            }
            clusterIPs.add(instance);
        }
        // 这里将IP更新一下
        for (Map.Entry<String, List<Instance>> entry : ipMap.entrySet()) {
            //make every ip mine
            List<Instance> entryIPs = entry.getValue();
            clusterMap.get(entry.getKey()).updateIps(entryIPs);
        }
//        getPushService().serviceChanged(this);
    }
}
