package com.candao.inorder.lzrabbit;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.candao.inorder.utils.CustomerContextHolder;

/**
 * 
 * @author liangdong
 * 多数据源融合
 *
 */
public class MultipleDataSource extends  AbstractRoutingDataSource  {


	    @Override
	    protected Object determineCurrentLookupKey() {
	        return CustomerContextHolder.getCustomerType();
	    }

}
