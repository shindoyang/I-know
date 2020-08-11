package com.shindo.esay;

/**
 * 回文数
 * <p>
 * 判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 * 示例 1:
 * 输入: 121
 * 输出: true
 * <p>
 * 示例 2:
 * 输入: -121
 * 输出: false
 * 解释: 从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。
 * <p>
 * 示例 3:
 * 输入: 10
 * 输出: false
 * 解释: 从右向左读, 为 01 。因此它不是一个回文数。
 */
public class Palindrome {

	//将整数反转
	protected static int reverse(int x) {
		int reverse = 0;
		while (x != 0) {
			int temp = x % 10;
			reverse = reverse * 10 + temp;
			x = x / 10;
		}
		return reverse;
	}

	public static boolean isPalindrome(int x) {
		if (x > Integer.MAX_VALUE || x < Integer.MIN_VALUE) {
			return false;
		}
		//按题目描述，负数肯定不是回文数，所以负数直接返回false
		if (x < 0) {
			return false;
		}
		int reserve = reverse(x);
		//判断原值与反转后的值是否相等
		return x == reserve;
	}

	public static void main(String[] args) {
		int x = -12521;
		boolean palindrome = isPalindrome(x);
		System.out.println("isPalindrome = " + palindrome);
		System.out.println(7 / 10);
	}
}
