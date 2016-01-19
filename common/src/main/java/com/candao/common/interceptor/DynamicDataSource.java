package com.candao.common.interceptor;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

public class DynamicDataSource extends AbstractRoutingDataSource implements InitializingBean{

    @Override
    protected String  determineCurrentLookupKey() {
        return DataSourceSwitcher.getDataSource();
    }
    
    @Override  
    public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {  
        super.setDataSourceLookup(dataSourceLookup);  
    }  
  
    @Override  
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {  
        super.setDefaultTargetDataSource(defaultTargetDataSource);  
    }  
  
    @Override  
    public void setTargetDataSources(Map targetDataSources) {  
        super.setTargetDataSources(targetDataSources);  
        //重点  
        super.afterPropertiesSet();  
    } 

}
