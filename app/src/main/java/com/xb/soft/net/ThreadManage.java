package com.xb.soft.net;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManage {

	public static final ThreadManage single = new ThreadManage();
	private static final int MAX_THREAD_COUNT = 3;
	private static final int CORE_THREAD_COUNT = 2;

	private ExecutorService executor = null;

	private ThreadManage(){
		initThreadPool();
	}

	private void initThreadPool(){
		executor = new ThreadPoolExecutor(CORE_THREAD_COUNT, MAX_THREAD_COUNT, 5000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(20),new ThreadPoolExecutor.DiscardPolicy());
	}


	public static ThreadManage getInstance(){
		return single;
	}

	public void ansyExeTask(Runnable task){
		if(executor != null){
			executor.submit(task);
		}
	}

	public void destroy(){
		executor.shutdown();
	}


}
