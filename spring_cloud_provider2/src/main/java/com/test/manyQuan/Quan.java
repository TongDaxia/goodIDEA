package com.test.manyQuan;


import java.util.Objects;

public class Quan {

    private  String id ;
    private  int userid ;
    //private String tag ;
    private  int low  = 1 ;//优惠券使用下限
    private  int subprice  =  1;//优惠金额
    private  double value ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getSubprice() {
        return subprice;
    }

    public void setSubprice(int subprice) {
        this.subprice = subprice;
    }

    public double getValue() {
        this.value = (low/subprice);
        return low/subprice;
    }


    @Override
    public String toString() {
        return "Quan{" +
                "id=" + id +
                ", userid=" + userid +
                ", low=" + low +
                ", subprice=" + subprice +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quan quan = (Quan) o;
        return  low == quan.low &&
                subprice == quan.subprice ;

    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userid, low, subprice);
    }
}
