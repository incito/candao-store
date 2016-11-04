package com.candao.www.bossstore.dao;

import java.util.List;
import java.util.Map;

public interface TTableDao {
	public static final String PREFIX = TTableDao.class.getName();
	/**
     * 按桌子人数 状态 统计桌子数量
     * @return
     */
    public List<Object[]> queryTablesStatus();
    
    /**
     * 统计当月的服务员信息
     * @return
     */
    public List<Object[]> queryService(Map<String,String> params);

    /**
     * 根据tableId 查询是桌子信息 几人桌、桌号
     * @param tableId 桌子id
     * @return 返回桌子信息
     */
    public Object[] queryTableInfoByTableId(Map<String,Object> params);
}
