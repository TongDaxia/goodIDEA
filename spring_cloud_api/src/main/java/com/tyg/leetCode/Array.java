package com.tyg.leetCode;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 *
 *
 *
 *
 */
public class Array {

    /**
     * Given an array and a value, remove all instances of that = value in place and return the new length.
     * The order of elements can be changed. It doesn't matter what you leave beyond the new length.
     * 对于给定的数组和值，去除和指定值相等的数，并返回新长度，超出新数组范围的内容不限。
     *
     * @param A
     * @param n
     * @param elem
     * @return
     */
    public static int removeElement(int A[], int n, int elem) {

        int i = 0;
        int j = 0;
        for (i = 0; i < n; i++) {
            if (A[i] == elem) {
                continue;
            }
            A[j] = A[i];
            j++;
        }

        return j;
    }


    /**
     * Given a sorted array, remove the duplicates in place such that > each element appear only once and return the new length.
     * Do not allocate extra space for another array, you must do this in place with constant memory.
     * For example, Given input array A = [1,1,2],
     * Your function should return length = 2, and A is now [1,2].
     * <p>
     * 对于给定的排序好的数组，重复项移除，这样每个元素只出现一次，并返回新的长度。
     * 不要为另一个数组分配额外的空间，必须在内存恒定的情况下就地分配
     */
    public static int removeDuplicates(int A[], int n) {

        if (n == 0) {
            return 0;
        }

        int j = 0;
        for (int i = 1; i < n; i++) {
            if (A[j] != A[i]) {
                A[++j] = A[i];
            }
        }
        return j + 1;
    }


    /**
     * Given a non-negative number represented as an array of digits, plus one to the number.
     * The digits are stored such that the most significant digit is at the head of the list.
     * 给定一个非负数，用一个数字数组表示，再加上一个数字。
     * 存储数字时，最有效的数字在列表的最前面。
     *
     * @return
     */
    public static int[] plusOne(int[] a) {

//        for (int i = 0; i < a.length; i++) {
//            a[i] = 0 ;
//        }
//        int sum = 0;
//        int one = 1;
//
//        for (int i = a.length - 1; i >= 0; i--) {
//
//            sum = one + a[i];
//            one = sum / 10;
//            a[i] = sum % 10;
//        }
//        if(one > 0) {
//            res.insert(res.begin(), one);
//        }
//        return res;

        return null;

    }


    public static void main(String[] args) {


        int[] a = {1, 2, 3, 4, 3, 1, 2, 3, 4};
        int i = removeElement(a, a.length, 1);
        System.out.println(i);


    }




}
