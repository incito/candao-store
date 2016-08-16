package com.candao.print.listener.template;

import com.candao.print.entity.PrintObj;

public interface ListenerTemplate {

	public static final String BLANK_SPACE = " ";
	
	public static Integer BLANK_SPACE_LENGTH = 1;

	public void setType(Integer type);

	public Integer getType();

	public byte[] setAlignCenter();

	public byte[] setAlignLeft();

	public byte[] setClearfont();

	public byte[] getTitleFont();

	public byte[] getBodyFont();

	public String[] getTableMsg(PrintObj obj) throws Exception;

	public Object[] getBodyMsg(PrintObj obj) throws Exception;

	public Object[] getTailMsg(PrintObj obj) throws Exception;

	public Integer[] getNoteLength();

	public Object[] getPrinterPortMsg(PrintObj obj) throws Exception;

	public String[] getSpecTableMsg(PrintObj obj) throws Exception;
}
