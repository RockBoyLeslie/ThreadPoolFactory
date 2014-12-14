package com.pingan.common.thread.pool.state;

import com.pingan.common.thread.pool.ThreadUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
/**
 * @function 线程状态监控类， 继承至AbstractStateMonitor 并完成主任务方法doExecute
 * 该监控类负责获取thread pool context中创建的ThreadGroup， 并计算每个ThreadGroup中所有线程的状态
 * 包括 new, runnable, blocked, waiting, timedWaiting, terminated
 * 
 * @author leslie
 * 
 */
public class ThreadStateMonitor extends AbstractStateMonitor {

    private static final Logger LOG = Logger.getLogger(ThreadStateMonitor.class);

    @Override
    protected void doExecute() {
        Set<ThreadGroup> threadGroups = getMonioredThreadGroups();
        for (ThreadGroup threadGroup : threadGroups) {
            monitorThreadGroup(threadGroup);
        }
    }

    private Set<ThreadGroup> getMonioredThreadGroups() {
        Set<String> threadGroupNames = new HashSet<String>(threadPoolContext.poolNames().size());
        for (String poolName : threadPoolContext.poolNames()) {
            threadGroupNames.add(ThreadUtils.getThreadGroupName(poolName));
        }

        return ThreadUtils.getMonitoredThreadGroups(threadGroupNames);
    }

    private void monitorThreadGroup(ThreadGroup threadGroup) {
        int newCount = 0, runnableCount = 0, blockedCount = 0, waitingCount = 0, timedWaitingCount = 0, terminatedCount = 0;

        List<Thread> threads = ThreadUtils.getGroupThreads(threadGroup);
        for (Thread thread : threads) {
            switch (thread.getState()) {
                case NEW:
                    newCount++;
                    break;
                case RUNNABLE:
                    runnableCount++;
                    break;
                case BLOCKED:
                    blockedCount++;
                    break;
                case WAITING:
                    waitingCount++;
                    break;
                case TIMED_WAITING:
                    timedWaitingCount++;
                    break;
                case TERMINATED:
                    terminatedCount++;
                    break;
                default:
                    // nothing
                    break;
            }
        }

        if (LOG.isInfoEnabled()) {
            LOG.info(String.format("thread group : %s, new [%d], runnable [%d], blocked [%d], waiting [%d], timed waiting [%d], terminated [%d]",
                    threadGroup.getName(), newCount, runnableCount, blockedCount, waitingCount, timedWaitingCount, terminatedCount));
        }
    }

}
