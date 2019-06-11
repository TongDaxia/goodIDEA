package com.tyg.util;


import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class MongoUtil {
	
	private static Logger logger = Logger.getLogger(MongoUtil.class);
	
	public static final String ORDER_ASC = "asc";
	public static final String ORDER_DESC = "desc";
	
	/**
	 * 支持str查询条件
	 * 基本查询条件(cell)格式: [字段名称]+[oprator]+[值]，如 a>1
	 * 基本查询条件之间可以用关系运算符(&,|)连接， 如 a>1|b<3
	 * 支持单层括号提升运算优先级，如 a>1(b<3|c=2)|(d!=4&e<=5)
	 * 由括号包裹的条件之间可以省略关系运算符，省略时等同于&
	 * 
	 */
	public static final String OPRATOR_EQ = "=";
	public static final String OPRATOR_NE = "!=";
	public static final String OPRATOR_GT = ">";
	public static final String OPRATOR_GTE = ">=";
	public static final String OPRATOR_LT = "<";
	public static final String OPRATOR_LTE = "<=";
	
	public static final String OPRATOR_AND = "&";
	public static final String OPRATOR_OR = "|";
	private static final String REGEX_OR = "\\|";
	private static final String REGEX_RELATION = OPRATOR_AND + OPRATOR_OR + REGEX_OR;
	
	private static final String REGEX_BRACKETS = "\\(|\\)";
	
	public static <T> List<T> query(Class<T> tClass, MongoTemplate template, String... str){
		if(!checkCollection(tClass, template)){
			logger.error("collection NOT exist! bean is " + tClass.getName());
			return null;
		}
		if(ArrayUtils.isEmpty(str)){
			return template.findAll(tClass);
		}
		
		Query query = getQueryFromStr(str[0]);
		if(str.length > 1){
			query = addOrder(query, str[1]);
		}
		
		return template.find(query, tClass);
	}
	
	public static <T> T queryOne(Class<T> tClass, MongoTemplate template, String str){
		
		if(!checkCollection(tClass, template)){
			logger.error("collection NOT exist! bean is " + tClass.getName());
			return null;
		}
		Query query = new Query();
		if(StringUtils.isNotEmpty(str)){
			query = getQueryFromStr(str);
		}
		
		return template.findOne(query, tClass);
	}
	
	public static <T> boolean checkCollection(Class<T> tClass, MongoTemplate template){
		return template.collectionExists(tClass);
	}
	
	public static Query addOrder(Query query, String orderStr){
		if(StringUtils.isEmpty(orderStr)){
			return query;
		}
		
		Order[] orders = getOrdersFromStr(orderStr);
		query.with(new Sort(orders));
		return query;
	}
	
	public static Order[] getOrdersFromStr(String orderStr){
		String[] arr = orderStr.split(OPRATOR_AND);
		Order[] orders = new Order[arr.length];
		for(int i=0; i<arr.length; i++){
			orders[i] = getOrderFromStrCell(arr[i]);
		}
		return orders;
	}
	
	public static Order getOrderFromStrCell(String cell){
		String[] arr = cell.split(OPRATOR_EQ);
		if(2 == arr.length){
			if(ORDER_DESC.equalsIgnoreCase(arr[1])){
				return new Order(Direction.DESC, arr[0]);
			}
		}
		return new Order(arr[0]);
	}
	
	public static Query getQueryFromStr(String str){
		if(StringUtils.isEmpty(str)){
			return new Query();
		}
		Criteria criteria = getCriteriaFromStr(str);
		return new Query(criteria);
		
	}
	
	public static Criteria getCriteriaFromStr(String str){
		
		String [] arr = str.split(REGEX_BRACKETS);
		Criteria lastCriteria = null;
		String lastOprator = OPRATOR_AND;
		for(String part : arr){
			
			if(part.startsWith(OPRATOR_OR)){
				lastOprator = OPRATOR_OR;
			}
			if(part.startsWith(OPRATOR_AND)){
				lastOprator = OPRATOR_AND;
			}
			
			if(1 == part.length()){
				continue;
			}
			
			if(null == lastCriteria){
				lastCriteria = getCriteriaFromStrPart(part);
			} else {
				lastCriteria = mergeCriteria(lastOprator, lastCriteria, getCriteriaFromStrPart(part));
			}
			
			if(part.endsWith(OPRATOR_OR)){
				lastOprator = OPRATOR_OR;
			} else {
				lastOprator = OPRATOR_AND;
			}
		}
		return lastCriteria;
	}
	
	public static Criteria getCriteriaFromStrPart(String part){
		String [] arr = part.split(REGEX_RELATION);
		Criteria lastCriteria = null;
		
		int opratorPosition = arr[0].length();
		for(String cell : arr){
			if(StringUtils.isNotBlank(cell)){
				if(null == lastCriteria){
					lastCriteria = getCriteriaFromStrCell(cell);
				} else {
					String oprator = ""+part.charAt(opratorPosition);
					lastCriteria = mergeCriteria(oprator, lastCriteria, getCriteriaFromStrCell(cell));
					opratorPosition += cell.length() + 1;
				}
				
			}
		}
		return lastCriteria;
	}
	
	public static Criteria mergeCriteria(String oprator, Criteria criteria1, Criteria criteria2){
		Criteria criteria = new Criteria();
		if(oprator == OPRATOR_OR){
			criteria.orOperator(criteria1, criteria2);
		} else {
			criteria.andOperator(criteria1, criteria2);
		}
		return criteria;
	}
	
	public static Criteria getCriteriaFromStrCell(String cell){
		if(cell.contains(OPRATOR_NE)){
			String [] arr = cell.split(OPRATOR_NE);
			if(2 == arr.length){
				return new Criteria(arr[0]).ne(getValue(arr[1]));
			} else {
				return new Criteria(arr[0]).ne("");
			}
		}
		if(cell.contains(OPRATOR_GTE)){
			String [] arr = cell.split(OPRATOR_GTE);
			if(2 == arr.length){
				return new Criteria(arr[0]).gte(getValue(arr[1]));
			} else {
				return new Criteria(arr[0]).gte("");
			}
		}
		if(cell.contains(OPRATOR_LTE)){
			String [] arr = cell.split(OPRATOR_LTE);
			if(2 == arr.length){
				return new Criteria(arr[0]).lte(getValue(arr[1]));
			} else {
				return new Criteria(arr[0]).lte("");
			}
		}
		if(cell.contains(OPRATOR_GT)){
			String [] arr = cell.split(OPRATOR_GT);
			if(2 == arr.length){
				return new Criteria(arr[0]).gt(getValue(arr[1]));
			} else {
				return new Criteria(arr[0]).gt("");
			}
		}
		if(cell.contains(OPRATOR_LT)){
			String [] arr = cell.split(OPRATOR_LT);
			if(2 == arr.length){
				return new Criteria(arr[0]).lt(getValue(arr[1]));
			} else {
				return new Criteria(arr[0]).lt("");
			}
		}
		if(cell.contains(OPRATOR_EQ)){
			String [] arr = cell.split(OPRATOR_EQ);
			if(2 == arr.length){
				return new Criteria(arr[0]).is(getValue(arr[1]));
			} else {
				return new Criteria(arr[0]).is("");
			}
		}
		return new Criteria();
	}
	
	private static Object getValue(String str){
		if(str.startsWith("'")){
			return str.replaceAll("'", "");
		} else {
			try {
				double value = Double.parseDouble(str);
				return value;
			} catch (Exception ignore) {
				return str;
			}
		}
	}
	
	
	public static void main(String[] args) {
		String test = "x=1|(a=b&c=d)&(y=2|z=3)p=4|q=5";
		Criteria criteria = getCriteriaFromStr(test);
		
		System.out.println(criteria);

	}
	
}
