// 1. Two Sum — https://leetcode.com/problems/two-sum/
// Topics: Array, Hash Map
//
// Approach: one pass over the array, keeping a map of value -> index for the numbers
// already seen. For every number we look up its complement (target - n) in the map:
// if it was seen before we found the pair, otherwise we remember the current number.
// O(n) time, O(n) memory.

class Solution {
    fun twoSum(nums: IntArray, target: Int): IntArray {
        val seen = mutableMapOf<Int, Int>()
        nums.forEachIndexed { i, n ->
            seen[target - n]?.let { j -> return intArrayOf(j, i) }
            seen[n] = i
        }
        return intArrayOf()
    }
}
