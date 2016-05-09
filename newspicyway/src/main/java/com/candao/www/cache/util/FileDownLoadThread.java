package com.candao.www.cache.util;

import java.io.IOException;

/**
 * @title :为文件下载提供线程，异步处理，提高效率
 * @author: zhaigt
 * @date: 2012-12-12
 */
public class FileDownLoadThread extends Thread{
    private String url;   // 要下载的资源路径
    private String path;  // 要保存的路径地址
   
    public FileDownLoadThread(String url, String path){
        this.url = url;
        this.path = path;
    }

    @Override
    public void run() {
        try {
          HttpUtil.downLoad(url, path);
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
}