package com.shindo.singleton;

/**
 * 饿汉式 (推荐使用)
 * 在类加载的时候，已经把对象实例化
 * 问题：不需要的时候，也提前加载了
 */
public class Mgr01 {
	private static final Mgr01 INSTANCE = new Mgr01();

	public Mgr01() {
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
