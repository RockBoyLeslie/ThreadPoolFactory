package com.pingan.common.thread.pool;


import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.pingan.common.thread.pool.config.ThreadPoolConfig;
import com.pingan.common.thread.pool.config.ThreadPoolConfigParser;
import com.pingan.common.thread.pool.config.ThreadPoolFactoryConfig;
import com.pingan.common.thread.pool.state.AbstractStateMonitor;

/**
 * @function ThreadPool 工厂类， 负责根据用户配置的ThreadPool信息 创建相应的ThreadPool，
 * 并对各个ThreadPool的运行情况以及ThreadPool中线程的执行情况进行定时监控
 * 
 * @author leslie
 */
public final class ThreadPoolContext {

    private static final Logger LOG = Logger.getLogger(ThreadPoolContext.class);
    private static final String DEFAULT_POOL = "default";
    private static final int MONITOR_NUMBER = 5;
    private static Lock CONTEXT_LOCK = new ReentrantLock();
    private static ThreadPoolContext context;

    private ScheduledExecutorService monitorExecutor = Executors.newScheduledThreadPool(MONITOR_NUMBER);
    private Map<String, ExecutorService> threadPools = new ConcurrentHashMap<String, ExecutorService>();

    private ThreadPoolContext() {
        init();
    }

    public static ThreadPoolContext getContext() {
        if (context == null) {
            CONTEXT_LOCK.lock();
            if (context == null) {
                context = new ThreadPoolContext();
            }
            CONTEXT_LOCK.unlock();
        }

        return context;
    }

    public ExecutorService getThreadPool() {
        return getThreadPool(DEFAULT_POOL);
    }

    public ExecutorService getThreadPool(String poolName) {
        if (!threadPools.containsKey(poolName)) {
            throw new IllegalArgumentException(String.format("thread pool : %s not exist", poolName));
        }

        return threadPools.get(poolName);
    }

    public Set<String> poolNames() {
        return threadPools.keySet();
    }

    public void destroy() {
        if (monitorExecutor != null) {
            monitorExecutor.shutdown();
            if (LOG.isInfoEnabled()) {
                LOG.info("stop state monitors");
            }
        }

        for (Entry<String, ExecutorService> entry : threadPools.entrySet()) {
            entry.getValue().shutdown();
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("shutdown thread pool : %s", entry.getKey()));
            }
        }

        threadPools.clear();
    }

    private void init() {
        ThreadPoolFactoryConfig poolFactoryConfig = ThreadPoolConfigParser.getConfig();

        if (poolFactoryConfig != null) {
            initThreadPool(poolFactoryConfig);
            initMonitors(poolFactoryConfig);
        }
    }

    private void initThreadPool(ThreadPoolFactoryConfig poolFactoryConfig) {
        for (Entry<String, ThreadPoolConfig> entry : poolFactoryConfig.getPoolConfigMap().entrySet()) {
            ThreadPoolConfig config = entry.getValue();
            BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(config.getWorkQueueSize());
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(config.getCorePoolSize(), config.getMaxPoolSize(), config.getKeepAliveTime(),
                    TimeUnit.SECONDS, queue, new PooledThreadFactory(entry.getKey()));
            threadPools.put(entry.getKey(), threadPool);

            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("init thread pool : %s succeed ", config.toString()));
            }
        }
    }

    private void initMonitors(ThreadPoolFactoryConfig config) {
        Collection<AbstractStateMonitor> stateMonitors = config.getStateMonitorMap().values();
        for (AbstractStateMonitor stateMonitor : stateMonitors) {
            stateMonitor.setThreadPoolContext(this);
            monitorExecutor.scheduleAtFixedRate(stateMonitor, config.getInitialDelay(), config.getPeriod(), TimeUnit.SECONDS);

            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("start state monitor : %s succeed, initialDelay [%d], period [%d]", stateMonitor.getName(), config.getInitialDelay(),
                        config.getPeriod()));
            }
        }
    }
}
