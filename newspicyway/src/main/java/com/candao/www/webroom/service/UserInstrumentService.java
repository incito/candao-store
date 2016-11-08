package com.candao.www.webroom.service;
import java.util.List;
import java.util.Map;

import com.candao.www.data.model.TbUserInstrument;
public interface UserInstrumentService {
/**
  * 保存数据
  * @param tbUserInstrument
  * @return
  */
 public boolean save(TbUserInstrument tbUserInstrument);
 /**
  * 更改数据
  * @param tbUserInstrument
  * @return
  */
 public boolean update(TbUserInstrument tbUserInstrument);
 
 /**
  * 更改数据
  * @param tbUserInstrument
  * @return
  */
 public boolean updateByid(TbUserInstrument tbUserInstrument);
public int insertByParams(Map<String, Object> map);
public List<TbUserInstrument> findByParams(Map<String, Object> params);
public String findrelateUserid(List<Map<String, Object>> retableList);
}
