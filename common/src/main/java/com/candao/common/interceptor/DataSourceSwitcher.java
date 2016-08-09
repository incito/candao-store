package com.candao.common.interceptor;

import org.springframework.util.Assert;
 
public class DataSourceSwitcher {
    @SuppressWarnings("rawtypes")
    private static final ThreadLocal contextHolder = new ThreadLocal();
	private static final ThreadLocal <String> userHolder = new ThreadLocal<String>();
    
    private static final String DBSLAVE = "slave";
    private static final String DBMASTER = "master";
    
    
    public static void setCurrentUser(String accountId){
    	userHolder.set(accountId);
    }
 
     public static String getCurrentUser(){
    	 return userHolder.get();
     }
    
    @SuppressWarnings("unchecked")
    public static void setDataSource(String dataSource) {
        Assert.notNull(dataSource, "dataSource cannot be null");
        contextHolder.set(dataSource);
    }

    public static void setMaster(){
//        clearDataSource();
    	clearDataSource();
        setDataSource(DBMASTER);
    }
    
    public static void setSlave() {
    	clearDataSource();
        setDataSource(DBSLAVE);
    }
    
    public static String getDataSource() {
        return (String) contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
}
 
