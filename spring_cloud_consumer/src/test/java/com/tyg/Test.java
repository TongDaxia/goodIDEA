package com.tyg;

import com.tyg.pojo.Dept;

public class Test {

    @org.junit.Test
    public void test() {

        Dept d = new Dept();
        d.setDb_source("11").setDname("1111");
        System.out.println("修改之前：" + d.toString());
        changeD(d);
        System.out.println("修改之后：" + d.toString());

    }


    public void changeD(Dept dept) {

        dept.setDname("222").setDeptno(22L);
        System.out.println("修改过程中：" + dept.toString());
    }


    /**
     * 30 个人在一条船上，超载，需要 15 人下船。
     * <p>
     * 于是人们排成一队，排队的位置即为他们的编号。
     * <p>
     * 报数，从 1 开始，数到 9 的人下船。
     * <p>
     * 如此循环，直到船上仅剩 15 人为止，问都有哪些编号的人下船了呢？
     */
    @org.junit.Test
    public void testBoat() {

        int[] people = new int[30];
        //一共30个人，编号代表每个人
        for (int i = 0; i < 30; i++) {
            people[i] = i + 1;
        }
        int num = 1;
        int count = 0;
        int c = 0;
        while (true) {
            if (c == 30) {
                c=0;
            }
            if (people[c] == 0) {
                c++; //虽然这个位置的人下去了为了数到下一个人， c+1
                continue;
            }
            if (num == 9) {
                System.out.println("编号为" + people[c] + "的人下船了！");
                people[c] = 0;
                num=0 ;
                count++;
            }
            num++;
            c++;
            if (count >= 15) {
                System.out.println("好了，够了！");
                break;
            }
        }

    }
}
