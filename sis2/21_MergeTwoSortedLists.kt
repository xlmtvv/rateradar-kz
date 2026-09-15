// 21. Merge Two Sorted Lists — https://leetcode.com/problems/merge-two-sorted-lists/
// Topics: Linked List, Recursion
//
// Approach: recursive definition — the head of the merged list is the smaller of the two
// heads, and its tail is the merge of the rest. `when` handles the cases: if one list is
// empty the answer is the other one, otherwise we keep the smaller node and recursively
// merge its `next` with the other list (`apply` mutates `next` and returns the node).
// O(n + m) time and recursion depth.

/**
 * Definition for singly-linked list (provided by LeetCode).
 * class ListNode(var `val`: Int) {
 *     var next: ListNode? = null
 * }
 */
class Solution {
    fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
        return when {
            list1 == null -> list2
            list2 == null -> list1
            list1.`val` <= list2.`val` -> list1.apply { next = mergeTwoLists(next, list2) }
            else -> list2.apply { next = mergeTwoLists(list1, next) }
        }
    }
}
