package com.shindo.singleton;

/**
 * 懒汉式
 * 类加载完成后，并没有马上实例化对象，调用的时候才初始化对象
 * <p>
 * 但是下面的写法，在多线程场景下会有线程安全问题
 */
public class Mgr03 {
	private static Mgr03 INSTANCE;

	private Mgr03() {
	}

	public static Mgr03 getInstance() {
		if (INSTANCE == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			INSTANCE = new Mgr03();
		}
		return INSTANCE;
	}

	public static void main(String[] args) {
//		Mgr03 mgr1 = Mgr03.getInstance();
//		Mgr03 mgr2 = Mgr03.getInstance();
//		System.out.println(mgr1 == mgr2);

		for (int i = 0; i < 100; i++) {
			new Thread(() -> System.out.println(Mgr03.getInstance().hashCode())).start();
		}
	}
}
