package com.pingan.common.thread.pool.config;

import org.apache.commons.lang.StringUtils;

public class ThreadPoolConfig {

    private String name;

    private int corePoolSize;

    private int maxPoolSize;

    private int keepAliveTime;

    private int workQueueSize;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public int getWorkQueueSize() {
        return workQueueSize;
    }

    public void setWorkQueueSize(int workQueueSize) {
        this.workQueueSize = workQueueSize;
    }

    @Override
    public String toString() {
        return "ThreadPoolConfig [name=" + name + ", corePoolSize=" + corePoolSize + ", maxPoolSize=" + maxPoolSize + ", keepAliveTime=" + keepAliveTime
                + ", workQueueSize=" + workQueueSize + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        ThreadPoolConfig other = (ThreadPoolConfig) obj;
        return StringUtils.equals(name, other.name);
    }
}
