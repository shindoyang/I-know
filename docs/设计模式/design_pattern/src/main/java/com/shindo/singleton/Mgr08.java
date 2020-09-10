package com.shindo.singleton;

/**
 * 枚举单例
 * 背景：前面发现可以使用饿汉式或者双重校验懒汉式单例模块，
 * 但是因为这两种方式还是保留了构造函数，经不住坏人采用反射的办法来创建对象。
 *
 * Effective java 提供了枚举单例的方案
 *
 * 如果看Mgr09 的枚举单例示例，你会发现整套方案和静态内部类的思想异曲同工之妙。
 * 都是借助jdk原生的组件保证对象的单例和延迟创建。
 */
public enum Mgr08 {
	INSTANCE;
	public Mgr08 getInstance(){
		return INSTANCE;
	}
}
