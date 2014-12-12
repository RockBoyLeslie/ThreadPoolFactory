package com.pingan.common.thread.pool.config;

import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;

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

        digester.addObjectCreate("pools", ThreadPoolFactoryConfig.class);
        digester.addSetProperties("pools/attribute", "logPoolState", "logPoolState");
        digester.addSetProperties("pools/attribute", "logThreadState", "logThreadState");

        return digester;
    }

    public static void main(String[] args) {
        ThreadPoolFactoryConfig config = ThreadPoolConfigParser.getConfig();

        System.out.println(config.isLogPoolState());
    }

}
