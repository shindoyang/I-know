package com.shindo.singleton;

/**
 * 饿汉式优化
 * final修饰的变量必须马上初始化
 * 
 */
public class Mgr02 {
	private static final Mgr02 INSTANCE;

	static {
		INSTANCE = new Mgr02();
	}

	private Mgr02() {

	}
	
	public static Mgr02 getInstance(){
		return INSTANCE;
	}

	public static void main(String[] args) {
		Mgr02 mgr1 = Mgr02.getInstance();
		Mgr02 mgr2 = Mgr02.getInstance();
		System.out.println(mgr1 == mgr2);
	}

}
