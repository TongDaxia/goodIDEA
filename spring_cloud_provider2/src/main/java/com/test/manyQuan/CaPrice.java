package com.test.manyQuan;

import java.util.*;

public class CaPrice {


    public static void main(String[] args) {

        Quan A = new Quan();
        A.setId("A");
        A.setLow(15);
        A.setSubprice(5);
        Quan B = new Quan();
        B.setId("B");
        B.setLow(20);
        B.setSubprice(7);
        Quan C = new Quan();
        C.setId("C");
        C.setLow(30);
        C.setSubprice(9);
        Quan D = new Quan();
        D.setId("D");
        D.setLow(25);
        D.setSubprice(6);


        //计算最优价格
        OrderSku o1 = new OrderSku();
        o1.setPrice(10);
        ArrayList a1 = new ArrayList<Quan>();
        a1.add(A);
        a1.add(B);
        o1.setQuans(a1);

        OrderSku o2 = new OrderSku();
        o2.setPrice(20);
        ArrayList a2 = new ArrayList<Quan>();
        a2.add(A);
        a2.add(C);
        o2.setQuans(a2);

        OrderSku o3 = new OrderSku();
        o3.setPrice(15);
        ArrayList a3 = new ArrayList<Quan>();
        a3.add(B);
        a3.add(C);
        a3.add(D);
        o3.setQuans(a3);

        OrderSku o4 = new OrderSku();
        o4.setPrice(30);
        ArrayList a4 = new ArrayList<Quan>();
        a4.add(D);
        o4.setQuans(a4);


        List<OrderSku> skus = new ArrayList<>();
        skus.add(o1);
        skus.add(o2);
        skus.add(o3);
        skus.add(o4);
        Collections.sort(skus, new Comparator<OrderSku>() {
            @Override
            public int compare(OrderSku o1, OrderSku o2) {
                return o2.getPrice() - o1.getPrice(); //按照商品价格从高到低
            }
        });

        Order o = new Order();
        o.setSkus(skus);
        System.out.println(o);
        o = calPrice(o);

    }

    private static Order calPrice(Order o) {

        List<Quan> quans = new ArrayList();

        for (OrderSku sku : o.getSkus()) {
            if (sku.getQuans() == null) {
                continue;
            }
            for (Quan quan : sku.getQuans()) {
                if (!quans.contains(quan)) {
                    quans.add(quan);
                }
            }
        }
        Collections.sort(quans, new Comparator<Quan>() {
            @Override
            public int compare(Quan o1, Quan o2) {
                return o1.getValue() - o2.getValue() >= 0 ? 1 : -1;  //按照性价比从大到小
            }
        });
        System.out.println("按照性价比从大到小quans:" + quans);

        HashMap<Quan, List<OrderSku>> quans_order = new HashMap<>();
        //List<OrderSku> no_quans_order = new ArrayList<>();
        //现在的思=思路是手工选券，从性价比最高选起，但是商品从哪个选起也是个问题
        List<OrderSku> skus = o.getSkus();
        for (int i = 0; i < quans.size(); i++) {
            Quan prsentQ = quans.get(i);


            //先检验当前订单能不能有效使用这个券
            boolean flag = false;
            int sum = 0;
            for (OrderSku s : skus) {
                if (s.getTarget() == null &&
                        s.getQuans().contains(prsentQ)) {
                    sum += s.getPrice();
                }
            }
            if (sum >= prsentQ.getSubprice()) {
                flag = true;
            }

            if (!flag) continue;//剔除不能用的券

            int pcurrent = 0;
            List<OrderSku> sks = new ArrayList<>();
            for (OrderSku s : skus) {
                if (s.getQuans().contains(prsentQ) //可以用券
                        && s.getTarget() == null    //每个商品只能用一张券
                        && (pcurrent + s.getPrice() <= prsentQ.getSubprice()
                        || pcurrent < prsentQ.getSubprice())   //不得不用 或者 推荐用券,不白白使用
                        ) {

                    pcurrent += s.getPrice();
                    s.setTarget(prsentQ);
                    sks.add(s);
                } else {

                    s.setPriceTrue(s.getPrice());
                }
            }
            if (pcurrent >= prsentQ.getLow() && sks.size() > 0)
                quans_order.put(prsentQ, sks);
        }

        //计算每个sku的真正价格和订单总价
        for (Quan quan : quans_order.keySet()) {
            List<OrderSku> orderSkus = quans_order.get(quan);

            int total = 0;
            for (OrderSku orderSku : orderSkus) {
                total += orderSku.getPrice();
            }
            for (OrderSku orderSku : orderSkus) {
                int sub = quan.getSubprice() * (orderSku.getPrice() / total);//扣减的钱
                orderSku.setPriceTrue(orderSku.getPrice() - sub);
            }
        }


        System.out.println("最后的优惠券使用情况：");
        for (Quan quan : quans_order.keySet()) {
            System.out.println("券：" + quan.getId());
        }
        System.out.println("订单情况：");
        List<OrderSku> skus1 = o.getSkus();
        for (OrderSku orderSku : skus1) {

            System.out.println("" + orderSku.getPrice() + /*orderSku.getQuans()*/"------->" + orderSku.getPriceTrue());
        }


        return o;

    }
}
