package com.tyg;

import org.junit.Test;

public class ErFenFa {


    @Test
    public void test() {
        //int[] ints = {21,2,25,66,54,30,20,56,85,676,69,66};
        int[] ints = {1, 2, 25, 66, 154, 230, 260, 356, 405, 476, 569, 666};

        int i = bsearch(ints, ints.length, 476);
        System.out.println(i + "-----------");
    }


    // 二分查找的递归实现

    /**
     * 二分查找法要求数组&有序
     * 数组是连续的，不适合大规模数据的处理；有序的要求增删时保证顺序
     *
     * @param a
     * @param n
     * @param val
     * @return
     */
    public int bsearch(int[] a, int n, int val) {
        return bsearchInternally(a, 0, n - 1, val);
    }

    private int bsearchInternally(int[] a, int low, int high, int value) {
        if (low > high) return -1;
        int mid = low + ((high - low) >> 1);
        if (a[mid] == value) {
            return mid;
        } else if (a[mid] < value) {
            return bsearchInternally(a, mid + 1, high, value);
        } else {
            return bsearchInternally(a, low, mid - 1, value);
        }
    }


    /**
     * 查找第一个等于某值的下标
     *
     * @param a
     * @param n
     * @param value
     * @return
     */
    public int bsearchFirst(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else if (a[mid] < value) {
                low = mid + 1;
            } else {
                if ((mid == 0) || (a[mid - 1] != value))
                    return mid;
                else
                    high = mid - 1;
            }
        }
        return -1;
    }

    /**
     * 查找第一个等于某值的下标-- 高级写法
     *
     * @param a
     * @param n
     * @param value
     * @return
     */
    public int bsearchFirst2(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] >= value) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        if (low < n && a[low] == value) return low;
        else return -1;
    }


    /**
     * 利用二分法取出数组中比指定值大(或者相等)的最小值
     */
    public int getFirstBig(int[] a, int n, int value) {

        int low = 0;
        int high = n;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            //int mid = low + ((high - low) >> 1);
            if (a[mid] < value) {
                low = mid + 1;
            } else if (a[mid] >= value && a[mid - 1] < value) {
                return mid;
            } else {//就是a[mid]>=value ，但是a[mid-1]>=value,还可以往前取
                high = mid - 1;
            }

        }
        return -1;
    }
    /**
     * 利用二分法取出数组中比指定值小(或者相等)的最大值（最后一个）
     */
    public int getLastSmall(int[] a, int n, int value) {

        int low = 0;
        int high = n;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            //int mid = low + ((high - low) >> 1);
          if(a[mid]<=value && a[mid+1]>value){
              return mid;
          }else if(a[mid] > value){
              high = mid - 1;
          }else if(a[mid]<=value && a[mid+1]<=value){
              low = mid+1 ;
          }

        }
        return -1;
    }

    @Test
    public void t1() {

        int value = 5;
        int[] a = {1, 2, 3, 4, 4, 4, 4, 4, 6, 6, 6, 20};

        //int firstBig = getFirstBig(a, a.length - 1, 5);//获取第一个比5大的数
        int firstBig = getLastSmall(a, a.length - 1, 5);//获取最后一个比5小的数
        System.out.println(firstBig);




    }
}
