package com.pingan.common.thread.pool.state;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.log4j.Logger;

public class ThreadPoolStateMonitor extends AbstractStateMonitor {

    private static final Logger LOG = Logger.getLogger(ThreadPoolStateMonitor.class);
    private Map<String, ExecutorService> threadPools;

    public ThreadPoolStateMonitor(Map<String, ExecutorService> threadPools) {
        this.threadPools = threadPools;
    }

    @Override
    protected void doExecute() {
        for (Entry<String, ExecutorService> entry : threadPools.entrySet()) {
            ThreadPoolExecutor threadPool = (ThreadPoolExecutor) entry.getValue();
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("threadPool : %s, queue : %d, active : %d, total : %d, completed : %d", entry.getKey(), threadPool.getQueue().size(),
                        threadPool.getActiveCount(), threadPool.getTaskCount(), threadPool.getCompletedTaskCount()));
            }
        }
    }

}
