package com.candao.print.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.candao.common.utils.Constant;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class SettlementInfo4Pos implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 662544845230364134L;

    private List<PosJS> jSJson;

    private List<PosDish> listJson;

    private List<OrderInfo4Pos> orderJson;
    private Object doubleJson;

    private String workdate;

    private String data;

    private String info;

    private String branchName;

    private String tel;

    private String address;

    private List<SettlementInfo> settlementInfos;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PosJS> getJSJson() {
        return jSJson;
    }

    public void setJSJson(List<PosJS> jSJson) {
        this.jSJson = jSJson;
    }

    public List<PosDish> getListJson() {
        return listJson;
    }

    public void setListJson(List<PosDish> listJson) {
        this.listJson = listJson;
    }

    public List<OrderInfo4Pos> getOrderJson() {
        return orderJson;
    }

    public void setOrderJson(List<OrderInfo4Pos> orderJson) {
        this.orderJson = orderJson;
    }

    public String getWorkdate() {
        return workdate;
    }

    public void setWorkdate(String workdate) {
        this.workdate = workdate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public List<SettlementInfo> getSettlementInfos() {
        return settlementInfos;
    }

    public void setSettlementInfos(List<SettlementInfo> settlementInfos) {
        this.settlementInfos = settlementInfos;
    }

    public Object getDoubleJson() {
        return doubleJson;
    }

    public void setDoubleJson(Object doubleJson) {
        this.doubleJson = doubleJson;
    }

    public void init(int type) {
        // 品项初始化
        if (!CollectionUtils.isEmpty(this.listJson)) {
            for (PosDish it : listJson) {
                String title = resolveType(it.getTitle());
                String dishnum = resolveType(it.getDishnum());
                String dishunit = resolveType(it.getDishunit());

                title = com.candao.common.utils.StringUtils.resolveBilingualMark("#", title, "1".equals(it.getPricetype()) ? "(赠)" : "");
                dishunit = com.candao.common.utils.StringUtils.resolveBilingualMark("#", dishunit);
                it.setTitle(title);
                it.setDishunit(dishunit);
                it.setDishnum(dishnum);
            }
        }
        // 结算信息初始化
        if (!CollectionUtils.isEmpty(this.orderJson)) {
            settlementInfos = new ArrayList<>();
            OrderInfo4Pos orderinfo = orderJson.get(0);
            String dueamount = orderinfo.getDueamount();
            String ssamount = orderinfo.getSsamount();
            if (!StringUtils.isEmpty(dueamount)) {
                settlementInfos.add(getSettlementInfo("总额:", "￥" + dueamount));
            }
            switch (type) {
                case 1: {
                    if (!StringUtils.isEmpty(orderinfo.getPayamount2()) && 0 != new BigDecimal(orderinfo.getPayamount2()).compareTo(new BigDecimal("0"))) {
                        settlementInfos.add(getSettlementInfo("四舍五入:", "￥" + orderinfo.getPayamount2()));
                    }
                    break;
                }
                case 2: {
                    if (!StringUtils.isEmpty(orderinfo.getPayamount()) && 0 != new BigDecimal(orderinfo.getPayamount()).compareTo(new BigDecimal("0"))) {
                        settlementInfos.add(getSettlementInfo("抹零:", "￥" + orderinfo.getPayamount()));
                    }
                    break;
                }
                default:
                    break;

            }
            if (!StringUtils.isEmpty(orderinfo.getTipPaid()) && 0 != new BigDecimal(orderinfo.getTipPaid()).compareTo(new BigDecimal("0"))) {
                settlementInfos.add(getSettlementInfo("小费:", "￥" + orderinfo.getTipPaid()));
            }
            if (!StringUtils.isEmpty(orderinfo.getZdAmount()) && 0 != new BigDecimal(orderinfo.getZdAmount()).compareTo(new BigDecimal("0"))) {
                settlementInfos.add(getSettlementInfo("赠送金额:", "￥" + orderinfo.getZdAmount()));
            }
            String amount = new BigDecimal(dueamount).subtract(new BigDecimal(ssamount)).toString();
//            if (amount >= 0) {
            //总优惠默认显示
            settlementInfos.add(getSettlementInfo("总优惠:", "￥" + (StringUtils.isEmpty(amount) ? "0.00" : amount)));
//            }
            settlementInfos.add(getSettlementInfo("实收:", "￥" + ssamount));
        }

    }

    private SettlementInfo getSettlementInfo(String name, String value) {
        SettlementInfo info = new SettlementInfo();
        info.setName(name);
        info.setValue(value);
        return info;
    }

    private String resolveType(Object src) {
        return com.candao.common.utils.StringUtils.resolveNullType(src);
    }

}
