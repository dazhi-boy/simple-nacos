package com.dazhi.nacosjar.registry;

import java.util.Properties;

public class NacosNamingService implements NamingService {
    private NamingProxy serverProxy;

    @Override
    public void registerInstance(String serviceName, String groupName, Instance instance) {
//        if (instance.isEphemeral()) {
            BeatInfo beatInfo = new BeatInfo();
//            beatInfo.setServiceName(NamingUtils.getGroupedName(serviceName, groupName));
            beatInfo.setIp(instance.getIp());
            beatInfo.setPort(instance.getPort());
            beatInfo.setCluster(instance.getClusterName());
//            beatInfo.setWeight(instance.getWeight());
//            beatInfo.setMetadata(instance.getMetadata());
            beatInfo.setScheduled(false);
//            beatInfo.setPeriod(instance.getInstanceHeartBeatInterval());

//            beatReactor.addBeatInfo(NamingUtils.getGroupedName(serviceName, groupName), beatInfo);
//        }

        serverProxy.registerService(getGroupedName(serviceName, groupName), groupName, instance);
    }

    public static String getGroupedName(String serviceName, String groupName) {
        StringBuilder resultGroupedName = new StringBuilder()
                .append(groupName)
                .append("@@")
                .append(serviceName);
        return resultGroupedName.toString().intern();
    }
}
