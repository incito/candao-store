package com.candao.www.dataserver.mapper;

/**
 * Created by ytq on 2016/3/16.
 */
public interface CaleTableAmountMapper {
    void pCaleTableAmount(String orderId);
    
    int updateOrderDetailPayAmount(String orderId); 
    
    int updateOrderDueAmount(String orderId); 
}
