package com.candao.print.listener;

import com.candao.print.entity.PrintData;
import com.candao.print.entity.PrintObj;
import com.candao.print.listener.template.ListenerTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Administrator on 2016-6-13.
 */
public abstract class AbstractQueueListener implements QueueListener{

	protected Log log = LogFactory.getLog(getClass());

    protected PrintData prepareData(PrintObj obj,PrintData data,ListenerTemplate template) throws Exception {
    	if (data == null) {
			data = new PrintData();
		}
//        data.write(new byte[27]);
//        data.write(new byte[27]);
        data.flush();//
        
        printBusinessData(obj,data,data,template);

        // 下面指令为打印完成后自动走纸
//        data.write(new byte[27]);
//        data.write(new byte[100]);
//        data.write(new byte[4]);
//        data.write(new byte[10]);
//        data.flush();//
//        data.write(new byte[] { 0x1B, 0x69 });// 切纸

        return data;
    }

    protected abstract void printBusinessData(PrintObj object, PrintData socketOut, PrintData writer,ListenerTemplate template) throws Exception;
    
    public void write(PrintData writer,Object[] msg){
		write(writer,msg,false);
	}
	public void write(PrintData writer,Object[] msg,boolean istrim){
		if (msg != null && msg.length != 0) {
			for (int i = 0; i < msg.length; i++) {
				if (istrim)
					msg[i] = msg[i].toString().trim();
				writer.write(msg[i].toString());
				writer.write("\r\n");
			}
		}
	}
}
