package com.vk.simpleutil.library;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池 工具类
 * 
 * @author Administrator
 * 
 */
public class XSimpleExecutor {
	private ExecutorService SinglePool;
	private ExecutorService FixedPool;
	private final int FixedCount = 3;

	// 单例模式实例化类
	private static class Holder {
		private static XSimpleExecutor instance = new XSimpleExecutor();
	}

	public static XSimpleExecutor getInstance() {
		return Holder.instance;
	}

	/**
	 * 创建一个单线程的线程池。这个线程池只有一个线程在工作，也就是相当于单线程串行执行所有任务。如果这个唯一的线程因为异常结束，
	 * 那么会有一个新的线程来替代它。此线程池保证所有任务的执行顺序按照任务的提交顺序执行。
	 */
	public ExecutorService getSinglePool() {
		if (SinglePool == null) {
			SinglePool = Executors.newSingleThreadExecutor();
		}
		return SinglePool;

	}

	/**
	 * 创建固定大小的线程池。每次提交一个任务就创建一个线程，直到线程达到线程池的最大大小。线程池的大小一旦达到最大值就会保持不变，
	 * 如果某个线程因为执行异常而结束，那么线程池会补充一个新线程。
	 */
	public ExecutorService getFixedPool() {
		if (FixedPool == null) {
			FixedPool = Executors.newFixedThreadPool(FixedCount);
		}
		return FixedPool;

	}

	// 关闭线程池
	// pool.shutdown();
	public void stopPool() {
		SinglePool.shutdown();

	}

	/** 初始化 */
	private XSimpleExecutor() {
		if (SinglePool == null) {
			SinglePool = Executors.newSingleThreadExecutor();
		}
		if (FixedPool == null) {
			FixedPool = Executors.newFixedThreadPool(FixedCount);
		}
	}

}
