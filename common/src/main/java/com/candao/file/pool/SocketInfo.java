package com.candao.file.pool;

import java.net.Socket;

public class SocketInfo {
	/** 
     * socket 
     */  
    private Socket socket;  
    /** 
     * 是否空闲 （是：true  否：false） 
     */  
    private boolean isFree;  
    /** 
     * socket id 
     */  
    private Integer socketId;  
      
    /** 
     * 是否为可关闭链接 （是：true  否：false） 
     */  
    private boolean isClosed;  
  
    public Socket getSocket() {  
        return socket;  
    }  
  
    public void setSocket(Socket socket) {  
        this.socket = socket;  
    }  
  
    public boolean isFree() {  
        return isFree;  
    }  
  
    public void setFree(boolean isFree) {  
        this.isFree = isFree;  
    }  
  
    public Integer getSocketId() {  
        return socketId;  
    }  
  
    public void setSocketId(Integer socketId) {  
        this.socketId = socketId;  
    }  
  
    public boolean isClosed() {  
        return isClosed;  
    }  
  
    public void setClosed(boolean isClosed) {  
        this.isClosed = isClosed;  
    }  
}
