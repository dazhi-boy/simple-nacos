package com.dazhi.nacosclient.naming.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Component
public class ServiceManager {
    public static String SERVICE_INFO_SPLITER = "@@";
    public static final String NAMESPACE_KEY_CONNECTOR = "##";

    private static final String EPHEMERAL_KEY_PREFIX = "ephemeral.";

    public static final String INSTANCE_LIST_KEY_PREFIX = "com.alibaba.nacos.naming.iplist.";
    /**
     * Map(namespace, Map(group::serviceName, Service)).
     */
    private final Map<String, Map<String, Service>> serviceMap = new ConcurrentHashMap<>();

    private final Object putServiceLock = new Object();

    public Map<String, Map<String, Service>> getServiceMap() {
        return serviceMap;
    }

    public void registerInstance(String namespaceId, String serviceName, Instance instance) {
        //判断service是否存在，没有，创建
        createEmptyService(namespaceId, serviceName, null);
        // 取service
        Service service = getService(namespaceId, serviceName);
        //将service放进去
        addInstance(namespaceId, serviceName, instance);
    }

    public void addInstance(String namespaceId, String serviceName, Instance... ips){
        String key = INSTANCE_LIST_KEY_PREFIX + EPHEMERAL_KEY_PREFIX + namespaceId + NAMESPACE_KEY_CONNECTOR + serviceName;

        Service service = getService(namespaceId, serviceName);

        synchronized (service) {
            //同一个servicename可能有多个服务，给他放到一个list中
            List<Instance> instanceList = updateIpAddresses(service, "add", ips);

            Instances instances = new Instances();
            instances.setInstanceList(instanceList);
            //将instancesput进去
//            onPut(key, instances);
            service.onChange(key,instances);
        }
    }

    private List<Instance> updateIpAddresses(Service service, String action, Instance[] ips) {
        List<Instance> currentIPs = service.allIPs();
        Map<String, Instance> currentInstances = new HashMap<>(currentIPs.size());
        Set<String> currentInstanceIds = new HashSet();
        for (Instance instance : currentIPs) {
            currentInstances.put(instance.toIpAddr(), instance);
            currentInstanceIds.add(instance.getInstanceId());
        }

        Map<String, Instance> instanceMap;
        instanceMap = new HashMap<>(ips.length);

        for (Instance instance : ips) {
            if (!service.getClusterMap().containsKey(instance.getClusterName())) {
                Cluster cluster = new Cluster(instance.getClusterName(), service);
                cluster.init();
                service.getClusterMap().put(instance.getClusterName(), cluster);
            }

            if ("remove".equals(action)) {
                instanceMap.remove(instance.generateInstanceId());
            } else {
                Instance oldInstance = instanceMap.get(instance.generateInstanceId());
                if (oldInstance != null) {
                    instance.setInstanceId(oldInstance.getInstanceId());
                } else {
                    instance.setInstanceId(instance.generateInstanceId(currentInstanceIds));
                }
            }
            instanceMap.put(instance.generateInstanceId(), instance);
        }

        return new ArrayList<>(instanceMap.values());
    }

    public Service getService(String namespaceId, String serviceName) {
        if (serviceMap.get(namespaceId) == null) {
            return null;
        }
        return chooseServiceMap(namespaceId).get(serviceName);
    }

    private Map<String, Service> chooseServiceMap(String namespaceId) {
        return serviceMap.get(namespaceId);
    }

    private void createEmptyService(String namespaceId, String serviceName, Cluster cluster) {
        Service service = getService(namespaceId, serviceName);
        if (service == null) {
            service = new Service();
            service.setName(serviceName);
            service.setNamespaceId(namespaceId);
            service.setGroupName(getGroupName(serviceName));
            // now validate the service. if failed, exception will be thrown
            if (cluster != null) {
                cluster.setService(service);
                service.getClusterMap().put(cluster.getName(), cluster);
            }
            putServiceAndInit(service);
        }
    }

    private void putServiceAndInit(Service service) {
        putService(service);
        service.init();
    }

    private void putService(Service service) {
        if (!serviceMap.containsKey(service.getNamespaceId())) {
            synchronized (putServiceLock) {
                if (!serviceMap.containsKey(service.getNamespaceId())) {
                    serviceMap.put(service.getNamespaceId(), new ConcurrentSkipListMap<>());
                }
            }
        }
        serviceMap.get(service.getNamespaceId()).put(service.getName(), service);
    }

    public static String getGroupName(final String serviceNameWithGroup) {
        if (StringUtils.isBlank(serviceNameWithGroup)) {
            return StringUtils.EMPTY;
        }
        if (!serviceNameWithGroup.contains(SERVICE_INFO_SPLITER)) {
            return "DEFAULT_GROUP";
        }
        return serviceNameWithGroup.split(SERVICE_INFO_SPLITER)[0];
    }
}
