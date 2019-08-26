package com.tyg;

public class StringContains {




    private static final int SIZE = 256; // 全局变量或成员变量
    private void generateBC(char[] b, int m, int[] bc) {
        for (int i = 0; i < SIZE; ++i) {
            bc[i] = -1; // 初始化bc
        }
        for (int i = 0; i < m; ++i) {
            int ascii = (int)b[i]; // 计算b[i]的ASCII值
            bc[ascii] = i;
        }
    }


    /**
     * 正常情况下遇见模式串中不存在的坏字符，可以直接往后滑动位数？
     * 坏字符对应的模式串中的字符下标记作 si 坏字符在模式串中的下标记作 xi （多个取靠后的那一个，如果不存在就是 -1）
     * 模式串往后移动的位数就等于 si-xi
     *
     * 只包含坏字符规则：主串中出现了与模式串位置不对的但是字符串  比如  acabdc  abd
     * 不包含好后缀规则（下个程序中体现）：
     * 的BM算法
     *
     * @param a
     * @param n
     * @param b
     * @param m
     * @return
     */
    public int bm(char[] a, int n, char[] b, int m) {
        int[] bc = new int[SIZE]; // 记录模式串中每个字符最后出现的位置
        generateBC(b, m, bc); // 构建坏字符哈希表
        int i = 0; // i表示主串与模式串对齐的第一个字符
        while (i <= n - m) {
            int j;
            for (j = m - 1; j >= 0; --j) { // 模式串从后往前匹配
                if (a[i+j] != b[j]) break; // 坏字符对应模式串中的下标是j
            }
            if (j < 0) {
                return i; // 匹配成功，返回主串与模式串第一个匹配的字符的位置
            }
            // 这里等同于将模式串往后滑动j-bc[(int)a[i+j]]位
            i = i + (j - bc[(int)a[i+j]]);
        }
        return -1;
    }


    // b表示模式串，m表示长度，suffix，prefix数组事先申请好了
    private void generateGS(char[] b, int m, int[] suffix, boolean[] prefix) {
        for (int i = 0; i < m; ++i) { // 初始化
            suffix[i] = -1;
            prefix[i] = false;
        }
        for (int i = 0; i < m - 1; ++i) { // b[0, i]
            int j = i;
            int k = 0; // 公共后缀子串长度
            while (j >= 0 && b[j] == b[m-1-k]) { // 与b[0, m-1]求公共后缀子串
                --j;
                ++k;
                suffix[k] = j+1; //j+1表示公共后缀子串在b[0, i]中的起始下标

            }
           // i
            if (j == -1) prefix[k] = true; //如果公共后缀子串也是模式串的前缀子串
        }
    }


    /**
     *
     * 如果好后缀在模式串中不存在可匹配的子串，那在我们一步一步往后滑动模式串的过程中，
     * 只要主串中的 {u} 与模式串有重合，那肯定就无法完全匹配。
     * 但是当模式串滑动到前缀与主串中 {u} 的后缀有部分重合的时候，
     * 并且重合的部分相等的时候，就有可能会存在完全匹配的情况。
     * 要考察好后缀的后缀子串，是否存在跟模式串的前缀子串匹配的。
     * abcacabcbcbacabc……
     *    cbacabc
     *-->        cbacabc   -- 过度滑动
     *-->       cbacabc   -- 正常滑动
     *
     */


}
