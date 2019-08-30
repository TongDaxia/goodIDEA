package com.tyg.Cal;

/**
 *
 * 去重的主要思路
 * 首先是建立一个专门标识每一个位置的数组，数据下表代替每一个数据，数组的内容是标识该数据是否存在
 * 对于存在与否，除了简单使用true false ；还可以结合位运算产生结果
 * 比如：
 * 存：bytes[byteIndex] |= (1 << bitIndex);
 * 取：(bytes[byteIndex] & (1 << bitIndex)) != 0
 * 此事数组中存的是一个字节 char 也就是一个8位 的 01011010
 *
 * 布隆过滤器是组建一个比原数组还大的位图
 * 对原值进行多次不同的hash ,使得对应在 bitMap 中特有的位置有值 ；判断时，存在不一定存在，不存在肯定不存在。
 *
 */

public class BitMap {
    private char[] bytes;
    private int nbits;

    public BitMap(int nbits) {
        this.nbits = nbits;
        this.bytes = new char[nbits / 8 + 1];
    }

    public void set(int k) {
        if (k > nbits) return;
        int byteIndex = k / 8;
        int bitIndex = k % 8;
        bytes[byteIndex] |= (1 << bitIndex);
        System.out.println(bytes[byteIndex]);
    }

    public boolean get(int k) {
        if (k > nbits) return false;
        int byteIndex = k / 8;
        int bitIndex = k % 8;
        return (bytes[byteIndex] & (1 << bitIndex)) != 0;

    }


    public static void main(String[] args) {

        BitMap bitMap = new BitMap(10);
        bitMap.set(4);
        bitMap.set(5);
        bitMap.set(6);

        System.out.println();

        System.out.println( bitMap.get(2));
        System.out.println(bitMap.get(4));
        System.out.println(bitMap.get(6));
        System.out.println(bitMap.get(7));

    }
}