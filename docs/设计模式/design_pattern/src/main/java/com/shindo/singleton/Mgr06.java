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
 * <p>
 * 为了解决锁细化引起的线程安全问题，使用双重检查来规避（double check）
 * 成熟方案二
 */
public class Mgr06 {
	private static Mgr06 INSTANCE;

	private Mgr06() {
	}

	public static Mgr06 getInstance() {
		//double check
		if (INSTANCE == null) {
			synchronized (Mgr06.class) {
				if (INSTANCE == null) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					INSTANCE = new Mgr06();
				}
			}
		}
		return INSTANCE;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(() -> {
				System.out.println(Mgr06.getInstance().hashCode());
			}).start();
		}
	}

}
