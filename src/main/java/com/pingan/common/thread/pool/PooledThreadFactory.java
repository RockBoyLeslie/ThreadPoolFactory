package com.pingan.common.thread.pool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @function 公共线程池用到的线程工厂， 将ThreadPool的线程组挂在root线程组下面，命名规则为  pa-{poolName}-pool
 * 线程组中的线程命名规则为pa-{poolName}-pool-{index}
 * 
 * @author leslie
 *
 */
public class PooledThreadFactory implements ThreadFactory {

    private ThreadGroup threadGroup;
    private AtomicInteger number = new AtomicInteger(0);
    
    public PooledThreadFactory(String poolName) {
        ThreadGroup rootThreadGroup = ThreadUtils.getRootThreadGroup();
        threadGroup = new ThreadGroup(rootThreadGroup, ThreadUtils.getThreadGroupName(poolName));
    }
    
    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(threadGroup, runnable);
        thread.setName(String.format("%s-%d", threadGroup.getName(), number.incrementAndGet()));
        
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }

}
