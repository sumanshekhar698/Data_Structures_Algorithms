package com.dsa.leetcode.arrays_numbers.binary_search;

import java.util.Arrays;

public class GFG_FrequencyOfAnElementInSortedArray {

    public static void main(String[] args) {
        int[] nums = {5, 7, 7, 8, 8, 10};
        System.out.println(searchRange(nums, 7));

    }

    static public int searchRange(int[] nums, int target) {
        int i = 0;
        int j = nums.length - 1;


        while (i <= j) {
//            int mid = (i + j) / 2;
            int mid = i + (j - i) / 2;//to avoid integer overflow
            if (target == nums[mid]) {
                int start = mid;
                int end = mid;

                while (start >= 0 && nums[start] == target) {//boundary condition and till target is found
                    start--;
                }
                while (end < nums.length && nums[end] == target) {////boundary condition and till target is found
                    end++;//end will be nums.length if target is not found
                }

//                at this point start and end will be pointing little ahead and behind of the target
                return end - start - 1;// as this will give the count of the target

            } else if (nums[mid] < target)
                i = mid + 1;
            else
                j = mid - 1;
        }

        return -1;
    }
}
