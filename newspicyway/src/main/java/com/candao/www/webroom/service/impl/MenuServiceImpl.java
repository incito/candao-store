package com.candao.www.webroom.service.impl;

import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.file.common.Constant;
import com.candao.www.data.dao.*;
import com.candao.www.data.model.*;
import com.candao.www.dataserver.service.msghandler.MsgForwardService;
import com.candao.www.utils.SessionUtils;
import com.candao.www.utils.TsThread;
import com.candao.www.webroom.model.MenuGroup;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private TmenuBranchDao tmenuBranchDao;
    @Autowired
    private TmenuDao tmenuDao;
    @Autowired
    private TbTemplateDao tbTemplateDao;
    @Autowired
    private TtemplateDetailDao ttemplateDetailDao;
    @Autowired
    private TtemplateDishUnitlDao ttemplateDishUnitlDao;
    @Autowired
    private TbBranchDao tbBranchDao;
    @Autowired
    private TbasicDataDao tbasicDataDao;
    @Autowired
    private ComboDishDao comboDishDao;
    @Autowired
    private DataDictionaryService datadictionaryService;
    @Autowired
    private MsgForwardService msgForwardService;
    private static final String SEPERATOR = "^thumbnail^";
    private Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Override
    public boolean saveMenu(MenuGroup menuGroup) {
        boolean flag = true;
//		String userid = SessionUtils.getCurrentUser().getId();
        // TODO Auto-generated method stub
        // 添加菜谱基本信息
        // String menuid=IdentifierUtils.getId().generate().toString();
        String menuid = menuGroup.getMenu().getMenuid();
        Tmenu tmenu = menuGroup.getMenu();
        tmenu.setMenuid(menuid);
//		tmenu.setCreateuserid(userid);
        flag = flag && tmenuDao.insert(tmenu) > 0;
        // 添加菜谱和门店的关系表
        List<TmenuBranch> menuBranchlist = menuGroup.getMenuBranchlist();
        if (menuBranchlist != null && menuBranchlist.size() > 0) {
            for (TmenuBranch menuBranch : menuBranchlist) {
                menuBranch.setId(IdentifierUtils.getId().generate().toString());
                menuBranch.setMenuid(menuid);
            }
            flag = flag && tmenuBranchDao.addTmenuBranch(menuBranchlist) > 0;
        }
        // 添加菜谱模板信息
        List<Ttemplate> templatelist = menuGroup.getTemplatelist();
        Map<String, TtemplateDishUnit> DishunitlistMap = new HashMap<String, TtemplateDishUnit>();
        if (templatelist != null && templatelist.size() > 0) {
            for (Ttemplate template : templatelist) {
                String templateid = IdentifierUtils.getId().generate().toString();
                template.setId(IdentifierUtils.getId().generate().toString());
                template.setTemplateid(templateid);
                template.setMenuid(menuid);
                if (template.getDetaillist() != null
                        && template.getDetaillist().size() > 0) {
                    for (TtemplateDetail templateDetail : template.getDetaillist()) {
                        templateDetail.setId(IdentifierUtils.getId().generate().toString());
                        templateDetail.setMenuid(menuid);
                        templateDetail.setTemplateid(templateid);
                        if (templateDetail.getDishunitlist() != null && templateDetail.getDishunitlist().size() > 0) {
                            for (TtemplateDishUnit templateDishUnit : templateDetail.getDishunitlist()) {
                                templateDishUnit.setId(IdentifierUtils.getId().generate().toString());
                                templateDishUnit.setMenuid(menuid);
                                templateDishUnit.setRecommend(templateDetail.getRecommend());
                                //这边放到map中是为了去重，map的key是菜品id+单位，不然多计量单位就无法保存多个计量单位了
                                DishunitlistMap.put(templateDishUnit.getDishid() + templateDishUnit.getUnit(), templateDishUnit);
                            }
//							flag = flag&& ttemplateDishUnitlDao.addTtemplateDishUnit(templateDetail.getDishunitlist()) > 0;
                        }
                    }
                    flag = flag && ttemplateDetailDao.addTtemplateDetail(template.getDetaillist()) > 0;
                }
            }
            flag = flag && tbTemplateDao.addTtemplate(templatelist) > 0;
        }
        List<TtemplateDishUnit> list = getList(DishunitlistMap);
        if (list != null && list.size() > 0) {
            flag = flag && ttemplateDishUnitlDao.addTtemplateDishUnit(getList(DishunitlistMap)) > 0;
        }
        return flag;
    }

    public List<TtemplateDishUnit> getList(Map<String, TtemplateDishUnit> map) {
        List<TtemplateDishUnit> list = new ArrayList<TtemplateDishUnit>();
        Set<Entry<String, TtemplateDishUnit>> set = map.entrySet();
        Iterator<Entry<String, TtemplateDishUnit>> i = set.iterator();
        while (i.hasNext()) {
            list.add(i.next().getValue());
        }
        return list;
    }

    @Override
    public boolean updateMenu(MenuGroup menuGroup) {
        boolean flag = true;
        // TODO Auto-generated method stub
        // 添加菜谱基本信息
        String menuid = menuGroup.getMenu().getMenuid();
        flag = flag && tmenuDao.update(menuGroup.getMenu()) > 0;
        // 添加菜谱和门店的关系表
        flag = flag && tmenuBranchDao.delTmenuBranch(menuid) > 0;
        List<TmenuBranch> menuBranchlist = menuGroup.getMenuBranchlist();
        if (menuBranchlist != null && menuBranchlist.size() > 0) {
            for (TmenuBranch menuBranch : menuBranchlist) {
                menuBranch.setId(IdentifierUtils.getId().generate().toString());
                menuBranch.setMenuid(menuid);
            }
            flag = flag && tmenuBranchDao.addTmenuBranch(menuBranchlist) > 0;
        }
        // 添加菜谱模板信息
        flag = flag && tbTemplateDao.delTtemplate(menuid) > 0;
        flag = flag && ttemplateDetailDao.delTtemplateDetail(menuid) > 0;
        flag = flag && ttemplateDishUnitlDao.delTtemplateDishUnit(menuid) > 0;
        Map<String, TtemplateDishUnit> DishunitlistMap = new HashMap<String, TtemplateDishUnit>();
        List<Ttemplate> templatelist = menuGroup.getTemplatelist();
        if (templatelist != null && templatelist.size() > 0) {
            for (Ttemplate template : templatelist) {
                String templateid = IdentifierUtils.getId().generate().toString();
                template.setId(IdentifierUtils.getId().generate().toString());
                template.setTemplateid(templateid);
                template.setMenuid(menuid);
                if (template.getDetaillist() != null && template.getDetaillist().size() > 0) {
                    for (TtemplateDetail templateDetail : template.getDetaillist()) {
                        templateDetail.setId(IdentifierUtils.getId().generate().toString());
                        templateDetail.setMenuid(menuid);
                        templateDetail.setTemplateid(templateid);
                        if (templateDetail.getDishunitlist() != null && templateDetail.getDishunitlist().size() > 0) {
                            for (TtemplateDishUnit templateDishUnit : templateDetail.getDishunitlist()) {
                                templateDishUnit.setId(IdentifierUtils.getId().generate().toString());
                                templateDishUnit.setMenuid(menuid);
                                templateDishUnit.setRecommend(templateDetail.getRecommend());
                                DishunitlistMap.put(templateDishUnit.getDishid() + templateDishUnit.getUnit(), templateDishUnit);
                            }
//							flag = flag&& ttemplateDishUnitlDao.addTtemplateDishUnit(templateDetail.getDishunitlist()) > 0;
                        }
                    }
                    flag = flag && ttemplateDetailDao.addTtemplateDetail(template.getDetaillist()) > 0;
                }
            }
            flag = flag && tbTemplateDao.addTtemplate(templatelist) > 0;
        }
        List<TtemplateDishUnit> list = getList(DishunitlistMap);
        if (list != null && list.size() > 0) {
            flag = flag && ttemplateDishUnitlDao.addTtemplateDishUnit(getList(DishunitlistMap)) > 0;
        }
        return flag;
    }

    @Override
    public List<Tmenu> getMenuList(Map<String, Object> params) {
        // TODO Auto-generated method stub
        List<Tmenu> list = null;
        if ("Y".equals(PropertiesUtils.getValue("isbranch"))) {
            Map<String, Object> branchInfoMap = tbBranchDao.getBranchInfo();
            String branchid = null;
            branchid = (String) branchInfoMap.get("branchid");
            params.put("branchid", branchid);
            list = tmenuDao.findByBranchid(params);
        } else {
            list = tmenuDao.find(params);
        }
        List<Integer> beanchlist = tbBranchDao.getBranchidList();
        if (list != null && list.size() > 0) {
            for (Tmenu menu : list) {
                menu.setBranchMap(tmenuBranchDao.getBranchDetailBymenuId(menu.getMenuid()));
                List<Integer> menuBranchIdList = tmenuBranchDao.getBranchIdBymenuId(menu.getMenuid());
                if (menuBranchIdList != null) {
                    if (beanchlist != null) {
                        if (menuBranchIdList.containsAll(beanchlist)) {
                            menu.setFlag(1);
                        } else {
                            menu.setFlag(0);
                        }
                    } else {
                        menu.setFlag(0);
                    }
                } else {
                    menu.setFlag(0);
                }
            }
        }
        return list;
    }

    @Override
    public MenuGroup getMenuById(String menuid) {
        // TODO Auto-generated method stub
        // return tmenuDao.get(menuid);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("menuid", menuid);
        MenuGroup menuGroup = new MenuGroup();
        Tmenu menu = tmenuDao.get(menuid);
        menu.setBranchMap(tmenuBranchDao.getBranchDetailBymenuId(menuid));
        List<Integer> beanchlist = tbBranchDao.getBranchidList();
        List<Integer> menuBranchIdList = tmenuBranchDao.getBranchIdBymenuId(menu.getMenuid());
        if (menuBranchIdList != null && beanchlist != null) {
            if (menuBranchIdList.containsAll(beanchlist)) {
                menu.setFlag(1);
            } else {
                menu.setFlag(0);
            }
        } else {
            menu.setFlag(0);
        }
        menuGroup.setMenu(menu);
        menuGroup.setMenuBranchlist(tmenuBranchDao.getTmenuBranchBymenuId(menuid));
        List<Ttemplate> templatelist = tbTemplateDao.getTtemplateBymenuId(params);
        if (templatelist != null && templatelist.size() > 0) {
            for (Ttemplate template : templatelist) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("menuid", menuid);
                map.put("templateid", template.getTemplateid());
                List<TtemplateDetail> detaillist = ttemplateDetailDao.getTtemplateDetailByparams(map);
                if (detaillist != null && detaillist.size() > 0) {
                    for (TtemplateDetail templateDetail : detaillist) {
                        Map<String, Object> mapunit = new HashMap<String, Object>();
                        mapunit.put("menuid", menuid);
                        mapunit.put("dishid", templateDetail.getRedishid());
                        List<TtemplateDishUnit> dishunitlist = ttemplateDishUnitlDao.getTtemplateDishUnitByparams(mapunit);
                        templateDetail.setRecommend(dishunitlist.get(0).getRecommend());
                        //这个菜是鱼锅
                        if ("1".equals(templateDetail.getDishtype()) && !"1".equals(templateDetail.getLevel())) {
                            List<TtemplateDishUnit> fishpotlist = ttemplateDishUnitlDao.getTtemplatefishpotUnitByparams(mapunit);
                            dishunitlist.addAll(fishpotlist);
                            templateDetail.setFishpotlist(fishpotlist);
                        }
                        templateDetail.setDishunitlist(dishunitlist);
                    }
                }

//				2016年1月13号之后，新增的4种新版式的处理
                map.put("redishid", "TEMPLATE-IMAGE");
                List<TtemplateDetail> imgDetaillist = ttemplateDetailDao.getTtemplateDetailByParamsHasRedishid(map);
//				updated by caicai split field img 
                if (imgDetaillist != null && !imgDetaillist.isEmpty()) {
                    for (TtemplateDetail it : imgDetaillist) {
                        if (it.getImage() != null && !"".equals(it.getImage())) {
                            String[] path = it.getImage().split(SEPERATOR.replaceAll("\\^", "\\\\^"));
                            if (path != null && path.length == 2) {
                                it.setOriginalImage(path[0]);
                                it.setImage(path[1]);
                            }
                        }
                    }
                    detaillist.addAll(imgDetaillist);
                }
                template.setDetaillist(detaillist);
            }
        }
        menuGroup.setTemplatelist(templatelist);
        return menuGroup;
    }

    @Override
    public boolean deleteMenuById(String menuid) {
        // TODO Auto-generated method stub
        return tmenuDao.delete(menuid) > 0;
    }

    @Override
    public boolean copyMenu(String oldmenuid, MenuGroup menuGroup) {
        // TODO Auto-generated method stub
        boolean flag = true;
        // -------------------------
        // 复制菜谱基本信息
        String newmenuid = menuGroup.getMenu().getMenuid();
//		Tmenu menu = tmenuDao.get(oldmenuid);
        Tmenu menu = menuGroup.getMenu();
//		menu.setMenuid(newmenuid);
        menu.setCreateuserid(SessionUtils.getCurrentUser().getId());
        flag = flag && tmenuDao.insert(menu) > 0;
        // -------------------------------
        // 添加菜谱和门店的关系表
        List<TmenuBranch> menuBranchlist = menuGroup.getMenuBranchlist();
        if (menuBranchlist != null && menuBranchlist.size() > 0) {
            for (TmenuBranch menuBranch : menuBranchlist) {
                menuBranch.setId(IdentifierUtils.getId().generate().toString());
                menuBranch.setMenuid(newmenuid);
            }
            flag = flag && tmenuBranchDao.addTmenuBranch(menuBranchlist) > 0;
        }
        // -------------------------------
        // 复制模板
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("menuid", oldmenuid);
        List<Ttemplate> templatelist = tbTemplateDao.getTtemplateBymenuId(params);
        if (templatelist != null && templatelist.size() > 0) {
            for (Ttemplate template : templatelist) {
                template.setId(IdentifierUtils.getId().generate().toString());
                template.setMenuid(newmenuid);
            }
            flag = flag && tbTemplateDao.addTtemplate(templatelist) > 0;
        }
        // ------------------------------------
        // 复制模板详细
        List<TtemplateDetail> detaillist = ttemplateDetailDao.getTtemplateDetailByparams(params);
        if (detaillist != null && detaillist.size() > 0) {
            for (TtemplateDetail templateDetail : detaillist) {
                templateDetail.setId(IdentifierUtils.getId().generate().toString());
                templateDetail.setMenuid(newmenuid);
            }
            flag = flag && ttemplateDetailDao.addTtemplateDetail(detaillist) > 0;
        }
        // ----------------------------------------
        // 复制菜谱中菜品的详细信息
        List<TtemplateDishUnit> dishunitlist = ttemplateDishUnitlDao.getTtemplateDishUnitByparams(params);
        if (dishunitlist != null && dishunitlist.size() > 0) {
            for (TtemplateDishUnit templateDishUnit : dishunitlist) {
                templateDishUnit.setId(IdentifierUtils.getId().generate().toString());
                templateDishUnit.setMenuid(newmenuid);

            }
            flag = flag && ttemplateDishUnitlDao.addTtemplateDishUnit(dishunitlist) > 0;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getMenuData() {
        Map<String, Object> branchInfoMap = tbBranchDao.getBranchInfo();
        String branchid = null;
        String tenantid = null;
        if (branchInfoMap != null) {
            branchid = (String) branchInfoMap.get("branchid");
            tenantid = (String) branchInfoMap.get("tenantid");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("branchid", branchid);
        List<Map<String, Object>> menuList = tmenuDao.findMenuByBrachid(params);
        Map<String, Object> menu = new HashMap<String, Object>();
        if (menuList != null && menuList.size() > 0) {
            menu = menuList.get(0);
            String menuid = String.valueOf(menu.get("menuid"));
            params.put("menuid", menuid);
            List<Map<String, Object>> templatelist = tbTemplateDao.getTtemplateBymenuIdPad(params);
            if (templatelist != null && templatelist.size() > 0) {
                for (Map<String, Object> templateMap : templatelist) {
                    Map<String, Object> templateDataMap = new HashMap<String, Object>();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("menuid", menuid);
                    map.put("templateid", String.valueOf(templateMap.get("templateid")));
                    List<Map<String, Object>> detaillist = ttemplateDetailDao.getTtemplateDetailByparamsPad(map);
                    if (detaillist != null && detaillist.size() > 0) {
                        for (Map<String, Object> templateDetail : detaillist) {
                            Map<String, Object> mapunit = new HashMap<String, Object>();
                            mapunit.put("menuid", menuid);
                            mapunit.put("dishid", String.valueOf(templateDetail.get("redishid")));
                            List<Map<String, Object>> dishunitlist = ttemplateDishUnitlDao.getTtemplateDishUnitByparamsPad(mapunit);
                            templateDetail.put("dishunitlist", dishunitlist);
                            templateDataMap.put(String.valueOf(templateDetail.get("location")), templateDetail);
                        }
//						2016年1月13号之后，新增的4种新版式的处理
                        map.put("redishid", "TEMPLATE-IMAGE");
                        List<TtemplateDetail> imgDetaillist = ttemplateDetailDao.getTtemplateDetailByParamsHasRedishid(map);
                        if (imgDetaillist != null && !imgDetaillist.isEmpty()) {
                            TtemplateDetail tempdetail = imgDetaillist.get(0);
                            //added by caicai
                            tempdetail.setDishtype("0");
                            if (tempdetail.getImage() != null && !"".equals(tempdetail.getImage())) {
                                String[] path = tempdetail.getImage().split(SEPERATOR.replaceAll("\\^", "\\\\^"));
                                if (path != null && path.length == 2) {
                                    tempdetail.setOriginalImage(path[0]);
                                    tempdetail.setImage(path[1]);
                                }
                            }
                            templateDataMap.put(tempdetail.getLocation(), tempdetail);
                        }
                    }

                    templateMap.put("datas", templateDataMap);
                }
            }
            menu.put("rows", templatelist);
        }
        //添加餐具的数据到菜谱数据中，一起传给前台
        Map<String, Object> dinnerware = new HashMap<String, Object>();
        List<Map<String, Object>> listdinnerware = datadictionaryService.getDatasByType("DISHES");
        if (listdinnerware != null && listdinnerware.size() > 0) {
//			"dinnerware":{"id":"XXXXXX","price":"2","vipprice":"1","status":"0代表不收费，1代表收费"}
            dinnerware.put("id", listdinnerware.get(0).get("id"));
            dinnerware.put("price", listdinnerware.get(0).get("price"));
            dinnerware.put("vipprice", listdinnerware.get(0).get("memberprice"));
            dinnerware.put("status", listdinnerware.get(0).get("chargesstatus"));
        }
        menu.put("dinnerware", dinnerware);
        menu.put("imgserver", Constant.FILEURL_PREFIX);
        menu.put("tenantid", tenantid);
        return menu;
    }

    @Override
    public Map<String, Object> getMenuColumn() {
        Map<String, Object> branchInfoMap = tbBranchDao.getBranchInfo();
        String branchid = null;
        if (branchInfoMap != null) {
            branchid = (String) branchInfoMap.get("branchid");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("branchid", branchid);
        List<Map<String, Object>> menuList = tmenuDao.findMenuByBrachid(params);
        Map<String, Object> menu = new HashMap<String, Object>();
        if (menuList != null && menuList.size() > 0) {
            Map<String, Object> columnMap = new HashMap<String, Object>();
            menu = menuList.get(0);
            String menuid = String.valueOf(menu.get("menuid"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("menuid", menuid);
            map.put("id", 0);
            List<Map<String, Object>> columnList = tbasicDataDao.getMenuColumn(map);
            columnMap.put("rows", columnList);
            return columnMap;
        } else {
            logger.info("menuList为空");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getMenuFishPot(String jsonString) {
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
//		String menuid=String.valueOf(params.get("menuid"));
        String dishides = String.valueOf(params.get("dishides"));
        String[] dishidList = dishides.split(",");
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (String dishid : dishidList) {
            Map<String, Object> fishpotparams = new HashMap<String, Object>();
            Map<String, Object> totalMap = new HashMap<String, Object>();
            totalMap.put("dishid", dishid);
            fishpotparams.put("dishid", dishid);
            fishpotparams.put("menuid", String.valueOf(params.get("menuid")));
            List<Map<String, Object>> fishpotListMap = comboDishDao.getFishPotDetailPad(fishpotparams);
            totalMap.put("rows", fishpotListMap);
            mapList.add(totalMap);
        }
        return mapList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getMenuCombodish(String jsonString) {
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
//		String menuid=String.valueOf(params.get("menuid"));
        List<Map<String, Object>> CombodishtList = new ArrayList<Map<String, Object>>();
        Map<String, Object> CombodishMap = new HashMap<String, Object>();
        String dishides = String.valueOf(params.get("dishides"));
        String[] dishidList = dishides.split(",");
        for (String dishid : dishidList) {
            List<Map<String, Object>> tdishGroupList = comboDishDao.getTdishGroupList(dishid);
            List<Map<String, Object>> listall = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> suretgroupDetailList = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> GroupMap : tdishGroupList) {//分组的list
                String groupid = GroupMap.get("id").toString();
                List<Map<String, Object>> tgroupDetailList = comboDishDao.getTgroupDetailList(groupid);
                List<Map<String, Object>> seltgroupDetailList = new ArrayList<Map<String, Object>>();
                for (Map<String, Object> DetailMap : tgroupDetailList) {//各组下面的数据
                    if ("1".equals(DetailMap.get("dishtype").toString())) {//鱼锅查询所属的鱼和锅
                        List<Map<String, Object>> tgroupDetailList2 = comboDishDao.getTgroupDetailList(DetailMap.get("id").toString());
                        DetailMap.put("dishes", tgroupDetailList2);
                    } else {
                        DetailMap.put("dishes", "");
                    }
                    if ("1".equals(DetailMap.get("status").toString())) {//可选项
                        seltgroupDetailList.add(DetailMap);
                        logger.info("可选项");
                    } else {//必选项
                        logger.info("必选项");
                        suretgroupDetailList.add(DetailMap);
                    }
                }
                GroupMap.put("alldishes", seltgroupDetailList);
                if (seltgroupDetailList != null && seltgroupDetailList.size() > 0) {
                    listall.add(GroupMap);
                }
            }
            Map<String, Object> totalMap = new HashMap<String, Object>();
            totalMap.put("combo", listall);
            totalMap.put("only", suretgroupDetailList);
            totalMap.put("dishid", dishid);
            CombodishtList.add(totalMap);
        }
        CombodishMap.put("rows", CombodishtList);
        return CombodishMap;
    }

    @Override
    public boolean updateDishStatus(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return ttemplateDishUnitlDao.updateDishStatus(params);
    }

    /**
     * 根据menuid,dishid获取t_template_dishunit表中的数据
     */
    @Override
    public List<Map<String, Object>> findOneTtd(Map<String, Object> paramsTtd) {
        // TODO Auto-generated method stub
        return ttemplateDishUnitlDao.findOneTtd(paramsTtd);
    }

    @Override
    public List<Tmenu> findEffectMenu(Map<String, Object> params) {
        // TODO Auto-generated method stub
        if ("Y".equals(PropertiesUtils.getValue("isbranch"))) {
            Map<String, Object> branchInfoMap = tbBranchDao.getBranchInfo();
            String branchid = "";
            branchid = (String) branchInfoMap.get("branchid");
            params.put("branchid", branchid);
            return tmenuDao.findEffectMenu(params);
        } else {
            System.out.println("总店---------------");
            List<Tmenu> list = tmenuDao.findEffectMenu(params);
            if (list != null && list.size() > 0) {
                for (Tmenu menu : list) {
                    menu.setBranchidlist(tmenuBranchDao.getBranchIdBymenuId(menu.getMenuid()));
                }
            }
            return list;
        }
    }

    @Override
    public List<Map<String, Object>> findMenuByBrachid() {
        if ("Y".equals(PropertiesUtils.getValue("isbranch"))) {
            String branchid = "";
            Map<String, Object> branchInfoMap = tbBranchDao.getBranchInfo();
            branchid = (String) branchInfoMap.get("branchid");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("branchid", branchid);
            return tmenuDao.findMenuByBrachid(params);
        } else {
            return null;
        }

    }

    @Override
    public Map<String, Object> getHeatDishList(Map<String, Object> params) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> list = tmenuDao.getHeatDishList(params);
        map.put("data", list);
        if (list != null && list.size() > 0) {
            logger.info("获取数据成功");
            map.put("flag", "1");
            map.put("code", "获取数据成功");
        } else {
            logger.info("没有数据");
            map.put("flag", "0");
            map.put("code", "没有数据");
        }

        return map;
    }

    @Override
    public List<Map<String, Object>> getBranchMenuColumn(Map<String, Object> params) {
        return tmenuDao.getBranchMenuColumn(params);
    }

    @Override
    public List<Map<String, Object>> getBranchMenuDishByType(
            Map<String, Object> params) {
        // TODO Auto-generated method stub
        return tmenuDao.getBranchMenuDishByType(params);
    }

    @Override
    public Map<String, Object> getMenuDishDetailById(Map<String, Object> params) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        params.put("flag", "1");
        map.put("dish", tmenuDao.getMenuDishDetailById(params));
        params.put("flag", "2");
        map.put("columnList", tmenuDao.getMenuDishDetailById(params));
        params.put("flag", "3");
        map.put("unitList", tmenuDao.getMenuDishDetailById(params));
        return map;
    }

    @Override
    public Tmenu getMenuNameById(String menuid) {
        Tmenu menu = tmenuDao.get(menuid);
        return menu;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getMenuSpfishpot(String jsonString) {
        Map<String, Object> params = JacksonJsonMapper.jsonToObject(jsonString, Map.class);
        String dishides = String.valueOf(params.get("dishides"));
        String[] dishidList = dishides.split(",");
        List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
        for (String dishid : dishidList) {
            Map<String, Object> fishpotparams = new HashMap<String, Object>();
            fishpotparams.put("dishid", dishid);
            fishpotparams.put("menuid", String.valueOf(params.get("menuid")));
            List<Map<String, Object>> fishpotListMap = comboDishDao.getFishPotDetailPad(fishpotparams);
            if (fishpotListMap != null && fishpotListMap.size() > 0) {
                Map<String, Object> totalMap = new HashMap<String, Object>();
                totalMap.put("dishid", dishid);
                for (Map<String, Object> map : fishpotListMap) {
                    Map<String, Object> deteilparams = new HashMap<String, Object>();
                    deteilparams.put("dishid", String.valueOf(map.get("dishid")));
                    deteilparams.put("menuid", String.valueOf(params.get("menuid")));
                    List<Map<String, Object>> fishpotDetailMap = comboDishDao.getFishPotDetailPad(deteilparams);
                    map.put("dishes", fishpotDetailMap);
                }
                totalMap.put("rows", fishpotListMap);
                resultMap.add(totalMap);
            }
        }
        return resultMap;
    }

    @Override
    public void notifyDishStatus(String dishId, short code) {
        //兼容咖啡模式 推送给dataserver广播接口
        StringBuffer str = new StringBuffer(com.candao.www.constant.Constant.TS_URL);
        String msgId;
        switch (code) {
            case 1:
                msgId = com.candao.www.constant.Constant.MSG_ID.GUQING;
                str.append(com.candao.www.constant.Constant.MessageType.msg_1003);
                break;
            case 2:
                msgId = com.candao.www.constant.Constant.MSG_ID.QXGUQING;
                str.append(com.candao.www.constant.Constant.MessageType.msg_1007);
                break;
            default:
                return;
        }
        Map<String, Object> msgData = new HashMap<>();
        msgData.put("dishId", dishId);
        msgData.put("oper", msgId);
        //消息有效期 秒
        int expireSeconds = 4 * 60 * 60;
        msgForwardService.broadCastMsg4Netty(msgId, msgData, expireSeconds, false);

        str.append("/").append(dishId);
        new Thread(new TsThread(str.toString())).run();
    }
}
