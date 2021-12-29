package com.shindo.singleton;

/**
 * 懒汉式
 * 类加载完成后，并没有马上实例化对象，调用的时候才初始化对象
 * <p>
 * 使用Synchronized 关键字给getInstance()方法加锁，以解决线程安全问题
 * <p>
 * 虽然这种加锁方式可以解决懒汉式线程安全的问题，但是对性能有一定影响
 * <p>
 * 为了优化加锁带来的性能问题，这里采用了锁细化的思想，把加锁的范围缩小
 * 但非常不幸，这样写又会引发线程安全问题
 */
public class Mgr05 {
	private static Mgr05 INSTANCE;

	private Mgr05() {
	}

	public static Mgr05 getInstance() {
		if (INSTANCE == null) {
			synchronized (Mgr05.class) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				INSTANCE = new Mgr05();
			}
		}
		return INSTANCE;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(() -> System.out.println(Mgr05.getInstance().hashCode())).start();
		}
	}
}
