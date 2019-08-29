package com.tyg.Cal;

import java.util.ArrayList;

public class TestBag {



    static  int[] weight = {1,5,4,3,9,5,1,4,3,4,7,8};
    static int[] price = {2,5,6,8,4,6,2,4,5,3,4,5};

    final static int limit = 31 ;
    static ArrayList<Integer> re = new ArrayList<>();

    //满足重量前提下的最中的重量
    //调用test(0,0)
    public static void  test(int i,int cw ){
        if(cw == limit|| i == weight.length-1  ){
            re.add(cw);
            System.out.println("不能在装了，目前的重量是"+cw+"：目前使用的物品个数是："+i);
            return ;
        }
        System.out.println("不装第i个，他的重量是："+weight[i]);
        test(i+1,cw);//这就代表不装第i个
        if(cw+weight[i]<=limit){
            System.out.println("装载第i+1个："+weight[i+1]);
            test(i+1,cw+weight[i]);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        test(0,0);
        Thread.sleep(300);
        System.out.println(re);
    }

}
