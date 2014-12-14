package com.pingan.common.thread.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadPoolContextTest {

    public static void main(String[] args) {
        ThreadPoolContext context = ThreadPoolContext.getContext();
        final ExecutorService pool = context.getThreadPool(ThreadPoolContextConstants.AMESB_FUND_SERVICE_POOL);
        
        for (int i = 0 ; i < 10000000; i ++) {
            final int index = i;
            new Thread(){
                public void run() {
                    System.out.println(String.format("start thread %d", index));
                    Future<String> future = pool.submit(new TestRunnable());
                    try {
                        future.get(5, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        future.cancel(true);
                    }
                    System.out.println(String.format("end thread %d", index));
                }
            }.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        
    }

    private static final class TestRunnable implements Callable<String> {

        @Override
        public String call() throws Exception {
            Thread.sleep(1000 * 1000);
            return "123";
        }

       

    }
}
