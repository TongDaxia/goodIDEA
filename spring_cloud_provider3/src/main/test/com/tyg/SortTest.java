package com.tyg;

import org.junit.Test;

import java.util.Arrays;

public class SortTest {


    /*
     * 快速排序得实现
     * 快速排序得主要步骤
     * 使用三分法取一个基数
     * 使用两个游标 i，j
     * [0,i] 是已经排序完成的
     * [j,n] 是需要继续排序的
     *
     * */
    public String[] qsout(String[] arr,int fist,int last) {


       return  null ;

    }

    public String getBase(String[] arr) {

        if (arr.length > 3) {
            String i1 = arr[0];
            int le = arr.length % 2 == 0 ? arr.length : arr.length - 1;
            String i2 = arr[le / 2];
            String i3 = arr[arr.length - 1];
            String max = Integer.parseInt(i1.concat(i2)) > 0 ? i1 : i2;
            return Integer.parseInt(max.concat(i3)) > 0 ? max : i3;
        } else {
            return arr[0];
        }

    }




    public static void sort(int[] a){
        if (a.length>0) {
            sort(a,0,a.length-1);
        }
        
    }
    public static void sort(int[] a,int low,int height){
        int i=low;
        int j=height;
        if (i>j) {    //放在k之前，防止下标越界
            return;
        }
        int k=a[i];////比较基准，最好优化一下
        
        while (i<j) {   
            while (i<j && a[j]>k) {    //找出小的数
                j--;
            }
            while (i<j && a[i]<=k) {  //找出大的数
                i++;
            }
            if(i<j){   //交换
                int swap=a[i];
                a[i]=a[j];
                a[j]=swap;
            }
            
        }
        //交换K
        k=a[i];
        a[i]=a[low];
        a[low]=k;
        
        //对左边进行排序,递归算法
        sort(a, low, i-1);
        //对右边进行排序
        sort(a,i+1,height);
    }

    @Test
    public void ttt(){

        int[]  arr={5,9,7,4,5,7,6,1,9,9,7,4};
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));


    }
 


}
