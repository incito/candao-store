package com.candao.www.data.model;

import java.util.ArrayList;
import java.util.List;

public class TPrinterDeviceprinterExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TPrinterDeviceprinterExample() {
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

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andDevicecodeIsNull() {
            addCriterion("devicecode is null");
            return (Criteria) this;
        }

        public Criteria andDevicecodeIsNotNull() {
            addCriterion("devicecode is not null");
            return (Criteria) this;
        }

        public Criteria andDevicecodeEqualTo(String value) {
            addCriterion("devicecode =", value, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicecodeNotEqualTo(String value) {
            addCriterion("devicecode <>", value, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicecodeGreaterThan(String value) {
            addCriterion("devicecode >", value, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicecodeGreaterThanOrEqualTo(String value) {
            addCriterion("devicecode >=", value, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicecodeLessThan(String value) {
            addCriterion("devicecode <", value, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicecodeLessThanOrEqualTo(String value) {
            addCriterion("devicecode <=", value, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicecodeLike(String value) {
            addCriterion("devicecode like", value, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicecodeNotLike(String value) {
            addCriterion("devicecode not like", value, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicecodeIn(List<String> values) {
            addCriterion("devicecode in", values, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicecodeNotIn(List<String> values) {
            addCriterion("devicecode not in", values, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicecodeBetween(String value1, String value2) {
            addCriterion("devicecode between", value1, value2, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicecodeNotBetween(String value1, String value2) {
            addCriterion("devicecode not between", value1, value2, "devicecode");
            return (Criteria) this;
        }

        public Criteria andDevicenameIsNull() {
            addCriterion("devicename is null");
            return (Criteria) this;
        }

        public Criteria andDevicenameIsNotNull() {
            addCriterion("devicename is not null");
            return (Criteria) this;
        }

        public Criteria andDevicenameEqualTo(String value) {
            addCriterion("devicename =", value, "devicename");
            return (Criteria) this;
        }

        public Criteria andDevicenameNotEqualTo(String value) {
            addCriterion("devicename <>", value, "devicename");
            return (Criteria) this;
        }

        public Criteria andDevicenameGreaterThan(String value) {
            addCriterion("devicename >", value, "devicename");
            return (Criteria) this;
        }

        public Criteria andDevicenameGreaterThanOrEqualTo(String value) {
            addCriterion("devicename >=", value, "devicename");
            return (Criteria) this;
        }

        public Criteria andDevicenameLessThan(String value) {
            addCriterion("devicename <", value, "devicename");
            return (Criteria) this;
        }

        public Criteria andDevicenameLessThanOrEqualTo(String value) {
            addCriterion("devicename <=", value, "devicename");
            return (Criteria) this;
        }

        public Criteria andDevicenameLike(String value) {
            addCriterion("devicename like", value, "devicename");
            return (Criteria) this;
        }

        public Criteria andDevicenameNotLike(String value) {
            addCriterion("devicename not like", value, "devicename");
            return (Criteria) this;
        }

        public Criteria andDevicenameIn(List<String> values) {
            addCriterion("devicename in", values, "devicename");
            return (Criteria) this;
        }

        public Criteria andDevicenameNotIn(List<String> values) {
            addCriterion("devicename not in", values, "devicename");
            return (Criteria) this;
        }

        public Criteria andDevicenameBetween(String value1, String value2) {
            addCriterion("devicename between", value1, value2, "devicename");
            return (Criteria) this;
        }

        public Criteria andDevicenameNotBetween(String value1, String value2) {
            addCriterion("devicename not between", value1, value2, "devicename");
            return (Criteria) this;
        }

        public Criteria andPrinteridIsNull() {
            addCriterion("printerid is null");
            return (Criteria) this;
        }

        public Criteria andPrinteridIsNotNull() {
            addCriterion("printerid is not null");
            return (Criteria) this;
        }

        public Criteria andPrinteridEqualTo(String value) {
            addCriterion("printerid =", value, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinteridNotEqualTo(String value) {
            addCriterion("printerid <>", value, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinteridGreaterThan(String value) {
            addCriterion("printerid >", value, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinteridGreaterThanOrEqualTo(String value) {
            addCriterion("printerid >=", value, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinteridLessThan(String value) {
            addCriterion("printerid <", value, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinteridLessThanOrEqualTo(String value) {
            addCriterion("printerid <=", value, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinteridLike(String value) {
            addCriterion("printerid like", value, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinteridNotLike(String value) {
            addCriterion("printerid not like", value, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinteridIn(List<String> values) {
            addCriterion("printerid in", values, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinteridNotIn(List<String> values) {
            addCriterion("printerid not in", values, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinteridBetween(String value1, String value2) {
            addCriterion("printerid between", value1, value2, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinteridNotBetween(String value1, String value2) {
            addCriterion("printerid not between", value1, value2, "printerid");
            return (Criteria) this;
        }

        public Criteria andPrinternameIsNull() {
            addCriterion("printername is null");
            return (Criteria) this;
        }

        public Criteria andPrinternameIsNotNull() {
            addCriterion("printername is not null");
            return (Criteria) this;
        }

        public Criteria andPrinternameEqualTo(String value) {
            addCriterion("printername =", value, "printername");
            return (Criteria) this;
        }

        public Criteria andPrinternameNotEqualTo(String value) {
            addCriterion("printername <>", value, "printername");
            return (Criteria) this;
        }

        public Criteria andPrinternameGreaterThan(String value) {
            addCriterion("printername >", value, "printername");
            return (Criteria) this;
        }

        public Criteria andPrinternameGreaterThanOrEqualTo(String value) {
            addCriterion("printername >=", value, "printername");
            return (Criteria) this;
        }

        public Criteria andPrinternameLessThan(String value) {
            addCriterion("printername <", value, "printername");
            return (Criteria) this;
        }

        public Criteria andPrinternameLessThanOrEqualTo(String value) {
            addCriterion("printername <=", value, "printername");
            return (Criteria) this;
        }

        public Criteria andPrinternameLike(String value) {
            addCriterion("printername like", value, "printername");
            return (Criteria) this;
        }

        public Criteria andPrinternameNotLike(String value) {
            addCriterion("printername not like", value, "printername");
            return (Criteria) this;
        }

        public Criteria andPrinternameIn(List<String> values) {
            addCriterion("printername in", values, "printername");
            return (Criteria) this;
        }

        public Criteria andPrinternameNotIn(List<String> values) {
            addCriterion("printername not in", values, "printername");
            return (Criteria) this;
        }

        public Criteria andPrinternameBetween(String value1, String value2) {
            addCriterion("printername between", value1, value2, "printername");
            return (Criteria) this;
        }

        public Criteria andPrinternameNotBetween(String value1, String value2) {
            addCriterion("printername not between", value1, value2, "printername");
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