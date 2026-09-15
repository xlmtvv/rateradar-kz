// 14. Longest Common Prefix — https://leetcode.com/problems/longest-common-prefix/
// Topics: String
//
// Approach: the common prefix of the whole array is the common prefix of the first string
// with the second, then of that result with the third, and so on — exactly a `reduce`.
// The accumulator starts as strs[0] and is shortened by `commonPrefixWith` for every next
// string; once it becomes empty it stays empty. O(S) time for S total characters.

class Solution {
    fun longestCommonPrefix(strs: Array<String>): String {
        return strs.reduce { prefix, s -> prefix.commonPrefixWith(s) }
    }
}
