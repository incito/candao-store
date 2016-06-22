package com.candao.www.webroom.service.impl;

import com.candao.www.data.dao.TReturnDishDetailDao;
import com.candao.www.utils.OrderDetailParse;
import com.candao.www.webroom.service.PresentDishDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 退菜明细表
 *
 * @author Administrator
 */
@Service
public class PresentDishDetailServiceImpl implements PresentDishDetailService {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private TReturnDishDetailDao treturnDishDetailDao;

    @Override
    public List<Map<String, Object>> presentDishList(Map<String, Object> params) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Map<String, Object> map : getPresentDishList(params)) {
            String obTime = map.get("beginTime") + "";
            if (!obTime.contains(".0")) {
                String beginTime = sdf.format(new Date(Long.parseLong(obTime)));
                map.put("beginTime", beginTime);
            } else {
                map.put("beginTime", obTime.replace(".0", ""));
            }
            String sperequire = map.get("sperequire") + "";
            map.put("waiter", OrderDetailParse.getFreeUser(sperequire));
            map.put("presentReason", OrderDetailParse.getFreeReason(sperequire));
            mapList.add(map);
        }
        return mapList;
    }

    private List<Map<String, Object>> getPresentDishList(Map<String, Object> params) {
        return treturnDishDetailDao.getPresentDishList(params);
    }
}
