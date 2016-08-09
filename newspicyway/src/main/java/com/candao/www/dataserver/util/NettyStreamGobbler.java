package com.candao.www.dataserver.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NettyStreamGobbler extends Thread {

    InputStream is;
    String type;

    public NettyStreamGobbler(InputStream is, String type) {
        this.is = is;  
        this.type = type;  
    }  
  
    public void run() {  
        try {  
            InputStreamReader isr = new InputStreamReader(is);  
            BufferedReader br = new BufferedReader(isr);  
            String line = null;  
            while ((line = br.readLine()) != null) {  

            }  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        }  
    }  
}  