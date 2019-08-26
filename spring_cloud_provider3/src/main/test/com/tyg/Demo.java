package com.tyg;


import org.junit.Test;

import java.util.Stack;

/**
 * 判断一个链表是否是回文结构
 * 实现思路：
 * 使用一个辅助的栈结构，再使用两指针 ，一个快指针，一个慢指针，当快指针走完整个链表的时候，慢指针刚好来到链表。
 * 中间的位置，此时从中间的位置开始将剩余链表中的元素放入到栈中；然后重头开始遍历链表，栈中的元素也依次弹出。
 * 如果栈为空的时候，还没有出现元素不相等的情况的话，那么该链表是回文结构。
 * 相比较于第一种方法，这样的方法能够节省一半的空间，但是额外空间复杂度还是O(n)的。
 * 这里值得注意的是：无论链表的长度是奇数还是偶数，都需要保证慢指针在后半部分的起点
 */

public class Demo {


    private class Node {
        private int data;
        private Node next;

        public Node(int data) {
            this.data = data;
        }
    }

    @Test
    public void test() {

        Node n = new Node(121);

        boolean palindrome = isPalindrome(n);
        System.out.println(palindrome);

    }

    public static boolean isPalindrome(Node head) {
        if (head == null && head.next == null) {
            return true;
        }

        Node right = head.next;
        Node cur = head;
        while (cur.next != null && cur.next.next != null) {
            right = right.next;
            cur = cur.next.next;
        }

        Stack<Node> stack = new Stack<Node>();
        while (right != null) {
            stack.push(right);
            right = right.next;
        }

        while (!stack.isEmpty()) {
            if (head.data != stack.pop().data) {
                return false;
            }
            head = head.next;
        }
        return true;
    }


    @Test
    public void t2() {
        SkipList sk = new SkipList();
        for (int i = 0; i < 30; i++) {

            sk.insert(i);

        }
      sk.printAll();


    }

}
