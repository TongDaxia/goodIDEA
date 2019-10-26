package com.test.promotion.price.decorator;

import com.test.promotion.price.pojo.OrderDetail;

import java.math.BigDecimal;

/**
 * 计算支付金额接口类
 * @author admin
 *
 */
public interface IBaseCount {
	
	BigDecimal countPayMoney(OrderDetail orderDetail);

}
