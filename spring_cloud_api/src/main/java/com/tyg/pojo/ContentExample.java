package com.tyg.pojo;

import java.util.ArrayList;
import java.util.List;

public class ContentExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ContentExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andBangIdIsNull() {
            addCriterion("bang_id is null");
            return (Criteria) this;
        }

        public Criteria andBangIdIsNotNull() {
            addCriterion("bang_id is not null");
            return (Criteria) this;
        }

        public Criteria andBangIdEqualTo(String value) {
            addCriterion("bang_id =", value, "bangId");
            return (Criteria) this;
        }

        public Criteria andBangIdNotEqualTo(String value) {
            addCriterion("bang_id <>", value, "bangId");
            return (Criteria) this;
        }

        public Criteria andBangIdGreaterThan(String value) {
            addCriterion("bang_id >", value, "bangId");
            return (Criteria) this;
        }

        public Criteria andBangIdGreaterThanOrEqualTo(String value) {
            addCriterion("bang_id >=", value, "bangId");
            return (Criteria) this;
        }

        public Criteria andBangIdLessThan(String value) {
            addCriterion("bang_id <", value, "bangId");
            return (Criteria) this;
        }

        public Criteria andBangIdLessThanOrEqualTo(String value) {
            addCriterion("bang_id <=", value, "bangId");
            return (Criteria) this;
        }

        public Criteria andBangIdLike(String value) {
            addCriterion("bang_id like", value, "bangId");
            return (Criteria) this;
        }

        public Criteria andBangIdNotLike(String value) {
            addCriterion("bang_id not like", value, "bangId");
            return (Criteria) this;
        }

        public Criteria andBangIdIn(List<String> values) {
            addCriterion("bang_id in", values, "bangId");
            return (Criteria) this;
        }

        public Criteria andBangIdNotIn(List<String> values) {
            addCriterion("bang_id not in", values, "bangId");
            return (Criteria) this;
        }

        public Criteria andBangIdBetween(String value1, String value2) {
            addCriterion("bang_id between", value1, value2, "bangId");
            return (Criteria) this;
        }

        public Criteria andBangIdNotBetween(String value1, String value2) {
            addCriterion("bang_id not between", value1, value2, "bangId");
            return (Criteria) this;
        }

        public Criteria andLinkIsNull() {
            addCriterion("link is null");
            return (Criteria) this;
        }

        public Criteria andLinkIsNotNull() {
            addCriterion("link is not null");
            return (Criteria) this;
        }

        public Criteria andLinkEqualTo(String value) {
            addCriterion("link =", value, "link");
            return (Criteria) this;
        }

        public Criteria andLinkNotEqualTo(String value) {
            addCriterion("link <>", value, "link");
            return (Criteria) this;
        }

        public Criteria andLinkGreaterThan(String value) {
            addCriterion("link >", value, "link");
            return (Criteria) this;
        }

        public Criteria andLinkGreaterThanOrEqualTo(String value) {
            addCriterion("link >=", value, "link");
            return (Criteria) this;
        }

        public Criteria andLinkLessThan(String value) {
            addCriterion("link <", value, "link");
            return (Criteria) this;
        }

        public Criteria andLinkLessThanOrEqualTo(String value) {
            addCriterion("link <=", value, "link");
            return (Criteria) this;
        }

        public Criteria andLinkLike(String value) {
            addCriterion("link like", value, "link");
            return (Criteria) this;
        }

        public Criteria andLinkNotLike(String value) {
            addCriterion("link not like", value, "link");
            return (Criteria) this;
        }

        public Criteria andLinkIn(List<String> values) {
            addCriterion("link in", values, "link");
            return (Criteria) this;
        }

        public Criteria andLinkNotIn(List<String> values) {
            addCriterion("link not in", values, "link");
            return (Criteria) this;
        }

        public Criteria andLinkBetween(String value1, String value2) {
            addCriterion("link between", value1, value2, "link");
            return (Criteria) this;
        }

        public Criteria andLinkNotBetween(String value1, String value2) {
            addCriterion("link not between", value1, value2, "link");
            return (Criteria) this;
        }

        public Criteria andHotIsNull() {
            addCriterion("hot is null");
            return (Criteria) this;
        }

        public Criteria andHotIsNotNull() {
            addCriterion("hot is not null");
            return (Criteria) this;
        }

        public Criteria andHotEqualTo(Long value) {
            addCriterion("hot =", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotNotEqualTo(Long value) {
            addCriterion("hot <>", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotGreaterThan(Long value) {
            addCriterion("hot >", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotGreaterThanOrEqualTo(Long value) {
            addCriterion("hot >=", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotLessThan(Long value) {
            addCriterion("hot <", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotLessThanOrEqualTo(Long value) {
            addCriterion("hot <=", value, "hot");
            return (Criteria) this;
        }

        public Criteria andHotIn(List<Long> values) {
            addCriterion("hot in", values, "hot");
            return (Criteria) this;
        }

        public Criteria andHotNotIn(List<Long> values) {
            addCriterion("hot not in", values, "hot");
            return (Criteria) this;
        }

        public Criteria andHotBetween(Long value1, Long value2) {
            addCriterion("hot between", value1, value2, "hot");
            return (Criteria) this;
        }

        public Criteria andHotNotBetween(Long value1, Long value2) {
            addCriterion("hot not between", value1, value2, "hot");
            return (Criteria) this;
        }

        public Criteria andChangeIsNull() {
            addCriterion("change is null");
            return (Criteria) this;
        }

        public Criteria andChangeIsNotNull() {
            addCriterion("change is not null");
            return (Criteria) this;
        }

        public Criteria andChangeEqualTo(Integer value) {
            addCriterion("change =", value, "change");
            return (Criteria) this;
        }

        public Criteria andChangeNotEqualTo(Integer value) {
            addCriterion("change <>", value, "change");
            return (Criteria) this;
        }

        public Criteria andChangeGreaterThan(Integer value) {
            addCriterion("change >", value, "change");
            return (Criteria) this;
        }

        public Criteria andChangeGreaterThanOrEqualTo(Integer value) {
            addCriterion("change >=", value, "change");
            return (Criteria) this;
        }

        public Criteria andChangeLessThan(Integer value) {
            addCriterion("change <", value, "change");
            return (Criteria) this;
        }

        public Criteria andChangeLessThanOrEqualTo(Integer value) {
            addCriterion("change <=", value, "change");
            return (Criteria) this;
        }

        public Criteria andChangeIn(List<Integer> values) {
            addCriterion("change in", values, "change");
            return (Criteria) this;
        }

        public Criteria andChangeNotIn(List<Integer> values) {
            addCriterion("change not in", values, "change");
            return (Criteria) this;
        }

        public Criteria andChangeBetween(Integer value1, Integer value2) {
            addCriterion("change between", value1, value2, "change");
            return (Criteria) this;
        }

        public Criteria andChangeNotBetween(Integer value1, Integer value2) {
            addCriterion("change not between", value1, value2, "change");
            return (Criteria) this;
        }

        public Criteria andStatuIsNull() {
            addCriterion("statu is null");
            return (Criteria) this;
        }

        public Criteria andStatuIsNotNull() {
            addCriterion("statu is not null");
            return (Criteria) this;
        }

        public Criteria andStatuEqualTo(String value) {
            addCriterion("statu =", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuNotEqualTo(String value) {
            addCriterion("statu <>", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuGreaterThan(String value) {
            addCriterion("statu >", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuGreaterThanOrEqualTo(String value) {
            addCriterion("statu >=", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuLessThan(String value) {
            addCriterion("statu <", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuLessThanOrEqualTo(String value) {
            addCriterion("statu <=", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuLike(String value) {
            addCriterion("statu like", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuNotLike(String value) {
            addCriterion("statu not like", value, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuIn(List<String> values) {
            addCriterion("statu in", values, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuNotIn(List<String> values) {
            addCriterion("statu not in", values, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuBetween(String value1, String value2) {
            addCriterion("statu between", value1, value2, "statu");
            return (Criteria) this;
        }

        public Criteria andStatuNotBetween(String value1, String value2) {
            addCriterion("statu not between", value1, value2, "statu");
            return (Criteria) this;
        }

        public Criteria andRangeIsNull() {
            addCriterion("range is null");
            return (Criteria) this;
        }

        public Criteria andRangeIsNotNull() {
            addCriterion("range is not null");
            return (Criteria) this;
        }

        public Criteria andRangeEqualTo(Integer value) {
            addCriterion("range =", value, "range");
            return (Criteria) this;
        }

        public Criteria andRangeNotEqualTo(Integer value) {
            addCriterion("range <>", value, "range");
            return (Criteria) this;
        }

        public Criteria andRangeGreaterThan(Integer value) {
            addCriterion("range >", value, "range");
            return (Criteria) this;
        }

        public Criteria andRangeGreaterThanOrEqualTo(Integer value) {
            addCriterion("range >=", value, "range");
            return (Criteria) this;
        }

        public Criteria andRangeLessThan(Integer value) {
            addCriterion("range <", value, "range");
            return (Criteria) this;
        }

        public Criteria andRangeLessThanOrEqualTo(Integer value) {
            addCriterion("range <=", value, "range");
            return (Criteria) this;
        }

        public Criteria andRangeIn(List<Integer> values) {
            addCriterion("range in", values, "range");
            return (Criteria) this;
        }

        public Criteria andRangeNotIn(List<Integer> values) {
            addCriterion("range not in", values, "range");
            return (Criteria) this;
        }

        public Criteria andRangeBetween(Integer value1, Integer value2) {
            addCriterion("range between", value1, value2, "range");
            return (Criteria) this;
        }

        public Criteria andRangeNotBetween(Integer value1, Integer value2) {
            addCriterion("range not between", value1, value2, "range");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}