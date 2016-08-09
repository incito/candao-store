package com.candao.print.entity;

import java.util.List;
import java.util.Map;

public class TbPrinterManager extends TbPrinter {
	
	private List<TbPrinterArea> tbPrinterAreaList;
	private List<Map<String, Object>> tbPrinterDetailList;
	private List<Map<String, Object>> areaslistTag;
	private List<Map<String, Object>> dishTypeslistTag;
	private List<GroupDishBusiness> groupDishList;
	public List<TbPrinterArea> getTbPrinterAreaList() {
		return tbPrinterAreaList;
	}
	public void setTbPrinterAreaList(List<TbPrinterArea> tbPrinterAreaList) {
		this.tbPrinterAreaList = tbPrinterAreaList;
	}
	public List<Map<String, Object>> getTbPrinterDetailList() {
		return tbPrinterDetailList;
	}
	public void setTbPrinterDetailList(List<Map<String, Object>> tbPrinterDetailList) {
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
	public List<GroupDishBusiness> getGroupDishList() {
		return groupDishList;
	}
	public void setGroupDishList(List<GroupDishBusiness> groupDishList) {
		this.groupDishList = groupDishList;
	}
	
}
