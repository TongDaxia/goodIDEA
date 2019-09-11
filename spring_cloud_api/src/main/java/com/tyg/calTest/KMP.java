package com.tyg.calTest;


/**
 *
 * KMP算法是一种用于检测字符串包含关系的算法
 * 主要实现思路类似于BM算法，就是想模式串在和主串匹配时如果遇到坏字符依次多向后滑动几位
 * KMP算法的理解不是特别容易，需要经常复习司考
 *
 * 根据模式串自身的特点，来确定向后滑动的长度
 *
 * KMP的关键是一个Next数组
 * 好前缀的最长可匹配前缀和后缀子串
 *
 * next[]   下标时好前缀对应的结尾字符下标
 *          针对每一个候选的好前缀，需要计算其对应的 最长可匹配前缀字符串的字符下标
 */
public class KMP {


    public static void main(String[] args) {
        char[] a = {'a','c','d','g','s','d','b'};
        char[] b = {'d','g','s'};
        int kmp = kmp(a, a.length, b, b.length);
        System.out.println(kmp);
    }



    // a, b分别是主串和模式串； n, m分别是主串和模式串的长度。
    public static int kmp(char[] a, int n, char[] b, int m) {
        int[] next = getNexts(b, m);
        int j = 0;
        for (int i = 0; i < n; ++i) {
            while (j > 0 && a[i] != b[j]) { // 一直找到a[i]和b[j]
                j = next[j - 1] + 1;
            }
            if (a[i] == b[j]) {
                ++j;
            }
            if (j == m) { // 找到匹配模式串的了
                return i - m + 1;
            }
        }
        return -1;
    }




    /**
     *
     * 获取 失效函数
     * 失效函数的获取规律：
     * 考察第 i 个位置 如果 next[i-1] = k-1 ;说明已经有一段能匹配上了，只需要接下来新增的一个也匹配上， b[i] == b[k] ,那么 next[i] = k
     * 如果i
     *
     * @param b 表示模式串
     * @param m 表示模式串的长度
     * @return
     */
    private static int[] getNexts(char[] b, int m) {
        int[] next = new int[m];
        next[0] = -1;
        int k = -1;
        for (int i = 1; i < m; ++i) {  //考察第 i 个的数值
            while (k != -1 && b[k + 1] != b[i]) { //这一步的确很难
                k = next[k];
            }
            if (b[k + 1] == b[i]) { //相比上次循环，本次i已经增加1；如果 b[i] == b[k+1],那么直接得出 next[i] =k+1；
                ++k;
            }
            next[i] = k;
        }
        return next;
    }



    // 如果不满足 b[i] == b[k+1]，那么往前回溯，
    // 比如next[2] = 0 next[3] = 1; next[4] = 2 ，next[5]--》next[2] = 0 = k
    // 我们假设b[0, i]的最长可匹配后缀子串是b[r, i]。如果我们把最后一个字符去掉，那b[r, i-1]肯定是b[0, i-1]的可匹配后缀子串，但不一定是最长可匹配后缀子串。
    // 既然b[0, i-1]最长可匹配后缀子串对应的模式串的前缀子串的下一个字符并不等于b[i]，
    // 那么我们就可以考察b[0, i-1]的次长可匹配后缀子串b[x, i-1]对应的可匹
    // 配前缀子串b[0, i-1-x]的下一个字符b[i-x]是否等于b[i]。
    // 如果等于，那b[x, i]就是b[0, i]的最长可匹配后缀子串。
    // 可是，如何求得b[0, i-1]的次长可匹配后缀子串呢？次长可匹配后缀子串肯定被包含在最长可匹配后缀子串中，而最长可匹配后缀子串又对应最长可匹配前缀子串b[0, y]。
    // 于是，查找b[0, i-1]的次长可匹配后缀子串，这个问题就变成，查找b[0, y]的最长匹配后缀子串的问题了。
    // 按照这个思路，我们可以考察完所有的b[0, i-1]的可匹配后缀子串b[y, i-1]，直到找到一个可匹配的后缀子串，
    // 它对应的前缀子串的下一个字符等于b[i]，那这个b[y,i]就是b[0, i]的最长可匹配后缀子串。

}
