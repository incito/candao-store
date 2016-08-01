package com.candao.print.entity;

import java.io.UnsupportedEncodingException;

public class PrinterConstant {

    public static final String ENCODESTR = "UTF-8";

    public static final String LISTLABEL = "listdata";

    public static final String CUST_TEMPLATE_NAME = "customerbill.vm";
    public static final String COOKIE_TEMPLATE_NAME = "normal.vm";
    public static final String SETTLE_TEMPLATE_NAME = "settlement.vm";
    public static final String ADDDISH_TEMPLATE_NAME = "adddish.vm";
    public static final byte ESC = 27;//换码
    public static final byte GS = 29;//组分隔符
    /**
     * 开启自动状态返回
     */
    public static final byte[] AUTO_STATUS = new byte[]{0x1D, 0x61, (byte)0xFF};
    /**
     * 切纸
     */
    public static final byte[] CUT = new byte[] { 0x1B, 0x69 };
    /**
     * 走纸一行
     */
    public static final byte LINE = 10;
    /**
     * 开钱箱
     */
    public static final byte[] OPEN_CASH={27, 112, 0, 100, 100};

    public static byte[] getClear_font() {

        byte[] clear_font = new byte[3];
        clear_font[0] = 0x1d;
        clear_font[1] = 0x21;
        clear_font[2] = 0000;

        return clear_font;
    }

    /**
     * 打印并向前走纸n行
     * @param n
     * @return
     */
    public static byte[] getLineN(byte n) {
        return new byte[]{27,100,n};
    }


    public static byte[] getFont_B() {/* 纵向放大一倍 */
        byte[] font_b = new byte[3];
        font_b[0] = 27;
        font_b[1] = 33;
        font_b[2] = 8;

        return font_b;
    }

    public static byte[] getFdFont() { /* 横向放大一倍 */
        byte[] fd_font = new byte[3];
        fd_font[0] = 0x1c;
        fd_font[1] = 0x21;
        fd_font[2] = 4;

        return fd_font;
    }

    public static byte[] getFd8Font() { /* 纵向放大一倍 */
        byte[] fd_font = new byte[3];
        fd_font[0] = 0x1c;
        fd_font[1] = 0x21;
        fd_font[2] = 8;

        return fd_font;
    }

    public static byte[] getFd12Font() { /* */
        byte[] fd_font = new byte[3];

        fd_font[0] = 0x1c;
        fd_font[1] = 0x21;
        fd_font[2] = 12;

//		   socketWriter.write(0x1c); 
//		   socketWriter.write(0x21); 
//		   socketWriter.write(12); 
//		   socketWriter.write(0x1b); 
//		   socketWriter.write(0x21); 
//		   socketWriter.write(12);
        return fd_font;
    }

    public static byte[] getFdDoubleFont() { /* 横向放大两倍，纵向放大两倍 */
        byte[] fd_font = new byte[3];
        fd_font[0] = 0x1d;
        fd_font[1] = 0x21;
        fd_font[2] = 17;


        return fd_font;
    }

    public static byte[] getLineFont() {
        byte[] fd_font = new byte[3];
        fd_font[0] = 0x1B;
        fd_font[1] = 0x33;
        fd_font[2] = 8;

        return fd_font;
    }

    public static byte[] getPrintCode() {
        byte[] print_code = new byte[9];
        print_code[0] = 0x1d;
        print_code[1] = 0x68;
        print_code[2] = 120;
        print_code[3] = 0x1d;
        print_code[4] = 0x48;
        print_code[5] = 0x10;
        print_code[6] = 0x1d;
        print_code[7] = 0x6B;
        print_code[8] = 0x02;

        return print_code;
    }

    /**
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String fix_set_length(int type, String strs, int len) {
        StringBuffer strtemp = new StringBuffer(strs);
        switch (type) {
            case 0:
                while (strtemp.length() < len) {
                    strtemp.append(" ");
                }
                while (strtemp.length() > len) {
                    strtemp.deleteCharAt(strtemp.length() - 1);
                }
                break;
            case 1:
                while (strtemp.length() < len) {
                    strtemp.append(" ");
                }
                while (strtemp.length() > len) {
                    strtemp.deleteCharAt(strtemp.length() - 1);
                }
                break;
            default:

                break;
        }

        return strtemp.toString();
    }


    public static String getTemplateName(String value) {

        String templateName = "";
        switch (value) {
            //
            case "0":
                templateName = PrinterConstant.CUST_TEMPLATE_NAME;
                break;

            //normaldish.vm
            case "1":
                templateName = PrinterConstant.COOKIE_TEMPLATE_NAME;
                break;

            //adddish.vm
            case "2":
                templateName = PrinterConstant.ADDDISH_TEMPLATE_NAME;
                break;
            default:
                break;
        }
        return templateName;
    }
    
    //added by caicai
    /**
     * 居中对齐
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static byte[] alignCenter()
	{
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 97;
		result[2] = 1;
		return result;
	}
    /**
     * 左对齐
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static byte[] alignLeft()
	{
		byte[] result = new byte[3];
		result[0] = 27;
		result[1] = 97;
		result[2] = 0;
		return result;
	}
    //纵向放大两倍（汉字，英文，数字都支持）
	public static String longitudinalDouble() {
		byte[] result = new byte[3];
		result[0] = GS;
		result[1] = 33;
		result[2] = 1;
		return new String(result);
	}
    
	//纵向，横向放大两倍（汉字，英文，数字都支持）
	public static String bothDouble() {
		byte[] result = new byte[3];
		result[0] = GS;
		result[1] = 33;
		result[2] = 17;
		return new String(result);
	}
	
	public static byte[] VerticalDoubleFont() {
		byte[] fd_font = new byte[3];
        fd_font[0] = 0x1d;
        fd_font[1] = 0x21;
        fd_font[2] = 0x02;

        return fd_font;
    }
	/**
	 * 纵向放大一倍
	 * @return
	 */
	public static byte[] VerticalFont() {
		byte[] fd_font = new byte[3];
        fd_font[0] = 0x1d;
        fd_font[1] = 0x21;
        fd_font[2] = 0x01;

        return fd_font;
    }
}
