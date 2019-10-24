package com.test.manyQuan;

import java.util.List;


public class Order {
    private  int id ;
    private  int userid;
    private  int amount ;  //总价
    private  int amountTrue ;  //实际支付总价
    List<OrderSku> skus;
    List<Quan> quansUsed ; //实际使用过的券

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmountTrue() {
        return amountTrue;
    }

    public void setAmountTrue(int amountTrue) {
        this.amountTrue = amountTrue;
    }

    public List<OrderSku> getSkus() {
        return skus;
    }

    public void setSkus(List<OrderSku> skus) {
        this.skus = skus;
    }

    public List<Quan> getQuansUsed() {
        return quansUsed;
    }

    public void setQuansUsed(List<Quan> quansUsed) {
        this.quansUsed = quansUsed;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userid=" + userid +
                ", amount=" + amount +
                ", amountTrue=" + amountTrue +
                ", skus=" + skus +
                ", quansUsed=" + quansUsed +
                '}' ;
    }
}
