package com.candao.www.data.model;

import java.math.BigDecimal;
import java.util.Date;

public class TdishSet {
    private String id;

    private String masterdishid;

    private String slavedishid;

    private BigDecimal slavedishprice;

    private Date inserttime;

    private String insertuserid;

    private String abbrdesc;

    private Byte status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMasterdishid() {
        return masterdishid;
    }

    public void setMasterdishid(String masterdishid) {
        this.masterdishid = masterdishid;
    }

    public String getSlavedishid() {
        return slavedishid;
    }

    public void setSlavedishid(String slavedishid) {
        this.slavedishid = slavedishid;
    }

    public BigDecimal getSlavedishprice() {
        return slavedishprice;
    }

    public void setSlavedishprice(BigDecimal slavedishprice) {
        this.slavedishprice = slavedishprice;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public String getInsertuserid() {
        return insertuserid;
    }

    public void setInsertuserid(String insertuserid) {
        this.insertuserid = insertuserid;
    }

    public String getAbbrdesc() {
        return abbrdesc;
    }

    public void setAbbrdesc(String abbrdesc) {
        this.abbrdesc = abbrdesc;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}