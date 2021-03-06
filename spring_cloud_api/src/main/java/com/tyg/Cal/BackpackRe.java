package com.tyg.Cal;


import java.util.ArrayList;

public class BackpackRe {

    // 回溯算法实现。注意：我把输入的变量都定义成了成员变量。
    private int maxW = Integer.MIN_VALUE; // 结果放到maxW中
    private int[] weight = {2, 2, 4, 6, 3}; // 物品重量
    private int n = weight.length; // 物品个数
    private int w = 9; // 背包承受的最大重量
    private ArrayList arr = new ArrayList();

    public void f(int i, int cw) { // 调用f(0, 0)

        if (cw == w || i == n) { // cw==w表示装满了，i==n表示物品都考察完了
            if (cw > maxW) {
                maxW = cw;
            }
            System.out.println("目前的书包里有的物理："+arr.toString());

            return;
        }


        f(i + 1, cw); // 选择不装第i个物品

        if (cw + weight[i] <= w) {
            arr.add(weight[i]);
            f(i + 1, cw + weight[i]); // 尝试装第i个物品，尝试完成（迭代完成）把把拿出来进行下一次尝试
            arr.remove(arr.size()-1);
        }
    }

    public static void main(String[] args) {
        new BackpackRe().f(0, 0);
    }


}
