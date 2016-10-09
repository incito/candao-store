package com.candao.www.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TPrinterDeviceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TPrinterDeviceExample() {
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

        public Criteria andDevice(Map param){
            if (param.get("deviceid")!= null)
                andDeviceidEqualTo(param.get("deviceid").toString());
            if (param.get("devicecode")!= null)
                andDevicecodeEqualTo(param.get("devicecode").toString());
            if (param.get("devicename")!= null)
                andDevicenameEqualTo(param.get("devicename").toString());
            if (param.get("devicearea")!= null)
                andDeviceareaEqualTo(param.get("devicearea").toString());
            if (param.get("devicestatus")!= null)
                andDevicestatusEqualTo(Integer.valueOf(param.get("devicestatus").toString()));
            if (param.get("devicetype")!= null)
                andDevicetypeEqualTo(Integer.valueOf(param.get("devicetype").toString()));
            if (param.get("deviceowner")!= null)
                andDeviceownerEqualTo(param.get("deviceowner").toString());
            if (param.get("devicegroup")!= null)
                andDevicegroupEqualTo(param.get("devicegroup").toString());
            return (Criteria) this;
        }

        public Criteria andDeviceidIsNull() {
            addCriterion("deviceid is null");
            return (Criteria) this;
        }

        public Criteria andDeviceidIsNotNull() {
            addCriterion("deviceid is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceidEqualTo(String value) {
            addCriterion("deviceid =", value, "deviceid");
            return (Criteria) this;
        }

        public Criteria andDeviceidNotEqualTo(String value) {
            addCriterion("deviceid <>", value, "deviceid");
            return (Criteria) this;
        }

        public Criteria andDeviceidGreaterThan(String value) {
            addCriterion("deviceid >", value, "deviceid");
            return (Criteria) this;
        }

        public Criteria andDeviceidGreaterThanOrEqualTo(String value) {
            addCriterion("deviceid >=", value, "deviceid");
            return (Criteria) this;
        }

        public Criteria andDeviceidLessThan(String value) {
            addCriterion("deviceid <", value, "deviceid");
            return (Criteria) this;
        }

        public Criteria andDeviceidLessThanOrEqualTo(String value) {
            addCriterion("deviceid <=", value, "deviceid");
            return (Criteria) this;
        }

        public Criteria andDeviceidLike(String value) {
            addCriterion("deviceid like", value, "deviceid");
            return (Criteria) this;
        }

        public Criteria andDeviceidNotLike(String value) {
            addCriterion("deviceid not like", value, "deviceid");
            return (Criteria) this;
        }

        public Criteria andDeviceidIn(List<String> values) {
            addCriterion("deviceid in", values, "deviceid");
            return (Criteria) this;
        }

        public Criteria andDeviceidNotIn(List<String> values) {
            addCriterion("deviceid not in", values, "deviceid");
            return (Criteria) this;
        }

        public Criteria andDeviceidBetween(String value1, String value2) {
            addCriterion("deviceid between", value1, value2, "deviceid");
            return (Criteria) this;
        }

        public Criteria andDeviceidNotBetween(String value1, String value2) {
            addCriterion("deviceid not between", value1, value2, "deviceid");
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

        public Criteria andDeviceareaIsNull() {
            addCriterion("devicearea is null");
            return (Criteria) this;
        }

        public Criteria andDeviceareaIsNotNull() {
            addCriterion("devicearea is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceareaEqualTo(String value) {
            addCriterion("devicearea =", value, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDeviceareaNotEqualTo(String value) {
            addCriterion("devicearea <>", value, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDeviceareaGreaterThan(String value) {
            addCriterion("devicearea >", value, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDeviceareaGreaterThanOrEqualTo(String value) {
            addCriterion("devicearea >=", value, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDeviceareaLessThan(String value) {
            addCriterion("devicearea <", value, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDeviceareaLessThanOrEqualTo(String value) {
            addCriterion("devicearea <=", value, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDeviceareaLike(String value) {
            addCriterion("devicearea like", value, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDeviceareaNotLike(String value) {
            addCriterion("devicearea not like", value, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDeviceareaIn(List<String> values) {
            addCriterion("devicearea in", values, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDeviceareaNotIn(List<String> values) {
            addCriterion("devicearea not in", values, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDeviceareaBetween(String value1, String value2) {
            addCriterion("devicearea between", value1, value2, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDeviceareaNotBetween(String value1, String value2) {
            addCriterion("devicearea not between", value1, value2, "devicearea");
            return (Criteria) this;
        }

        public Criteria andDevicestatusIsNull() {
            addCriterion("devicestatus is null");
            return (Criteria) this;
        }

        public Criteria andDevicestatusIsNotNull() {
            addCriterion("devicestatus is not null");
            return (Criteria) this;
        }

        public Criteria andDevicestatusEqualTo(Integer value) {
            addCriterion("devicestatus =", value, "devicestatus");
            return (Criteria) this;
        }

        public Criteria andDevicestatusNotEqualTo(Integer value) {
            addCriterion("devicestatus <>", value, "devicestatus");
            return (Criteria) this;
        }

        public Criteria andDevicestatusGreaterThan(Integer value) {
            addCriterion("devicestatus >", value, "devicestatus");
            return (Criteria) this;
        }

        public Criteria andDevicestatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("devicestatus >=", value, "devicestatus");
            return (Criteria) this;
        }

        public Criteria andDevicestatusLessThan(Integer value) {
            addCriterion("devicestatus <", value, "devicestatus");
            return (Criteria) this;
        }

        public Criteria andDevicestatusLessThanOrEqualTo(Integer value) {
            addCriterion("devicestatus <=", value, "devicestatus");
            return (Criteria) this;
        }

        public Criteria andDevicestatusIn(List<Integer> values) {
            addCriterion("devicestatus in", values, "devicestatus");
            return (Criteria) this;
        }

        public Criteria andDevicestatusNotIn(List<Integer> values) {
            addCriterion("devicestatus not in", values, "devicestatus");
            return (Criteria) this;
        }

        public Criteria andDevicestatusBetween(Integer value1, Integer value2) {
            addCriterion("devicestatus between", value1, value2, "devicestatus");
            return (Criteria) this;
        }

        public Criteria andDevicestatusNotBetween(Integer value1, Integer value2) {
            addCriterion("devicestatus not between", value1, value2, "devicestatus");
            return (Criteria) this;
        }

        public Criteria andDevicetypeIsNull() {
            addCriterion("devicetype is null");
            return (Criteria) this;
        }

        public Criteria andDevicetypeIsNotNull() {
            addCriterion("devicetype is not null");
            return (Criteria) this;
        }

        public Criteria andDevicetypeEqualTo(Integer value) {
            addCriterion("devicetype =", value, "devicetype");
            return (Criteria) this;
        }

        public Criteria andDevicetypeNotEqualTo(Integer value) {
            addCriterion("devicetype <>", value, "devicetype");
            return (Criteria) this;
        }

        public Criteria andDevicetypeGreaterThan(Integer value) {
            addCriterion("devicetype >", value, "devicetype");
            return (Criteria) this;
        }

        public Criteria andDevicetypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("devicetype >=", value, "devicetype");
            return (Criteria) this;
        }

        public Criteria andDevicetypeLessThan(Integer value) {
            addCriterion("devicetype <", value, "devicetype");
            return (Criteria) this;
        }

        public Criteria andDevicetypeLessThanOrEqualTo(Integer value) {
            addCriterion("devicetype <=", value, "devicetype");
            return (Criteria) this;
        }

        public Criteria andDevicetypeIn(List<Integer> values) {
            addCriterion("devicetype in", values, "devicetype");
            return (Criteria) this;
        }

        public Criteria andDevicetypeNotIn(List<Integer> values) {
            addCriterion("devicetype not in", values, "devicetype");
            return (Criteria) this;
        }

        public Criteria andDevicetypeBetween(Integer value1, Integer value2) {
            addCriterion("devicetype between", value1, value2, "devicetype");
            return (Criteria) this;
        }

        public Criteria andDevicetypeNotBetween(Integer value1, Integer value2) {
            addCriterion("devicetype not between", value1, value2, "devicetype");
            return (Criteria) this;
        }

        public Criteria andDeviceipIsNull() {
            addCriterion("deviceip is null");
            return (Criteria) this;
        }

        public Criteria andDeviceipIsNotNull() {
            addCriterion("deviceip is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceipEqualTo(String value) {
            addCriterion("deviceip =", value, "deviceip");
            return (Criteria) this;
        }

        public Criteria andDeviceipNotEqualTo(String value) {
            addCriterion("deviceip <>", value, "deviceip");
            return (Criteria) this;
        }

        public Criteria andDeviceipGreaterThan(String value) {
            addCriterion("deviceip >", value, "deviceip");
            return (Criteria) this;
        }

        public Criteria andDeviceipGreaterThanOrEqualTo(String value) {
            addCriterion("deviceip >=", value, "deviceip");
            return (Criteria) this;
        }

        public Criteria andDeviceipLessThan(String value) {
            addCriterion("deviceip <", value, "deviceip");
            return (Criteria) this;
        }

        public Criteria andDeviceipLessThanOrEqualTo(String value) {
            addCriterion("deviceip <=", value, "deviceip");
            return (Criteria) this;
        }

        public Criteria andDeviceipLike(String value) {
            addCriterion("deviceip like", value, "deviceip");
            return (Criteria) this;
        }

        public Criteria andDeviceipNotLike(String value) {
            addCriterion("deviceip not like", value, "deviceip");
            return (Criteria) this;
        }

        public Criteria andDeviceipIn(List<String> values) {
            addCriterion("deviceip in", values, "deviceip");
            return (Criteria) this;
        }

        public Criteria andDeviceipNotIn(List<String> values) {
            addCriterion("deviceip not in", values, "deviceip");
            return (Criteria) this;
        }

        public Criteria andDeviceipBetween(String value1, String value2) {
            addCriterion("deviceip between", value1, value2, "deviceip");
            return (Criteria) this;
        }

        public Criteria andDeviceipNotBetween(String value1, String value2) {
            addCriterion("deviceip not between", value1, value2, "deviceip");
            return (Criteria) this;
        }

        public Criteria andInserttimeIsNull() {
            addCriterion("inserttime is null");
            return (Criteria) this;
        }

        public Criteria andInserttimeIsNotNull() {
            addCriterion("inserttime is not null");
            return (Criteria) this;
        }

        public Criteria andInserttimeEqualTo(Date value) {
            addCriterion("inserttime =", value, "inserttime");
            return (Criteria) this;
        }

        public Criteria andInserttimeNotEqualTo(Date value) {
            addCriterion("inserttime <>", value, "inserttime");
            return (Criteria) this;
        }

        public Criteria andInserttimeGreaterThan(Date value) {
            addCriterion("inserttime >", value, "inserttime");
            return (Criteria) this;
        }

        public Criteria andInserttimeGreaterThanOrEqualTo(Date value) {
            addCriterion("inserttime >=", value, "inserttime");
            return (Criteria) this;
        }

        public Criteria andInserttimeLessThan(Date value) {
            addCriterion("inserttime <", value, "inserttime");
            return (Criteria) this;
        }

        public Criteria andInserttimeLessThanOrEqualTo(Date value) {
            addCriterion("inserttime <=", value, "inserttime");
            return (Criteria) this;
        }

        public Criteria andInserttimeIn(List<Date> values) {
            addCriterion("inserttime in", values, "inserttime");
            return (Criteria) this;
        }

        public Criteria andInserttimeNotIn(List<Date> values) {
            addCriterion("inserttime not in", values, "inserttime");
            return (Criteria) this;
        }

        public Criteria andInserttimeBetween(Date value1, Date value2) {
            addCriterion("inserttime between", value1, value2, "inserttime");
            return (Criteria) this;
        }

        public Criteria andInserttimeNotBetween(Date value1, Date value2) {
            addCriterion("inserttime not between", value1, value2, "inserttime");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeIsNull() {
            addCriterion("modifiedtime is null");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeIsNotNull() {
            addCriterion("modifiedtime is not null");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeEqualTo(Date value) {
            addCriterion("modifiedtime =", value, "modifiedtime");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeNotEqualTo(Date value) {
            addCriterion("modifiedtime <>", value, "modifiedtime");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeGreaterThan(Date value) {
            addCriterion("modifiedtime >", value, "modifiedtime");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("modifiedtime >=", value, "modifiedtime");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeLessThan(Date value) {
            addCriterion("modifiedtime <", value, "modifiedtime");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeLessThanOrEqualTo(Date value) {
            addCriterion("modifiedtime <=", value, "modifiedtime");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeIn(List<Date> values) {
            addCriterion("modifiedtime in", values, "modifiedtime");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeNotIn(List<Date> values) {
            addCriterion("modifiedtime not in", values, "modifiedtime");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeBetween(Date value1, Date value2) {
            addCriterion("modifiedtime between", value1, value2, "modifiedtime");
            return (Criteria) this;
        }

        public Criteria andModifiedtimeNotBetween(Date value1, Date value2) {
            addCriterion("modifiedtime not between", value1, value2, "modifiedtime");
            return (Criteria) this;
        }

        public Criteria andDeviceownerIsNull() {
            addCriterion("deviceowner is null");
            return (Criteria) this;
        }

        public Criteria andDeviceownerIsNotNull() {
            addCriterion("deviceowner is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceownerEqualTo(String value) {
            addCriterion("deviceowner =", value, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDeviceownerNotEqualTo(String value) {
            addCriterion("deviceowner <>", value, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDeviceownerGreaterThan(String value) {
            addCriterion("deviceowner >", value, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDeviceownerGreaterThanOrEqualTo(String value) {
            addCriterion("deviceowner >=", value, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDeviceownerLessThan(String value) {
            addCriterion("deviceowner <", value, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDeviceownerLessThanOrEqualTo(String value) {
            addCriterion("deviceowner <=", value, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDeviceownerLike(String value) {
            addCriterion("deviceowner like", value, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDeviceownerNotLike(String value) {
            addCriterion("deviceowner not like", value, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDeviceownerIn(List<String> values) {
            addCriterion("deviceowner in", values, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDeviceownerNotIn(List<String> values) {
            addCriterion("deviceowner not in", values, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDeviceownerBetween(String value1, String value2) {
            addCriterion("deviceowner between", value1, value2, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDeviceownerNotBetween(String value1, String value2) {
            addCriterion("deviceowner not between", value1, value2, "deviceowner");
            return (Criteria) this;
        }

        public Criteria andDevicegroupIsNull() {
            addCriterion("devicegroup is null");
            return (Criteria) this;
        }

        public Criteria andDevicegroupIsNotNull() {
            addCriterion("devicegroup is not null");
            return (Criteria) this;
        }

        public Criteria andDevicegroupEqualTo(String value) {
            addCriterion("devicegroup =", value, "devicegroup");
            return (Criteria) this;
        }

        public Criteria andDevicegroupNotEqualTo(String value) {
            addCriterion("devicegroup <>", value, "devicegroup");
            return (Criteria) this;
        }

        public Criteria andDevicegroupGreaterThan(String value) {
            addCriterion("devicegroup >", value, "devicegroup");
            return (Criteria) this;
        }

        public Criteria andDevicegroupGreaterThanOrEqualTo(String value) {
            addCriterion("devicegroup >=", value, "devicegroup");
            return (Criteria) this;
        }

        public Criteria andDevicegroupLessThan(String value) {
            addCriterion("devicegroup <", value, "devicegroup");
            return (Criteria) this;
        }

        public Criteria andDevicegroupLessThanOrEqualTo(String value) {
            addCriterion("devicegroup <=", value, "devicegroup");
            return (Criteria) this;
        }

        public Criteria andDevicegroupLike(String value) {
            addCriterion("devicegroup like", value, "devicegroup");
            return (Criteria) this;
        }

        public Criteria andDevicegroupNotLike(String value) {
            addCriterion("devicegroup not like", value, "devicegroup");
            return (Criteria) this;
        }

        public Criteria andDevicegroupIn(List<String> values) {
            addCriterion("devicegroup in", values, "devicegroup");
            return (Criteria) this;
        }

        public Criteria andDevicegroupNotIn(List<String> values) {
            addCriterion("devicegroup not in", values, "devicegroup");
            return (Criteria) this;
        }

        public Criteria andDevicegroupBetween(String value1, String value2) {
            addCriterion("devicegroup between", value1, value2, "devicegroup");
            return (Criteria) this;
        }

        public Criteria andDevicegroupNotBetween(String value1, String value2) {
            addCriterion("devicegroup not between", value1, value2, "devicegroup");
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