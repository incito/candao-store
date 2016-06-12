package com.candao.www.printer;

/**
 * 打印机命令
 * Created by liaoy on 2016/6/12.
 */
public class PrinterCommand {
    /**
     *开启自动状态返回
     */
    public static final byte[] AUTO_STATUS=new byte[]{0x1D,0x61,(byte)0xFF};
}
