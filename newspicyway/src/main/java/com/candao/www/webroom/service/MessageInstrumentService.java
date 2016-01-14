package com.candao.www.webroom.service;
import java.util.List;
import java.util.Map;
import com.candao.www.data.model.TbMessageInstrument;
public interface MessageInstrumentService {
/**
  * 保存数据
  * @param tbMessageInstrument
  * @return
  */
 public boolean save(TbMessageInstrument tbMessageInstrument);
 /**
  * 更改数据
  * @param tbMessageInstrument
  * @return
  */
 public boolean update(TbMessageInstrument tbMessageInstrument);

 /**
  * 查询单个数据
  * @param id
  * @return
  */
 public TbMessageInstrument findByParams(Map<String, Object> params);
 public TbMessageInstrument get(String id);
 
 /**
  * 删除单个数据
  * @param id
  * @return
  */
 public boolean deleteByParams(Map<String, Object> params);
 
 /**
  * 获取所有桌号
  * @author zhao
  * @param params
  * @return
  */
public List<Map<String, Object>>  find(Map<String, Object> params);
public int updateStatus(TbMessageInstrument tbMessageInstrument);

}
