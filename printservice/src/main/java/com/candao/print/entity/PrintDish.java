package com.candao.print.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.springframework.util.StringUtils;

import com.candao.common.utils.Constant;

public class PrintDish implements Serializable, Comparable<PrintDish> {

    /**
     *
     */
    private static final long serialVersionUID = 5744979808038381094L;


    @Override
    public int compareTo(PrintDish targetDish) {

        return (this.orderseq < targetDish.orderseq) ? -1 : 0;
    }


    private String dishName;

    private String dishNum;

    private BigDecimal dishPrice;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private String printerId;
    //排序规则： sperequire|globalsperequire|taste|freeuser|freeauthorize|freereason
    private String sperequire;//菜品忌口信息

    private String tableNomsg;

    private int maxDishCount;

    private String dishUnit;

    private String printipaddress;

    private String printport;

    private String printdishid;

    private String printobjid;

    private String dishId;

    private String relatedishid;

    private int orderseq;

    private String abbrname;

    private String printtype;

    private int dishtype;

    private int ordertype;

    private String parentkey;

    private String superkey;

    private int ismaster;

    private String primarykey;

    private int islatecooke;

    //0 正常下单 1 加单
    private int isadddish;

    //0 单品 1 火锅 2 套餐
    private int childdishtype;

    private int printnum;
    private int ispot;
    //口味
    private String taste;
    //赠菜人
    private String freeuser;
    //赠菜授权人
    private String freeauthorize;
    //赠菜原因
    private String freereason;
    //全单备注
    private String globalsperequire;


    private String parentDishName;


    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getFreeuser() {
        return freeuser;
    }

    public void setFreeuser(String freeuser) {
        this.freeuser = freeuser;
    }

    public String getFreeauthorize() {
        return freeauthorize;
    }

    public void setFreeauthorize(String freeauthorize) {
        this.freeauthorize = freeauthorize;
    }

    public String getFreereason() {
        return freereason;
    }

    public void setFreereason(String freereason) {
        this.freereason = freereason;
    }

    public String getGlobalsperequire() {
        return globalsperequire;
    }

    public void setGlobalsperequire(String globalsperequire) {
        this.globalsperequire = globalsperequire;
    }

    public int getIspot() {
        return ispot;
    }

    public void setIspot(int ispot) {
        this.ispot = ispot;
    }

    public int getPrintnum() {
        return printnum;
    }

    public void setPrintnum(int printnum) {
        this.printnum = printnum;
    }

    public int getChilddishtype() {
        return childdishtype;
    }

    public void setChilddishtype(int childdishtype) {
        this.childdishtype = childdishtype;
    }

    public int getDishtype() {
        return dishtype;
    }

    public void setDishtype(int dishtype) {
        this.dishtype = dishtype;
    }

    public String getPrinttype() {
        return printtype;
    }

    public void setPrinttype(String printtype) {
        this.printtype = printtype;
    }

    public String getAbbrname() {
        return abbrname;
    }

    public void setAbbrname(String abbrname) {
        this.abbrname = abbrname;
    }

    public int getOrderseq() {
        return orderseq;
    }

    public void setOrderseq(int orderseq) {
        this.orderseq = orderseq;
    }

    public String getRelatedishid() {
        return relatedishid;
    }

    public void setRelatedishid(String relatedishid) {
        this.relatedishid = relatedishid;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getTableNomsg() {
        return tableNomsg;
    }

    public void setTableNomsg(String tableNomsg) {
        this.tableNomsg = tableNomsg;
    }


    public String getPrintdishid() {
        return printdishid;
    }

    public void setPrintdishid(String printdishid) {
        this.printdishid = printdishid;
    }

    public String getPrintobjid() {
        return printobjid;
    }

    public void setPrintobjid(String printobjid) {
        this.printobjid = printobjid;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getPrintipaddress() {
        return printipaddress;
    }

    public void setPrintipaddress(String printipaddress) {
        this.printipaddress = printipaddress;
    }

    public String getPrintport() {
        return printport;
    }

    public void setPrintport(String printport) {
        this.printport = printport;
    }

    public String getDishUnit() {
        return dishUnit;
    }

    public void setDishUnit(String dishUnit) {
        this.dishUnit = dishUnit;
    }

    public int getMaxDishCount() {
        return maxDishCount;
    }

    public void setMaxDishCount(int maxDishCount) {
        this.maxDishCount = maxDishCount;
    }

    public String getSperequire() {
        return sperequire;
    }

    public void setSperequire(String sperequire) {
        this.sperequire = sperequire;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishNum() {
        return dishNum;
    }

    public void setDishNum(String dishNum) {
        this.dishNum = dishNum;
    }


    public BigDecimal getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(BigDecimal dishPrice) {
        this.dishPrice = dishPrice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public int getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(int ordertype) {
        this.ordertype = ordertype;
    }

    public String getParentkey() {
        return parentkey;
    }

    public void setParentkey(String parentkey) {
        this.parentkey = parentkey;
    }

    public String getSuperkey() {
        return superkey;
    }

    public void setSuperkey(String superkey) {
        this.superkey = superkey;
    }

    public int getIsmaster() {
        return ismaster;
    }

    public void setIsmaster(int ismaster) {
        this.ismaster = ismaster;
    }

    public String getPrimarykey() {
        return primarykey;
    }

    public void setPrimarykey(String primarykey) {
        this.primarykey = primarykey;
    }

    public int getIslatecooke() {
        return islatecooke;
    }

    public void setIslatecooke(int islatecooke) {
        this.islatecooke = islatecooke;
    }

    public int getIsadddish() {
        return isadddish;
    }

    public void setIsadddish(int isadddish) {
        this.isadddish = isadddish;
    }

    public String getParentDishName() {
        return parentDishName;
    }

    public void setParentDishName(String parentDishName) {
        this.parentDishName = parentDishName;
    }

    /**
     * 根据sperequire初始化相关字段
     *
     * @throws Exception
     */
    public void initData() throws Exception {
        if (this.sperequire != null && !this.sperequire.isEmpty()) {
            //6个特殊需求,数组顺序即为排序规则
            String[] properties = {"sperequire", "globalsperequire", "taste", "freeuser", "freeauthorize", "freereason"};
            String[] sperequires = StringUtils.delimitedListToStringArray(sperequire.trim(), Constant.DELIMITER_SPECIAL);
            for (int i = 0; i < sperequires.length && i < properties.length; i++) {
                Field field = this.getClass().getDeclaredField(properties[i]);
                field.setAccessible(true);
                String[] tempSperequires = StringUtils.delimitedListToStringArray(sperequires[i], Constant.DELIMITER_MULTISPECIAL);
                for (int j = 0; j < tempSperequires.length; j++) {
                    int end = tempSperequires[j].indexOf("#");
                    tempSperequires[j] = tempSperequires[j].substring(0, end == -1 ? tempSperequires[j].length() : end);
                }
                field.set(this, StringUtils.arrayToDelimitedString(tempSperequires, Constant.DELIMITER_MULTISPECIAL));
            }
        }
    }
}
