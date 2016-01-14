package com.candao.print.entity;

import java.util.List;
import java.util.Map;

public class TbPrinterManager extends TbPrinter {
	
	private List<TbPrinterArea> tbPrinterAreaList;
	private List<TbPrinterDetail> tbPrinterDetailList;
	private List<Map<String, Object>> areaslistTag;
	private List<Map<String, Object>> dishTypeslistTag;
	public List<TbPrinterArea> getTbPrinterAreaList() {
		return tbPrinterAreaList;
	}
	public void setTbPrinterAreaList(List<TbPrinterArea> tbPrinterAreaList) {
		this.tbPrinterAreaList = tbPrinterAreaList;
	}
	public List<TbPrinterDetail> getTbPrinterDetailList() {
		return tbPrinterDetailList;
	}
	public void setTbPrinterDetailList(List<TbPrinterDetail> tbPrinterDetailList) {
		this.tbPrinterDetailList = tbPrinterDetailList;
	}
	public List<Map<String, Object>> getAreaslistTag() {
		return areaslistTag;
	}
	public void setAreaslistTag(List<Map<String, Object>> areaslistTag) {
		this.areaslistTag = areaslistTag;
	}
	public List<Map<String, Object>> getDishTypeslistTag() {
		return dishTypeslistTag;
	}
	public void setDishTypeslistTag(List<Map<String, Object>> dishTypeslistTag) {
		this.dishTypeslistTag = dishTypeslistTag;
	}

	
}
