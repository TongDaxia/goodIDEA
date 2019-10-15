package com.tyg.Cal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BackpackRePro {


  /*  private int maxW = Integer.MIN_VALUE; // 结果放到maxW中
    private int[] weight = {2, 2, 4, 6, 3}; // 物品重量
    private int n = 5; // 物品个数
    private int w = 9; // 背包承受的最大重量
    private boolean[][] mem = new boolean[5][10]; // 备忘录，默认值false

    public void f(int i, int cw) { // 调用f(0, 0)
        if (cw == w || i == n) { // cw==w表示装满了，i==n表示物品都考察完了
            if (cw > maxW) maxW = cw;
            return;
        }
        if (mem[i][cw]) return; // 重复状态
        mem[i][cw] = true; // 记录(i, cw)这个状态
        f(i + 1, cw); // 选择不装第i个物品
        if (cw + weight[i] <= w) {

            f(i + 1, cw + weight[i]); // 选择装第i个物品
        }
    }
*/

    private ArrayList<Integer> arr = new ArrayList<>();
    private HashMap<String,ArrayList> hashMap = new HashMap();

    private int maxW = Integer.MIN_VALUE; // 结果放到maxW中
    private int[] weight = {2,2,4,6,3}; // 物品重量
    private int n = 5; // 物品个数
    private int w = 9; // 背包承受的最大重量
    private boolean[][] mem = new boolean[5][10]; // 备忘录，默认值false
    public void f(int i, int cw) { // 调用f(0, 0)
        if (cw == w || i == n) { // cw==w表示装满了，i==n表示物品都考察完了
            if (cw > maxW)
                maxW = cw;
            System.out.println("目前的内容1："+arr.toString());
            String key = "key"+i+cw+"";
            //System.out.println("存起来的内容："+key+"-->"+arr);
            hashMap.put(key,arr);
            return;
        }
        if (mem[i][cw]){// 重复状态
            String key =  "key"+i+cw+"";
            if(hashMap.get(key)!=null)
                arr.addAll(hashMap.get(key));
            System.out.println("目前的内容2+"+key+"+："+arr.toString());
            return;

        }


        mem[i][cw] = true; // 记录(i, cw)这个状态
        f(i+1, cw); // 选择不装第i个物品
        if (cw + weight[i] <= w) {
            arr.add(weight[i]);
            f(i+1,cw + weight[i]); // 选择装第i个物品
            arr.remove(arr.size()-1);
        }

        }

    public static void main(String[] args) {
        new BackpackRePro().f(0, 0);
    }


}
