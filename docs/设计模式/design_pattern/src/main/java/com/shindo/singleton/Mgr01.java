package com.shindo.singleton;

/**
 * 饿汉式
 * 类加载到内存后，就实例化一个单例，JVM保证线程安全
 * (成熟方案一：推荐使用)
 * 缺点：不管用到与否，类装载时就完成实例化
 */
public class Mgr01 {
	private static final Mgr01 INSTANCE = new Mgr01();

	private Mgr01() {
	}

	public static Mgr01 getInstance() {
		return INSTANCE;
	}

	public static void main(String[] args) {
		Mgr01 mgr1 = Mgr01.getInstance();
		Mgr01 mgr2 = Mgr01.getInstance();
		System.out.println(mgr1 == mgr2);

		for (int i = 0; i < 100; i++) {
			new Thread(() -> {
				System.out.println(Mgr01.getInstance().hashCode());
			}).start();
		}
	}
}
