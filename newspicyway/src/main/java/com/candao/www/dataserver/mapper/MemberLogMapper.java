package com.candao.www.dataserver.mapper;

import com.candao.www.webroom.model.MemberTransModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lenovo
 */
public interface MemberLogMapper {
    int insert(MemberTransModel model);

    List<Map<String,Object>> getStaticByUser(@Param("userId") String userId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}
