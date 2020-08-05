package com.shindo.esay;

import java.util.HashMap;
import java.util.Map;

/**
 * 1、两数之和
 * <p>
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
 * 例子：
 * 给定 nums = [2, 7, 11, 15], target = 9
 * <p>
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 */
public class TwoSums {
	/**
	 * 暴力破解
	 * 时间复杂度：O(n^2)
	 *
	 * @param nums
	 * @param target
	 * @return
	 */
	private static int[] twoSums(int[] nums, int target) {
		for (int i = 0; i < nums.length; i++) {
			for (int j = i + 1; j < nums.length; j++) {
				if (nums[j] == target - nums[i]) {
					return new int[]{i, j};
				}
			}
		}
		throw new IllegalArgumentException("No two sum solution");
	}

	/**
	 * 两边哈希表
	 * 时间复杂度O(n)
	 */
	private static int[] solution(int[] nums, int target) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		//第一次迭代，将每个元素和它的索引添加的哈希表中
		for (int i = 0; i < nums.length; i++) {
			map.put(nums[i], i);
		}

		for (int i = 0; i < nums.length; i++) {
			int complement = target - nums[i];
			if (map.containsKey(complement) && map.get(complement) != i) {
				return new int[]{i, map.get(complement)};
			}
		}
		throw new IllegalArgumentException("No two sum solution");
	}

	public static void main(String[] args) {
		int[] temp = new int[]{2, 7, 11, 15};
		int[] back = twoSums(temp, 22);//方法一
		int[] back2 = solution(temp, 22);//方法二
		System.out.println("====== " + back[0] + " , " + back[1]);
		System.out.println("====== " + back2[0] + " , " + back2[1]);
	}
}
