package com.candao.www.webroom.service;

import java.util.List;

import com.candao.www.data.model.TbVersion;

public interface VersionService {
 /**
  * 查找所有终端版本信息
  * author lzl
  * 2014/8/22 14:15
  * @return
  */
	public List<TbVersion> findAll();
 /**
  * 按终端类型查找版本信息
  * author lzl
  * 2014/8/22 14:16
  * @param type 终端类型  1： android平板  2：android手机   3:ios平板  4：ios手机
  * @return
  */
	public TbVersion findOne(int type);
	/**
	 * 更新版本信息
	 * author lzl
     * 2014/8/22 14:18
	 * @param tbVersion  
	 * @return
	 */
	public boolean update(TbVersion tbVersion);
}
