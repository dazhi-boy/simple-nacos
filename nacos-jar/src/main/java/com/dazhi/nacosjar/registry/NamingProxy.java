package com.dazhi.nacosjar.registry;

import com.dazhi.nacosjar.common.HttpClient;
import com.dazhi.nacosjar.common.HttpHeaderConsts;
import com.dazhi.nacosjar.common.UtilAndComs;
import com.dazhi.nacosjar.common.UuidUtils;
import com.dazhi.nacosjar.naming.CommonParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.net.HttpURLConnection;
import java.util.*;

public class NamingProxy {
    private static final int DEFAULT_SERVER_PORT = 8848;
    private int serverPort = DEFAULT_SERVER_PORT;

    private String namespaceId;

    private List<String> serverList;

    private String nacosDomain;

    private List<String> serversFromEndpoint = new ArrayList<String>();


    public void registerService(String serviceName, String groupName, Instance instance) {
        final Map<String, String> params = new HashMap<String, String>(9);
        params.put(CommonParams.NAMESPACE_ID, namespaceId);
        params.put(CommonParams.SERVICE_NAME, serviceName);
        params.put(CommonParams.GROUP_NAME, groupName);
        params.put(CommonParams.CLUSTER_NAME, instance.getClusterName());
        params.put("ip", instance.getIp());
        params.put("port", String.valueOf(instance.getPort()));
//        params.put("weight", String.valueOf(instance.getWeight()));
//        params.put("enable", String.valueOf(instance.isEnabled()));
//        params.put("healthy", String.valueOf(instance.isHealthy()));
//        params.put("ephemeral", String.valueOf(instance.isEphemeral()));
//        params.put("metadata", JSON.toJSONString(instance.getMetadata()));

        reqAPI(UtilAndComs.NACOS_URL_INSTANCE, params, "DELETE");
    }

    public String reqAPI(String api, Map<String, String> params, String method) {
        return reqAPI(api, params, "", method);
    }

    public String reqAPI(String api, Map<String, String> params, String body, String method) {
        return reqAPI(api, params, body, getServerList(), method);
    }

    private List<String> getServerList() {
        List<String> snapshot = serversFromEndpoint;
        if (!CollectionUtils.isEmpty(serverList)) {
            snapshot = serverList;
        }
        return snapshot;
    }

    public String reqAPI(String api, Map<String, String> params, String body, List<String> servers, String method) {

        params.put(CommonParams.NAMESPACE_ID, getNamespaceId());

        if (CollectionUtils.isEmpty(servers) && StringUtils.isEmpty(nacosDomain)) {
        }


        if (servers != null && !servers.isEmpty()) {

            Random random = new Random(System.currentTimeMillis());
            int index = random.nextInt(servers.size());

            for (int i = 0; i < servers.size(); i++) {
                String server = servers.get(index);
                    return callServer(api, params, body, server, method);
//                index = (index + 1) % servers.size();
            }
        }

        if (StringUtils.isNotBlank(nacosDomain)) {
            for (int i = 0; i < UtilAndComs.REQUEST_DOMAIN_RETRY_COUNT; i++) {
                    return callServer(api, params, body, nacosDomain, method);
            }
        }
        return null;
    }

    public String getNamespaceId() {
        return namespaceId;
    }

    public String callServer(String api, Map<String, String> params, String body, String curServer, String method) {
        long start = System.currentTimeMillis();
        long end = 0;
//        injectSecurityInfo(params);
        List<String> headers = builderHeaders();

        String url;
        if (curServer.startsWith(UtilAndComs.HTTPS) || curServer.startsWith(UtilAndComs.HTTP)) {
            url = curServer + api;
        } else {
            if (!curServer.contains(UtilAndComs.SERVER_ADDR_IP_SPLITER)) {
                curServer = curServer + UtilAndComs.SERVER_ADDR_IP_SPLITER + serverPort;
            }
            url = HttpClient.getPrefix() + curServer + api;
        }

        HttpClient.HttpResult result = HttpClient.request(url, headers, params, body, UtilAndComs.ENCODING, method);
        end = System.currentTimeMillis();

//        MetricsMonitor.getNamingRequestMonitor(method, url, String.valueOf(result.code))
//                .observe(end - start);

        if (HttpURLConnection.HTTP_OK == result.code) {
            return result.content;
        }

        if (HttpURLConnection.HTTP_NOT_MODIFIED == result.code) {
            return StringUtils.EMPTY;
        }
        return null;
    }

    public List<String> builderHeaders() {
        List<String> headers = Arrays.asList(
                HttpHeaderConsts.CLIENT_VERSION_HEADER, "v1",
                HttpHeaderConsts.USER_AGENT_HEADER, "v1",
                "Accept-Encoding", "gzip,deflate,sdch",
                "Connection", "Keep-Alive",
                "RequestId", UuidUtils.generateUuid(), "Request-Module", "Naming");
        return headers;
    }
}
