package com.cgx.Job;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StartJob {
    private static int threadIndex = 0;
    public StartJob() {

    }
    private static ThreadPoolExecutor jobManager=null;
    public static void startNewThread(){
        SendMailJob sendMailJob=new SendMailJob();
        Thread thread=new Thread(sendMailJob);
        jobManager.execute(sendMailJob);
    }
    static {
        jobManager = new ThreadPoolExecutor(
                3,  5, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {

                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setName("handleMsg-" + threadIndex++);
                        t.setDaemon(true);
                        return t;
                    }
                });
    }

}
