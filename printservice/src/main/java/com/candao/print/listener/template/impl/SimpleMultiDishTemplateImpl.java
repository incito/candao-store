package com.candao.print.listener.template.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.candao.common.utils.StringUtils;
import com.candao.print.entity.PrintDish;
import com.candao.print.entity.PrintObj;
import com.candao.print.entity.PrinterConstant;
import com.candao.print.listener.template.ListenerTemplate;
import com.candao.print.listener.template.impl.SimpleNormalDishTemplateImpl.Template;

public class SimpleMultiDishTemplateImpl implements ListenerTemplate{
	
	private Log logger = LogFactory.getLog(SimpleMultiDishTemplateImpl.class.getName());
	//模板大小
	private Integer type;
	private Template template;

	@Override
	public void setType(Integer type) {
		this.type = type;
		this.template = new Template(type);
	}

	@Override
	public Integer getType() {
		return type;
	}

	@Override
	public byte[] setAlignCenter() {
		return PrinterConstant.alignCenter();
	}

	@Override
	public byte[] setAlignLeft() {
		return PrinterConstant.alignLeft();
	}

	@Override
	public byte[] setClearfont() {
		return PrinterConstant.getClear_font();
	}

	@Override
	public byte[] getTitleFont() {
		//TODO
		return PrinterConstant.getClear_font();
	}

	@Override
	public byte[] getBodyFont() {
		byte[] rs = null;
		switch (type) {
		case 1:
			rs = PrinterConstant.VerticalFont();
			break;
		case 2:
			rs = PrinterConstant.getClear_font();
			break;
		case 3:
			rs = PrinterConstant.getFdDoubleFont();
			break;
		default:
			rs = PrinterConstant.VerticalFont();
			break;
		}
		return rs;
	}

	@Override
	public String[] getTableMsg(PrintObj obj) throws Exception {
		String[] tableName = { obj.getTableNo() };
		Integer[] tableLength = { template.getTableLength() };
		String[] table = StringUtils.getLineFeedText(tableName, tableLength);
		trim(table);
		return table;
	}

	@Override
	public Object[] getBodyMsg(PrintObj obj) throws Exception {
		Integer[] bodyLength = template.getBodyLength();
		return this.getPrintText(obj, bodyLength[0], bodyLength[1], bodyLength[2]);
	}

	@Override
	public Object[] getTailMsg(PrintObj obj) throws Exception {
		String[] names = this.checkTealMsg(obj);
		Integer[] lengths = template.getTailLength();
		String[] temp = StringUtils.getLineFeedText(names, lengths);
		if (temp!= null && temp.length != 0) {
			for (int i = 0; i < temp.length; i++) {
				temp[i].trim();
			}
		}
		return temp;
	}

	@Override
	public Integer[] getNoteLength() {
		return new Integer[]{template.getNoteLength()};
	}

	@Override
	public Object[] getPrinterPortMsg(PrintObj obj) throws Exception {
		String[] portName = { "(" + obj.getPrintName() + ")" };
		Integer[] len = { 9 };
		String[] temp = StringUtils.getLineFeedText(portName, len);
		trim(temp);
		return temp;
	}
	
	private void trim(String[] temp){
		if (temp!= null && temp.length != 0) {
			for (int i = 0; i < temp.length; i++) {
				temp[i] = temp[i].trim();
			}
		}
	}

	private String[] checkTealMsg(PrintObj obj){
		PrintDish printDish = obj.getList().get(0);
		String abbrName = printDish.getAbbrname() == null?"":printDish.getAbbrname();
		String ordersq = obj.getOrderseq() == 0 ? "": "第"+obj.getOrderseq()+"张";
		String timestamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		return new String[]{abbrName,ordersq,timestamp};
	}
	
	private Object[] getPrintText(PrintObj object, int num1, int num2, int num3) throws Exception {
		Object[] res = null;
		
		List<PrintDish> list = object.getList();

		for (PrintDish it : list) {
			// 校验名称
			String dishName = it.getDishName() == null ? "" : it.getDishName();
			String dishNum = it.getDishNum() == null ? "" : it.getDishNum();
			String dishUnit = it.getDishUnit() == null ? "" : it.getDishUnit();
			String taste = it.getTaste() != null && !it.getTaste().isEmpty()? "(" + it.getTaste().trim()+")":"";
			String Sperequire = it.getSperequire() != null && !it.getSperequire().isEmpty()? "(" + it.getSperequire().trim()+")":"";
			dishName += taste+Sperequire;

			String[] name = { dishName, dishNum, dishUnit };
			Integer[] len = { num1, num2, num3 };

			String[] temp = StringUtils.getLineFeedText(name, len);

			res = ArrayUtils.addAll(res, temp);
		}

		return res;
	}

	class Template{
		private int tableLength;
		private int noteLength;
		private Integer[] bodyLength;
		private Integer[] tailLength;
		
		public Template(int size) {
			switch (size) {
			case 1:
				this.tableLength = 36;
				this.noteLength = 38;
				this.bodyLength = new Integer[]{25,8,8};
				this.tailLength = new Integer[]{9,23,8};
				break;
			case 2:
				this.tableLength = 36;
				this.noteLength = 38;
				this.bodyLength = new Integer[]{25,8,8};
				this.tailLength = new Integer[]{9,23,8};
				break;
			case 3:
				this.tableLength = 18;
				this.noteLength = 19;
				this.bodyLength = new Integer[]{12,5,4};
				this.tailLength = new Integer[]{5,7,8};
				break;
			default:
				break;
			}
		}
		
		public Integer getTableLength(){
			return this.tableLength;
		}
		
		public Integer getNoteLength(){
			return this.noteLength;
		}
		
		public Integer[] getBodyLength(){
			return this.bodyLength;
		}
		
		public Integer[] getTailLength(){
			return this.tailLength;
		}
		
	}

	@Override
	public String[] getSpecTableMsg(PrintObj obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
