// 242. Valid Anagram — https://leetcode.com/problems/valid-anagram/
// Topics: String, Hash Map, Sorting
//
// Approach: two strings are anagrams when they contain the same characters with the same
// frequencies. `groupingBy { it }.eachCount()` builds a Map<Char, Int> of character counts
// for each string in one expression, and Kotlin compares maps structurally with ==.
// O(n) time, O(k) memory for k distinct characters.

class Solution {
    fun isAnagram(s: String, t: String): Boolean {
        return s.groupingBy { it }.eachCount() == t.groupingBy { it }.eachCount()
    }
}
