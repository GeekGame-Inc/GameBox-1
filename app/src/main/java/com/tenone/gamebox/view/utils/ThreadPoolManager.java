package com.tenone.gamebox.view.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadPoolManager {
    /**
     * �߳�ִ����
     **/
    private static ExecutorService executorService = null;
    /**
     * �̶�5���߳�
     **/
    private static int nThreads = 5;
    /**
     * ����
     **/
    private static ThreadPoolManager taskExecutorPool = null;

    /** ��ʼ���̳߳� **/
    static {
        taskExecutorPool = new ThreadPoolManager( nThreads * getNumCores() );
    }

    /**
     * ���캯��
     **/
    private ThreadPoolManager(int threads) {
        //executorService = Executors.newFixedThreadPool(threads);
        executorService = Executors.newScheduledThreadPool( threads );
    }

    /**
     * ȡ�õ���
     *
     * @return
     */
    public static ThreadPoolManager getInstance() {
        return taskExecutorPool;
    }

    /**
     * ȡ���߳�ִ����
     *
     * @return
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * ȡ���������߳�ִ����
     *
     * @return
     */
    public ScheduledExecutorService getScheduledExcutorService() {
        return (ScheduledExecutorService) executorService;
    }

    /**
     * ����ֻ�cup����
     *
     * @return
     */
    public static int getNumCores() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        return threadCount;
    }
}
