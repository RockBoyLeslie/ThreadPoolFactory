package com.pingan.common.thread.pool.config;

import com.pingan.common.thread.pool.state.AbstractStateMonitor;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class ThreadPoolFactoryConfig {

    private static final Logger LOG = Logger.getLogger(ThreadPoolFactoryConfig.class);

    // 线程池配置
    private Map<String, ThreadPoolConfig> poolConfigMap = new HashMap<String, ThreadPoolConfig>();

    // 线程池监控, 线程监控配置
    private Map<String, AbstractStateMonitor> stateMonitorMap = new HashMap<String, AbstractStateMonitor>();
    // 监控任务初始执行延迟时间， 默认10s
    private long initialDelay = 10;
    // 监控任务执行间隔时间， 默认60s
    private long period = 60;

    public ThreadPoolFactoryConfig() {

    }

    public void addThreadPoolConfig(ThreadPoolConfig threadPoolConfig) {
        if (poolConfigMap.containsKey(threadPoolConfig.getName())) {
            LOG.warn(String.format("thread pool name : %s repeat", threadPoolConfig.getName()));
            return;
        }
        poolConfigMap.put(threadPoolConfig.getName(), threadPoolConfig);
    }

    public void addStateMonitor(AbstractStateMonitor stateMonitor) {
        if (stateMonitorMap.containsKey(stateMonitor.getName())) {
            LOG.warn(String.format("state monitor name : %s repeat", stateMonitor.getName()));
            return;
        }
        stateMonitorMap.put(stateMonitor.getName(), stateMonitor);
    }

    public Map<String, ThreadPoolConfig> getPoolConfigMap() {
        return poolConfigMap;
    }

    public Map<String, AbstractStateMonitor> getStateMonitorMap() {
        return stateMonitorMap;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}
