package com.pingan.common.thread.pool.state;

public abstract class AbstractStateMonitor implements Runnable {

    @Override
    public void run() {
        doExecute();
    }

    
    protected abstract void doExecute();
    
    
}
