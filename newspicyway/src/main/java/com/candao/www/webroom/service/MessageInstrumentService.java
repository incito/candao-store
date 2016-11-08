package com.candao.www.webroom.service;
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

 public TbMessageInstrument get(String id);

}
