// 20. Valid Parentheses — https://leetcode.com/problems/valid-parentheses/
// Topics: String, Stack
//
// Approach: a stack of opening brackets — every opening bracket is pushed, every closing
// bracket must match the bracket on top of the stack, which is popped. A map from closing
// to opening bracket replaces a chain of if/else, and `all` stops at the first mismatch.
// The string is valid when everything matched and the stack is empty. O(n) time, O(n) memory.

class Solution {
    fun isValid(s: String): Boolean {
        val pairs = mapOf(')' to '(', ']' to '[', '}' to '{')
        val stack = ArrayDeque<Char>()
        val allMatched = s.all { c ->
            if (c in pairs.values) { stack.addLast(c); true }
            else stack.removeLastOrNull() == pairs[c]
        }
        return allMatched && stack.isEmpty()
    }
}
