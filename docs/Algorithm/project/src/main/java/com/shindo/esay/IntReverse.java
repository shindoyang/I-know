package com.shindo.esay;

/**
 * 7、整数反转
 * 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。
 * 示例 1:
 * 输入: 123
 * 输出: 321
 * 示例 2:
 * 输入: -123
 * 输出: -321
 * 示例 3:
 * 输入: 120
 * 输出: 21
 * 注意:
 * <p>
 * 假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−2^31,  2^31 − 1]。请根据这个假设，如果反转后整数溢出那么就返回 0。
 */
public class IntReverse {
	private static int method1(int x) {

		if (x >= Integer.MAX_VALUE || x <= Integer.MIN_VALUE) {
			throw new IllegalArgumentException("not a right num");
		}
		if (x == 0) {
			return x;
		}
		char[] chars = (x + "").toCharArray();

		int result = 0;
		boolean nagative = false;
		StringBuilder sb = new StringBuilder();
		for (int i = chars.length - 1; i >= 0; i--) {
			if (i == chars.length - 1 && chars[i] == '0') {
				continue;
			}
			if (i == 0 && chars[i] == '-') {
				nagative = true;
				continue;
			}
			sb.append(chars[i]);
		}
		result = Integer.parseInt(sb.toString());
		if (result >= Integer.MAX_VALUE || result <= Integer.MIN_VALUE) {
			throw new IllegalArgumentException("not a right num");
		}
		if (nagative) {
			result = -result;
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(Integer.MAX_VALUE);
		int i = method1(1534236469);//9646324351
		System.out.println("=========== " + i);
	}
}
