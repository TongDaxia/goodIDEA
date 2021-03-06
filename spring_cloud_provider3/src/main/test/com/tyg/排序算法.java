package com.tyg;

public class 排序算法 {

    /**
     * // 归并排序算法, A是数组，n表示数组大小
     * merge_sort(A, n) {
     * merge_sort_c(A, 0, n-1)
     * }
     * // 递归调用函数
     * merge_sort_c(A, p, r) {
     * // 递归终止条件
     * if p >= r then return
     * // 取p到r之间的中间位置q
     * q = (p+r) / 2
     * //分治递归
     * merge_sort_c(A, p, q)
     * merge_sort_c(A, q+1, r)
     * // 将A[p...q]和A[q+1...r]合并为A[p...r]
     * merge(A[p...r], A[p...q], A[q+1...r])
     * }
     */
    public void merge_sort(int[] A, int n) {
        merge_sort_c(A, 0, n - 1);


    }

    // 递归调用函数
    public void merge_sort_c(int[] A, int p, int r) {
        // 递归终止条件
        if (p >= r)
            return;
        // 取p到r之间的中间位置q
        int q = (p + r) / 2;
        //分治递归
        merge_sort_c(A, p, q);
        merge_sort_c(A, q + 1, r);
        // 将A[p...q]和A[q+1...r]合并为A[p...r]

    }

   /* public void merge(A[p...r], A[p...q], A[q+1...r]) {
        var i := p，j := q+1，k := 0 // 初始化变量i, j, k
        var tmp := new array[0...r-p] // 申请一个大小跟A[p...r]一样的临时数组
        while i<=q AND j<=r do {
            if A[i] <= A[j] {
                tmp[k++] = A[i++] // i++等于i:=i+1
            } else {
                tmp[k++] = A[j++]
            }
        }
// 判断哪个子数组中有剩余的数据
        var start := i，end := q
        if j<=r then start := j, end:=r
// 将剩余的数据拷贝到临时数组tmp
        while start <= end do {
            tmp[k++] = A[start++]
        }
// 将tmp中的数组拷贝回A[p...r]
        for i:=0 to r-p do {
            A[p+i] = tmp[i]
        }*/


}
