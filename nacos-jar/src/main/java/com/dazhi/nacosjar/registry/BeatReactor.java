package com.dazhi.nacosjar.registry;

import com.alibaba.fastjson.JSONObject;
import com.dazhi.nacosjar.common.HttpMethod;
import com.alibaba.fastjson.JSON;
import com.dazhi.nacosjar.naming.CommonParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class BeatReactor {
    public static final int DEFAULT_CLIENT_BEAT_THREAD_COUNT = Runtime.getRuntime()
            .availableProcessors() > 1 ? Runtime.getRuntime().availableProcessors() / 2
            : 1;

    private boolean lightBeatEnabled = false;

    private ScheduledExecutorService executorService;

    public BeatReactor(){
        executorService = new ScheduledThreadPoolExecutor(DEFAULT_CLIENT_BEAT_THREAD_COUNT, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("com.alibaba.nacos.naming.beat.sender");
                return thread;
            }
        });
    }

    public void addBeatInfo(String serviceName, BeatInfo beatInfo) {
        executorService.schedule(new BeatTask(beatInfo), beatInfo.getPeriod(), TimeUnit.MILLISECONDS);
    }

    class BeatTask implements Runnable {

        BeatInfo beatInfo;
        NamingProxy namingProxy = new NamingProxy();

        public BeatTask(BeatInfo beatInfo) {
            this.beatInfo = beatInfo;
        }

        @Override
        public void run() {
            if (beatInfo.isStopped()) {
                return;
            }
            long nextTime = beatInfo.getPeriod();
                JSONObject result = sendBeat(beatInfo, BeatReactor.this.lightBeatEnabled);
                long interval = result.getIntValue("clientBeatInterval");
                boolean lightBeatEnabled = false;
                if (result.containsKey(CommonParams.LIGHT_BEAT_ENABLED)) {
                    lightBeatEnabled = result.getBooleanValue(CommonParams.LIGHT_BEAT_ENABLED);
                }
                BeatReactor.this.lightBeatEnabled = lightBeatEnabled;
                if (interval > 0) {
                    nextTime = interval;
                }
                int code = 10200;
                if (result.containsKey(CommonParams.CODE)) {
                    code = result.getIntValue(CommonParams.CODE);
                }
                if (code == 20404) {
                    Instance instance = new Instance();
                    instance.setPort(beatInfo.getPort());
                    instance.setIp(beatInfo.getIp());
//                    instance.setWeight(beatInfo.getWeight());
                    instance.setMetadata(beatInfo.getMetadata());
                    instance.setClusterName(beatInfo.getCluster());
                    instance.setServiceName(beatInfo.getServiceName());
                    instance.setInstanceId(instance.getInstanceId());
//                    instance.setEphemeral(true);
                    try {
                        namingProxy.registerService(beatInfo.getServiceName(),
                                "", instance);
                    } catch (Exception ignore) {
                    }
                }
            executorService.schedule(new BeatTask(beatInfo), nextTime, TimeUnit.MILLISECONDS);
        }

        private JSONObject sendBeat(BeatInfo beatInfo, boolean lightBeatEnabled) {
            System.out.println("sendBeat");
            String body = org.apache.commons.lang3.StringUtils.EMPTY;
            if (!lightBeatEnabled) {
                try {
                    body = "beat=" + URLEncoder.encode(JSON.toJSONString(beatInfo), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    System.out.println("encode beatInfo error");
                }
            }
            //这里发http请求将服务注册到service
            NamingProxy namingProxy = new NamingProxy();
            Map<String, String> params = new HashMap();
            params.put("serviceName", "nacos-jar");
            params.put("ip", "127.0.0.1");
            params.put("port", "8868");
            String result = namingProxy.callServer("/nacos/v1/ns/instance/beat", params, body, "http://127.0.0.1:8848", HttpMethod.PUT);
            System.out.println(result);
            return JSON.parseObject(result);
        }
    }
}
