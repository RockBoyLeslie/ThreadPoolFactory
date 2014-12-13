package com.pingan.common.thread.pool;


import com.pingan.common.thread.pool.config.ThreadPoolConfig;
import com.pingan.common.thread.pool.config.ThreadPoolConfigParser;
import com.pingan.common.thread.pool.config.ThreadPoolFactoryConfig;
import com.pingan.common.thread.pool.state.AbstractStateMonitor;
import com.pingan.common.thread.pool.state.ThreadPoolStateMonitor;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public final class ThreadPoolContext {

    private static final Logger LOG = Logger.getLogger(ThreadPoolContext.class);
    private static final String DEFAULT_POOL = "default";
    private static final long MONITOR_INTERVAL = 60;
    private static ThreadPoolContext context;
    private static Object contextLock = new Object();

    private ScheduledExecutorService monitorExecutor = Executors.newScheduledThreadPool(2);
    private Map<String, ExecutorService> threadPools = new ConcurrentHashMap<String, ExecutorService>();

    private ThreadPoolContext() {
        init();
    }

    public static ThreadPoolContext getContext() {
        if (context == null) {
            synchronized (contextLock) {
                if (context == null) {
                    context = new ThreadPoolContext();
                }
            }
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
                    TimeUnit.SECONDS, queue);
            threadPools.put(entry.getKey(), threadPool);

            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("init thread pool : %s succeed ", config.toString()));
            }
        }
    }

    private void initMonitors(ThreadPoolFactoryConfig poolFactoryConfig) {
        if (poolFactoryConfig.isLogPoolState()) {
            AbstractStateMonitor monitor = new ThreadPoolStateMonitor(threadPools);
            monitorExecutor.scheduleAtFixedRate(monitor, 10, MONITOR_INTERVAL, TimeUnit.SECONDS);

            if (LOG.isInfoEnabled()) {
                LOG.info("start thread pool state monitor");
            }
        }

        if (poolFactoryConfig.isLogThreadState()) {

        }
    }
}
