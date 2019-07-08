package com.tyg;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

public class RanTest {


    @Test
    public void test() {

        DecimalFormat df2 = new DecimalFormat("#0.00");

        Double sum = 0.0;


       // ArrayList<BigDecimal> result = new ArrayList<>();
        BigDecimal s1 = new BigDecimal(0);
        BigDecimal s2 = new BigDecimal(0);
        BigDecimal s3 = new BigDecimal(0);
        BigDecimal s4 = new BigDecimal(0);
        BigDecimal s5 = new BigDecimal(0);
        for (int i = 0; i < 10000; i++) {
            ArrayList<Double> set  =new ArrayList();
            for (int i1 = 0; i1 < 4; i1++) {
                Double random = Math.random() * 5;
                Double d = Double.valueOf(df2.format(random));
                set.add(d);
            }
            Collections.sort(set);//随机点
            //System.out.println("sss"+set.toString());
            ArrayList<BigDecimal> re = new ArrayList();
            re.add(new BigDecimal(set.get(0)+""));//第一个人
            for (int i1 = 0; i1 < set.size()-1; i1++) {
                BigDecimal a = new BigDecimal(""+set.get(i1+1));
                BigDecimal b = new BigDecimal(""+set.get(i1));
                BigDecimal r = a.subtract(b);
                re.add(r);
            }

            BigDecimal d =new BigDecimal(set.get(set.size()-1)+"");
            BigDecimal last =new BigDecimal(5);
            re.add(last.subtract(d));//最后一个人

            System.out.println(re.toString());

                s1 = re.get(0).add(s1);
                s2 = re.get(1).add(s2);
                s3 = re.get(2).add(s3);
                s4 = re.get(3).add(s4);
                s5 = re.get(4).add(s5);
        }
        System.out.println("第1个人10000次取得的总金额："+s1+",平均每次获得："+s1.divide(new BigDecimal(10000))+"元");
        System.out.println("第2个人10000次取得的总金额："+s2+",平均每次获得："+s2.divide(new BigDecimal(10000))+"元");
        System.out.println("第3个人10000次取得的总金额："+s3+",平均每次获得："+s3.divide(new BigDecimal(10000))+"元");
        System.out.println("第4个人10000次取得的总金额："+s4+",平均每次获得："+s4.divide(new BigDecimal(10000))+"元");
        System.out.println("第5个人10000次取得的总金额："+s5+",平均每次获得："+s5.divide(new BigDecimal(10000))+"元");



    }
}
