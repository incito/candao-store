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

public class SimpleNormalDishTemplateImpl implements ListenerTemplate {

	public final Log logger = LogFactory.getLog(getClass().getName());

	private Template template;
	// 模板大小
	private Integer type;

	@Override
	public void setType(Integer type) {
		this.type = type;
		template = new Template(type);
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
		// TODO
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

	public byte[] getBIGFONT() {
		return PrinterConstant.getFdDoubleFont();
	}

	public byte[] getNORMALFONT() {
		return PrinterConstant.VerticalFont();
	}

	public byte[] getSMALLFONT() {
		return PrinterConstant.getClear_font();
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
		if (temp != null && temp.length != 0) {
			for (int i = 0; i < temp.length; i++) {
				temp[i].trim();
			}
		}
		return temp;
	}

	@Override
	public Integer[] getNoteLength() {
		return new Integer[] { template.getNoteLength() };
	}

	@Override
	public Object[] getPrinterPortMsg(PrintObj obj) throws Exception {
		String[] portName = { "(" + obj.getPrintName() + ")" };
		Integer[] len = { 21 };
		String[] temp = StringUtils.getLineFeedText(portName, len);
		trim(temp);
		return temp;
	}

	private void trim(String[] temp) {
		if (temp != null && temp.length != 0) {
			for (int i = 0; i < temp.length; i++) {
				temp[i] = temp[i].trim();
			}
		}
	}

	private String[] checkTealMsg(PrintObj obj) {
		PrintDish printDish = obj.getpDish().get(0);
		String abbrName = printDish.getAbbrname() == null ? "" : printDish.getAbbrname();
		String ordersq = obj.getOrderseq() == 0 ? "" : "第" + obj.getOrderseq() + "张";
		String timestamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		return new String[] { abbrName, ordersq, timestamp };
	}

	private Object[] getPrintText(PrintObj object, int num1, int num2, int num3) throws Exception {
		Object[] res = null;
		List<PrintDish> list = object.getpDish();

		for (PrintDish it : list) {
			// 校验名称
			String dishName = it.getDishName() == null ? "" : it.getDishName();
			String dishNum = it.getDishNum() == null ? "" : it.getDishNum();
			String dishUnit = it.getDishUnit() == null ? "" : it.getDishUnit();
			String taste = it.getTaste() != null && !it.getTaste().isEmpty() ? "(" + it.getTaste().trim() + ")" : "";
			String Sperequire = it.getSperequire() != null && !it.getSperequire().isEmpty()
					? "(" + it.getSperequire().trim() + ")" : "";
			dishName += taste + Sperequire;
			logger.error("------------------------");
			logger.error("订单号：" + object.getOrderNo() + "*打印菜品：" + it.getDishName());

			if (2 == it.getDishtype()) {
				dishName = "（套）" + dishName;
			}

			String[] name = { dishName, BLANK_SPACE, dishNum, BLANK_SPACE, dishUnit };
			Integer[] len = { num1, BLANK_SPACE_LENGTH, num2, BLANK_SPACE_LENGTH, num3 };

			String[] temp = StringUtils.getLineFeedText(name, len);

			res = ArrayUtils.addAll(res, temp);
		}

		return res;
	}

	class Template {
		private int tableLength;
		private int noteLength;
		private int speTableLength;
		private Integer[] bodyLength;
		private Integer[] tailLength;
		// private int NoteLength;
		// private int NoteLength;

		public Template(int size) {
			switch (size) {
			case 1:
				this.tableLength = 40;
				this.noteLength = 38;
				this.speTableLength = 40;
				this.bodyLength = new Integer[] { 24, 7, 8 };
				this.tailLength = new Integer[] { 9, 24, 8 };
				break;
			case 2:
				this.tableLength = 40;
				this.noteLength = 38;
				this.speTableLength = 40;
				this.bodyLength = new Integer[] { 24, 7, 8 };
				this.tailLength = new Integer[] { 9, 24, 8 };
				break;
			case 3:
				this.tableLength = 20;
				this.noteLength = 19;
				this.speTableLength = 20;
				this.bodyLength = new Integer[] { 11, 4, 4 };
				this.tailLength = new Integer[] { 5, 8, 8 };
				break;
			default:
				break;
			}
		}

		public Integer getTableLength() {
			return this.tableLength;
		}

		public Integer getNoteLength() {
			return this.noteLength;
		}

		public Integer[] getBodyLength() {
			return this.bodyLength;
		}

		public Integer[] getTailLength() {
			return this.tailLength;
		}

		public Integer getSpeTableLength() {
			return this.speTableLength;
		}

	}

	@Override
	public String[] getSpecTableMsg(PrintObj obj) throws Exception {
		PrintDish printDish = obj.getpDish().get(0);
		String special = printDish.getSperequire();
		String[] arraySpec = special.split(",");
		String subMsg = "";
		String subTableMsg = "桌号：";
		for (String spec : arraySpec) {
			if (!spec.contains("[")) {
				continue;
			} else {
				subMsg = spec.substring(1, spec.length() - 1);
				break;
			}
		}
		String prefixMsg = " (" + subMsg.split("-")[0] + "台送)";
		subMsg = subMsg.split("-")[1];

		String[] tableName = { subTableMsg + subMsg + prefixMsg };
		Integer[] tableLength = { template.getSpeTableLength() };
		String[] table = StringUtils.getLineFeedText(tableName, tableLength);

		return table;
	}

}
