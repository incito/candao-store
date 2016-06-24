package com.candao.print.listener;

import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintObj;
import com.candao.print.listener.template.ListenerTemplate;

/**
 * Created by Administrator on 2016-6-13.
 */
public interface QueueListener {

    public PrintData receiveMessage(PrintObj object,ListenerTemplate template) throws Exception;

}
