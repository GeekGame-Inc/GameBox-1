/** 
 * Project Name:GameBox 
 * File Name:DownLoadExecutor.java 
 * Package Name:com.tenone.gamebox.view.utils 
 * Date:2017-4-5����5:17:49 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * �̳߳ع��� ClassName:DownLoadExecutor <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-5 ����5:17:49 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class DownLoadExecutor {
	/** �����̳߳ض��У�ͬʱ����1���̲߳��� */
	private static ThreadPoolExecutor mPool;

	// ���̳߳��е��߳�С��mCorePoolSize��ֱ�Ӵ����µ��̼߳����̳߳�ִ������
	private static final int mCorePoolSize = 1;

	// ����߳���
	private static final int mMaximumPoolSize = 1;

	// �߳�ִ����������Ҷ�����û�п���ִ�е����񣬴���ʱ�䣬����Ĳ�����ʱ�䵥λ
	private static final long mKeepAliveTime = 5L;

	/** ִ�����񣬵��̳߳ش��ڹرգ��������´����µ��̳߳� */
	public synchronized static void execute(Runnable run) {
		if (run == null) {
			return;
		}
		if (mPool == null || mPool.isShutdown()) {
			// ����˵��
			// ���̳߳��е��߳�С��mCorePoolSize��ֱ�Ӵ����µ��̼߳����̳߳�ִ������
			// ���̳߳��е��߳���Ŀ����mCorePoolSize���������������������BlockingQueue��
			// ��BlockingQueue�е���������ˣ����ᴴ���µ��߳�ȥִ�У�
			// ���ǵ����߳�������mMaximumPoolSizeʱ�������׳��쳣������RejectedExecutionHandler����
			// mKeepAliveTime���߳�ִ����������Ҷ�����û�п���ִ�е����񣬴���ʱ�䣬����Ĳ�����ʱ�䵥λ
			// ThreadFactory��ÿ�δ����µ��̹߳���
			mPool = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize,
					mKeepAliveTime, TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<Runnable>(),
					Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
		}
		mPool.execute(run);
	}
	
	 /** ȡ���̳߳���ĳ����δִ�е����� */
    public synchronized static boolean cancel(Runnable run) {
        if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
            return mPool.getQueue().remove(run);
        }else{
            return false;
        }
    }
    
    
    /** �鿴�̳߳����Ƿ���ĳ����δִ�е����� */
    public synchronized static boolean contains(Runnable run) {
        if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
            return mPool.getQueue().contains(run);
        } else {
            return false;
        }
    }
    
    
    /** ���̹ر��̳߳أ���������ִ�е�����Ҳ���ᱻ�ж� */
    public static void stop() {
        if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
            mPool.shutdownNow();
        }
    }
    
    /** ƽ���رյ������̳߳أ����ǻ�ȷ�������Ѿ���������񶼽��ᱻִ����ϲŹر� */
    public synchronized static void shutdown() {
        if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
            mPool.shutdownNow();
        }
    }
}
