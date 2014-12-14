package com.pingan.common.thread.pool.state;

import java.util.concurrent.ThreadPoolExecutor;
import org.apache.log4j.Logger;

/**
 * 线程池状态监控任务
 * 
 * @author leslie
 *
 */
public class ThreadPoolStateMonitor extends AbstractStateMonitor {

    private static final Logger LOG = Logger.getLogger(ThreadPoolStateMonitor.class);

    @Override
    protected void doExecute() {
        for (String poolName : threadPoolContext.poolNames()) {
            ThreadPoolExecutor threadPool = (ThreadPoolExecutor) threadPoolContext.getThreadPool(poolName);
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("thread pool : %s, queue [%d], active [%d], total [%d], completed [%d]", poolName, threadPool.getQueue().size(),
                        threadPool.getActiveCount(), threadPool.getTaskCount(), threadPool.getCompletedTaskCount()));
            }
        }
    }

}
