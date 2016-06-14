package com.candao.print.listener;

import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintObj;

/**
 * Created by Administrator on 2016-6-13.
 */
public abstract class AbstractQueueListener implements QueueListener{


    protected PrintData prepareData(PrintObj obj,PrintData data) throws Exception {
        data.write(new byte[27]);
        data.write(new byte[27]);
        data.flush();//

        printBusinessData(obj,data,data);

        // 下面指令为打印完成后自动走纸
        data.write(new byte[27]);
        data.write(new byte[100]);
        data.write(new byte[4]);
        data.write(new byte[10]);
        data.flush();//
        data.write(new byte[] { 0x1B, 0x69 });// 切纸

        return data;
    }

    protected abstract void printBusinessData(PrintObj object, PrintData socketOut, PrintData writer) throws Exception;
}
