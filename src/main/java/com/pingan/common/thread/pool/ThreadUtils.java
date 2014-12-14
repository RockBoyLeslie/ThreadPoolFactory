package com.pingan.common.thread.pool;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

/**
 * 线程辅助类
 * 
 * @author leslie
 *
 */
public final class ThreadUtils {

    private ThreadUtils() {

    }

    /**
     * 获取当前线程的root线程组
     * 
     * @return root 线程组
     */
    public static ThreadGroup getRootThreadGroup() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while (threadGroup.getParent() != null) {
            threadGroup = threadGroup.getParent();
        }

        return threadGroup;
    }

    /**
     * 获取目标线程组下的所有线程信息
     * 
     * @param threadGroup
     *            目前线程组
     * @return 以list形式返回目标线程组下所有线程
     */
    public static List<Thread> getGroupThreads(ThreadGroup threadGroup) {
        if (threadGroup == null) {
            return Collections.emptyList();
        }

        Thread[] threadArray = new Thread[threadGroup.activeCount() * 2];
        threadGroup.enumerate(threadArray);
        return Arrays.asList(threadArray);
    }

    /**
     * 根据线程组名称 ， 获取线程组
     * 
     * @param threadGroupNames
     *            线程组名称集合
     * @return Set<ThreadGroup> 线程组集合
     */
    public static Set<ThreadGroup> getMonitoredThreadGroups(Set<String> threadGroupNames) {
        if (CollectionUtils.isEmpty(threadGroupNames)) {
            return Collections.emptySet();
        }

        ThreadGroup rootThreadGroup = getRootThreadGroup();
        ThreadGroup[] subGroups = new ThreadGroup[rootThreadGroup.activeGroupCount() * 2];
        int subGroupCount = rootThreadGroup.enumerate(subGroups, false);

        Set<ThreadGroup> monitoredThreadGroups = new HashSet<ThreadGroup>(subGroupCount);
        for (int i = 0; i < subGroupCount; i++) {
            ThreadGroup subGroup = subGroups[i];
            if (threadGroupNames.contains(subGroup.getName())) {
                monitoredThreadGroups.add(subGroup);
            }
        }
        return monitoredThreadGroups;
    }

    /**
     * 获取指定线程池的线程组名称， 规则为pa-{poolName}-pool
     * 
     * @param poolName
     *            线程池名称
     * @return 线程组名称
     */
    public static String getThreadGroupName(String poolName) {
        return String.format("pa-%s-pool", poolName);
    }
}
