package com.shindo.singleton;

/**
 * 完整的枚举单例示例
 * Mgr09是我们要应用单例模式的资源，具体可以表现为网络连接、数据库连接、线程池等。
 * <p>
 * 前置知识:
 * ennum Type{
 * A,B,C,D
 * }
 * 创建enum时，编译器会自动为我们生成一个继承自java.lang.Enum的类，我们上面的enum可以简单看作：
 * class Type extends Enum{
 * public static final Type A;
 * public static final Type B;
 * ......
 * }
 * 从这个例子，我们可以把枚举对象Type看做是一个类，而把枚举变量A,B,C,D看作类的Type的实例。
 * <p>
 * 有了上面对枚举的基本认识，下面简单说明枚举到底是如何保证单例的：
 * 首先，在枚举SingletonEnum中我们明确了构造方法限制为私有，在我们访问枚举实例时，会执行构造方法，
 * 同时每个枚举实例都是static final 类型的，也就表明只能被实例化一次。在调用构造方法时，我们的单例被实例化。
 * 也就是说，因为enum中实例被保证只会被实例化一次，所以我们的INSTANCE也被保证实例化一次。
 */
public class Mgr09 {
	//私有化构造函数
	private Mgr09() {
	}

	//定义一个静态枚举类
	static enum SingletonEnum {
		//创建一个枚举对象，该对象天生为单例
		INSTANCE;
		private Mgr09 mgr09;

		//私有化枚举的构造函数
		private SingletonEnum() {
			mgr09 = new Mgr09();
		}

		//
		public Mgr09 getInstance() {
			return mgr09;
		}
	}

	//对外暴露一个获取User对象的静态方法
	public static Mgr09 getInstance() {
		return SingletonEnum.INSTANCE.getInstance();
	}

	public static void main(String[] args) {
		System.out.println(Mgr09.getInstance());
		System.out.println(Mgr09.getInstance());
	}
}
