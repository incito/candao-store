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

public class SimpleCustDishTemplateImpl implements ListenerTemplate{
	
	private Log logger = LogFactory.getLog(SimpleCustDishTemplateImpl.class.getName());
	//模板大小
	private Integer type;

	@Override
	public void setType(Integer type) {
		this.type = type;
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
		// TODO
		return PrinterConstant.VerticalFont();
	}

	@Override
	public String[] getTableMsg(PrintObj obj) throws Exception {
		String[] tableName = { obj.getTableNo() };
		Integer[] tableLength = { 36 };
		String[] table = StringUtils.getLineFeedText(tableName, tableLength);
		trim(table);
		return table;
	}

	@Override
	public Object[] getBodyMsg(PrintObj obj) throws Exception {
		
		return this.getPrintText(obj, 24, 7, 8);
	}

	@Override
	public Object[] getTailMsg(PrintObj obj) throws Exception {
		String[] names = this.checkTealMsg(obj);
		Integer[] lengths = {4,26,8};
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
		return new Integer[]{38};
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
		PrintDish printDish = obj.getpDish().get(0);
		String abbrName = printDish.getAbbrname() == null?"":printDish.getAbbrname();
		String ordersq = obj.getOrderseq() == 0 ? "": "第"+obj.getOrderseq()+"张";
		String timestamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		return new String[]{abbrName,ordersq,timestamp};
	}
	
//	private Object[] getPrintText(PrintObj object, int num1, int num2, int num3) throws Exception {
//		Object[] res = null;
//		List<PrintDish> list = object.getpDish();
//		
//		for (PrintDish it : list) {
//			// 校验名称
//			String dishName = it.getDishName() == null ? "" : it.getDishName();
//			String dishNum = it.getDishNum() == null ? "" : it.getDishNum();
//			String dishUnit = it.getDishUnit() == null ? "" : it.getDishUnit();
//			String taste = it.getTaste() != null && !it.getTaste().isEmpty()? "(" + it.getTaste().trim()+")":"";
//			String Sperequire = it.getSperequire() != null && !it.getSperequire().isEmpty()? "(" + it.getSperequire().trim()+")":"";
//			dishName += taste+Sperequire;
//			logger.error("------------------------");
//			logger.error("订单号："+object.getOrderNo()+"*打印菜品：" + it.getDishName());
//			
//			if (2 == it.getDishtype()) {
//				dishName = "（套）"+dishName;
//			}
//			
//			String[] name = { dishName, dishNum, dishUnit };
//			Integer[] len = { num1, num2, num3 };
//			
//			String[] temp = StringUtils.getLineFeedText(name, len);
//			
//			res = ArrayUtils.addAll(res, temp);
//		}
//		
//		return res;
//	}
	
	private Object[] getPrintText(PrintObj object, int num1, int num2, int num3) throws Exception {
		Object[] res = null;
		List<PrintDish> list = object.getList();

		for (PrintDish it : list) {
			// 校验名称
			String dishName = it.getDishName() == null ? "" : it.getDishName();
			String dishNum = it.getDishNum() == null ? "" : it.getDishNum();
			String dishPrice = it.getDishPrice() == null ? "" : it.getDishPrice().toString();
			String dishUnit = it.getDishUnit() == null ? "" : "(" + it.getDishUnit() + ")";
			String taste = it.getTaste() != null && !it.getTaste().isEmpty() ? "(" + it.getTaste().trim() + ")" : "";
			String Sperequire = it.getSperequire() != null && !it.getSperequire().isEmpty()
					? "(" + it.getSperequire().trim() + ")" : "";
			// 判断是否赠菜
			if(it.getPrintport()!=null && !it.getPrintport().isEmpty()){
				if ("1".equals(it.getPrintport().trim())) {
					dishName += ("赠");
				}				
			}

			dishName += taste + Sperequire + dishUnit;

			String[] name = { dishName, dishNum, dishPrice };
			Integer[] len = { num1, num2, num3 };

			String[] temp = StringUtils.getLineFeedText(name, len);

			res = ArrayUtils.addAll(res, temp);
		}

		return res;
	}

}
