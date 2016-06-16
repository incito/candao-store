package com.candao.print.entity;

import java.io.UnsupportedEncodingException;

public class PrinterConstant {

    public static final String ENCODESTR = "UTF-8";

    public static final String LISTLABEL = "listdata";

    //���õ�
    public static final String CUST_TEMPLATE_NAME = "customerbill.vm";
    //�� ����
    public static final String COOKIE_TEMPLATE_NAME = "normal.vm";
    //Ԥ�ᵥ
    public static final String SETTLE_TEMPLATE_NAME = "settlement.vm";
    //��Ӳ�
    public static final String ADDDISH_TEMPLATE_NAME = "adddish.vm";
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

    public static byte[] getClear_font() {

        // ��������Ŵ�һ��
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

    // �����ӡָ��
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
     * �ַ��Ȳ���,�����ӡʱ����,������ӡҳ��,�������ļ�������е�����
     *
     * @param strs Դ�ַ�
     * @param len  ָ���ַ��
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


    public static String byteSubstring(String s, int length) {

        byte[] bytes = null;
        byte[] rebytes = new byte[length];
        try {
            bytes = s.getBytes("Unicode");
            int n = 0; // ��ʾ��ǰ���ֽ���
            int i = 2; // Ҫ��ȡ���ֽ���ӵ�3���ֽڿ�ʼ
            for (; i < bytes.length && n < length; i++) {
                // ����λ�ã���3��5��7�ȣ�ΪUCS2�����������ֽڵĵڶ����ֽ�
                if (i % 2 == 1) {
                    n++; // ��UCS2�ڶ����ֽ�ʱn��1
                } else {
                    // ��UCS2����ĵ�һ���ֽڲ�����0ʱ����UCS2�ַ�Ϊ���֣�һ�������������ֽ�
                    if (bytes[i] != 0) {
                        n++;
                    }
                }
            }
            // ���iΪ����ʱ�������ż��
            if (i % 2 == 1)

            {
                // ��UCS2�ַ��Ǻ���ʱ��ȥ�������һ��ĺ���
                if (bytes[i - 1] != 0)
                    i = i - 1;
                    // ��UCS2�ַ�����ĸ�����֣��������ַ�
                else
                    i = i + 1;
            }


            if (bytes.length < length) {
                System.arraycopy(bytes, 0, rebytes, 0, bytes.length);
//			        	int maxsize = rebytes.length;
                while (i < length) {
                    byte b = 32;
                    rebytes[i++] = b;
                }

            }

            return new String(rebytes, 0, length, "Unicode");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String(rebytes);
    }
}
