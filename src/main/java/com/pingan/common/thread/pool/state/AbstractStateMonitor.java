package com.pingan.common.thread.pool.state;

import com.pingan.common.thread.pool.ThreadPoolContext;
import org.apache.log4j.Logger;

/**
 * @function 线程状态监控abstract类， 各个实现类需override该类的doExecute 方法进行具体的任务监控
 * 
 * @author leslie
 *
 */
public abstract class AbstractStateMonitor implements Runnable {

    private static final Logger LOG = Logger.getLogger(AbstractStateMonitor.class);
    protected ThreadPoolContext threadPoolContext;
    private String name;

    @Override
    public void run() {
        if (threadPoolContext == null) {
            LOG.warn("thread pool context not initialized");
            return;
        }

        doExecute();
    }

    public void setThreadPoolContext(ThreadPoolContext threadPoolContext) {
        this.threadPoolContext = threadPoolContext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected abstract void doExecute();

}
