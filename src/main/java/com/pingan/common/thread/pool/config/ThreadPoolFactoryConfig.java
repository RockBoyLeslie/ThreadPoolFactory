package com.pingan.common.thread.pool.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ThreadPoolFactoryConfig {

    private static final Logger LOG = Logger.getLogger(ThreadPoolFactoryConfig.class);
    
    private Map<String, ThreadPoolConfig> poolConfigMap = new HashMap<String, ThreadPoolConfig>();
    private boolean logPoolState = false;
    private boolean logThreadState = false;

    public ThreadPoolFactoryConfig() {

    }

    public boolean isLogPoolState() {
        return logPoolState;
    }

    public void setLogPoolState(boolean logPoolState) {
        this.logPoolState = logPoolState;
    }

    public boolean isLogThreadState() {
        return logThreadState;
    }

    public void setLogThreadState(boolean logThreadState) {
        this.logThreadState = logThreadState;
    }

    public Map<String, ThreadPoolConfig> getPoolConfigMap() {
        return poolConfigMap;
    }
    
    public void addThreadPoolConfig(ThreadPoolConfig threadPoolConfig) {
        if (poolConfigMap.containsKey(threadPoolConfig.getName())) {
            LOG.warn(String.format("thread pool name : %s repeat", threadPoolConfig.getName()));
            return;
        }
        poolConfigMap.put(threadPoolConfig.getName(), threadPoolConfig);
    }
}
