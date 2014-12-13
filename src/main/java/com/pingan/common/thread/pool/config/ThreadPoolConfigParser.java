package com.pingan.common.thread.pool.config;

import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;

/**
 * 
 */
public final class ThreadPoolConfigParser {

    private static final Logger LOG = Logger.getLogger(ThreadPoolConfigParser.class);
    private static final String THREAD_POOL_CONFIG = "threadPool.xml";
    private static InputStream inputStream;

    public static ThreadPoolFactoryConfig getConfig() {
        try {
            return (ThreadPoolFactoryConfig) getDigester().parse(getInputStream());
        } catch (Exception e) {
            LOG.error("failed to parse thread pool configuration", e);
            return null;
        }
    }

    private static InputStream getInputStream() {
        if (inputStream == null) {
            synchronized (THREAD_POOL_CONFIG) {
                if (inputStream == null) {
                    inputStream = ThreadPoolConfigParser.class.getClassLoader().getResourceAsStream(THREAD_POOL_CONFIG);
                }
            }
        }

        return inputStream;
    }

    private static Digester getDigester() {
        Digester digester = new Digester();
        digester.setValidating(false);

        // parse thread pool factory node
        digester.addObjectCreate("pools", ThreadPoolFactoryConfig.class);
        digester.addSetProperties("pools/attribute");
        digester.addSetProperty("pools/attribute", "name", "value");

        // loop parse thread pool config node
        digester.addObjectCreate("*/pool", ThreadPoolConfig.class);
        digester.addSetProperties("*/pool");
        digester.addSetProperty("*/pool/attribute", "name", "value");
        digester.addSetNext("*/pool", "addThreadPoolConfig");

        return digester;
    }

    public static void main(String[] args) {
        ThreadPoolFactoryConfig config = ThreadPoolConfigParser.getConfig();

        System.out.println(config.isLogPoolState());
        System.out.println(config.isLogThreadState());
        for (ThreadPoolConfig poolConfig : config.getPoolConfigMap().values()) {
            System.out.println(String.format("%s, %s, %s, %s", poolConfig.getCorePoolSize(), poolConfig.getMaxPoolSize(), poolConfig.getKeepAliveTime(),
                    poolConfig.getWorkQueueSize()));
        }
    }

}
