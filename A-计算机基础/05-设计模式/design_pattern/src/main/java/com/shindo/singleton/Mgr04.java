package com.shindo.singleton;

/**
 * 懒汉式
 * 类加载完成后，并没有马上实例化对象，调用的时候才初始化对象
 * <p>
 * 使用Synchronized 关键字给getInstance()方法加锁，以解决线程安全问题
 * 这个Synchronized 因为是加载方法上的，所以锁的对象就是当前类的.class
 * <p>
 * 虽然这种加锁方式可以解决懒汉式线程安全的问题，但是对性能有一定影响
 */
public class Mgr04 {
	private static Mgr04 INSTANCE;

	private Mgr04() {
	}

	public synchronized static Mgr04 getInstance() {
		if (INSTANCE == null) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			INSTANCE = new Mgr04();
		}
		return INSTANCE;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(() -> System.out.println(Mgr04.getInstance().hashCode())).start();
		}
	}
}
