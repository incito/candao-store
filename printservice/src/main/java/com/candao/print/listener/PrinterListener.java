package com.candao.print.listener;

import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintObj;

/**
 * Created by Administrator on 2016-6-13.
 */
public interface PrinterListener {

    public PrintData receiveMessage(PrintObj object) throws Exception;

}
