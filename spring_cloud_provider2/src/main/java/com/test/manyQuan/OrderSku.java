package com.test.manyQuan;

import java.util.List;

public class OrderSku {

    private  int id ;

    private int price ;
    private List<Quan> quans ;//支持的优惠券
    private Quan target ;//当前订单指定的优惠券
    private double priceTrue ; //优惠完成的价格

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Quan> getQuans() {
        return quans;
    }

    public void setQuans(List<Quan> quans) {
        this.quans = quans;
    }

    public Quan getTarget() {
        return target;
    }

    public void setTarget(Quan target) {
        this.target = target;
    }

    public double getPriceTrue() {
        return priceTrue;
    }

    public void setPriceTrue(double priceTrue) {
        this.priceTrue = priceTrue;
    }

    @Override
    public String toString() {
        return "OrderSku{" +
                "id=" + id +
                ", price=" + price +
                ", quans=" + quans +
                ", target=" + target +
                ", priceTrue=" + priceTrue +
                '}';
    }
}
