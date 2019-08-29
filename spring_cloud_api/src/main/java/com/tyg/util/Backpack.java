package com.tyg.util;

/**
 * 我们有一个背包，背包总的承载重量是 Wkg 。
 * 现在我们有 n 个物品，每个物品的重量不等，并且不可分割。
 * 我们现在期望选择几件物品，装载到背包中。
 * 在不超过背包所能装载重量的前提下，如何让背包中物品的总重量最大？
 */
public class Backpack {
    //回溯算法
    //难懂程度还不是最高的哦

    public int maxW = Integer.MIN_VALUE; //存储背包中物品总重量的最大值

    // cw表示当前已经装进去的物品的重量和；i表示考察到哪个物品了；
    // w背包重量；items表示每个物品的重量；n表示物品个数
    // 假设背包可承受重量100，物品个数10，物品重量存储在数组a中，那可以这样调用函数：
    // f(0, 0, a, 10, 100)
    public void f(int i, int cw, int[] items, int n, int w) {
        System.out.print(items[i]+"   ");
        if (cw == w || i == n) { // cw==w表示装满了;i==n表示已经考察完所有的物品
            if (cw > maxW) {
                maxW = cw;
            }
            System.out.println("本轮结束，总重量是："+maxW);
            return;
        }
        f(i + 1, cw, items, n, w);
        if (cw + items[i] <= w) {// 已经超过可以背包承受的重量的时候，就不要再装了
            f(i + 1, cw + items[i], items, n, w);
        }
    }


    public static void main(String[] args) {
        Backpack b = new Backpack();
        b.f(0, 0, new int[]{11, 8, 4, 6, 12, 7, 6, 4, 13,11}, 5, 100);
        System.out.println(b.maxW);
    }
}
