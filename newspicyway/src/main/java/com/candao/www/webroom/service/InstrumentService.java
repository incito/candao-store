package com.candao.www.webroom.service;
import java.util.List;
import java.util.Map;
import com.candao.www.data.model.TbInstrument;
public interface InstrumentService {
/**
  * 保存数据
  * @param tbInstrument
  * @return
  */
 public boolean save(TbInstrument tbInstrument);
 /**
  * 更改数据
  * @param tbInstrument
  * @return
  */
 public boolean update(TbInstrument tbInstrument);

 /**
  * 查询单个数据
  * @param id
  * @return
  */
 public TbInstrument findByParams(Map<String, Object> params);
 
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
public int updateStatus(TbInstrument tbInstrument);

}
