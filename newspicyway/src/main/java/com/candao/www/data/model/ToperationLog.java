package com.candao.www.data.model;

public class ToperationLog {
    private String id;

    private String tableno;

    private int operationtype;

    private String sequence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTableno() {
		return tableno;
	}

	public void setTableno(String tableno) {
		this.tableno = tableno;
	}

    public int getOperationtype() {
		return operationtype;
	}

	public void setOperationtype(int operationtype) {
		this.operationtype = operationtype;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

}