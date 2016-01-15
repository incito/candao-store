package com.candao.www.webroom.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.page.Page;
import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.ValidateUtils;
import com.candao.file.fastdfs.service.FileService;
import com.candao.print.service.PrinterService;
import com.candao.www.data.model.TbasicData;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.TdishUnit;
import com.candao.www.security.controller.BaseController;
import com.candao.www.utils.SessionUtils;
import com.candao.www.webroom.model.TdishGroup;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.DishService;
import com.candao.www.webroom.service.DishTypeService;
import com.candao.www.webroom.service.DishUnitService;
import com.candao.www.webroom.service.MenuService;
import com.candao.www.webroom.service.TemplateService;
//*
@Controller
@RequestMapping("/dish")
public class DishController extends BaseController{
	@Autowired
	private DishService dishService;
	@Autowired
	private DishTypeService dishTypeService;
	@Autowired
	private PrinterService printerService;
	@Autowired
    private TemplateService templateService;
	@Autowired 
	private DishUnitService dishUnitService;
	@Autowired
	private DataDictionaryService datadictionaryService;
	@Autowired
	private MenuService menuService;
	
	@Autowired
	FileService  fileService;
//	@Autowired
//	FileService  fileService;
	@RequestMapping("/index")
	public String index(){
		return "/dish/show";
	}
	
	/**
	 * 分页查询
	 * @param params
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(@RequestParam Map<String, Object> params, int page, int rows) {
		Page<Map<String, Object>> pageMap = dishService.grid(params, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		return mav;
	}
	@RequestMapping("/pageSearch")
	@ResponseBody
	public ModelAndView pageSearch(@RequestParam Map<String, Object> params, int page, int rows) {
		Page<Map<String, Object>> pageMap = dishService.pageSearchService(params, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		return mav;
	}
	@RequestMapping("/getDishList")
	@ResponseBody
	public ModelAndView getDishList(){
		List<Tdish> dishlist= dishService.getDishList();
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", dishlist);
		return mav;
	}
	@RequestMapping(value = "/save")
	@ResponseBody
	public String save(HttpServletRequest request,Tdish tdish, @RequestParam("main_img") MultipartFile file1) {
		Tdish a = null;
		String id = tdish.getDishid();
		Map<String, Object> map = new HashMap<String, Object>();
//		tdish.setLabel(tdish.getLabel().replaceAll("，", ","));// 防止页面填中文,号
//		File file = null;
//		Map<String,Object> params =new HashMap<String,Object>();
		
		if (!file1.isEmpty() && ValidateUtils.isEmpty(tdish.getImage())) {
//			Date time = new Date();
			String fileName = file1.getOriginalFilename();
//			String dirTime = String.valueOf(time.getTime());
			String extName = fileName.substring(fileName.lastIndexOf(".")+1);
//			String fileupload=PropertiesUtils.getValue("image.path");
//			file = new File(request.getRealPath(fileupload), dirTime + extName);
			try {
//				file1.transferTo(file);
				//使用 fast dfs 上传文件
				 String fileUrlpath = fileService.uploadFile(file1.getInputStream(),extName);
//				tdish.setImage(fileupload.substring(1)+"/"+ dirTime + extName);
				 tdish.setImage(fileUrlpath);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//tdish.setUserid(SessionUtils.getCurrentUser().getUserid());
//		tdish.setUserid(((User)request.getSession().getAttribute(com.candao.www.constant.Constant.CURRENT_USER)).getId());
		tdish.setUserid(SessionUtils.getCurrentUser().getId() );
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				tdish.setDishid(IdentifierUtils.getId().generate().toString());
				tdish.setSource(getFirstRoot(tdish.getColumnid()));
				tdish.setIsdisplay(1);
				tdish.setIsselect(1);
				tdish.setStatus(1);
				tdish.setCreatetime(new Date());
				tdish.setDishno("dishNo");
				a = dishService.save(tdish);
				map.put("dishid", tdish.getDishid());
			} else {// 修改articleService
				Tdish oldtdish= dishService.findById(id);
				tdish.setCreatetime(oldtdish.getCreatetime());
				tdish.setSource(getFirstRoot(tdish.getColumnid()));
				tdish.setIsdisplay(oldtdish.getIsdisplay());
				tdish.setIsselect(oldtdish.getIsselect());
				tdish.setStatus(oldtdish.getStatus());
				a = dishService.update(tdish);
				//菜品修改了，如果该菜品已在模板中选择了，就相应修改了模板中的数据
//				updateTemplate(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!ValidateUtils.isEmpty(a)) {
			if (ValidateUtils.isEmpty(id)) {
				map.put("message", "添加成功");
				map.put("tdish", tdish);
			} else {
				map.put("message", "修改成功");
				map.put("tdish", tdish);
			}
		} else {
			if (ValidateUtils.isEmpty(id)) {
				map.put("message", "添加失败");
			} else {
				map.put("message", "修改失败");
			}
		}
		return JacksonJsonMapper.objectToJson(map);
	}
	/**
	 * 获取菜品信息（不包括估清状态）
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		Tdish tdish = dishService.findById(id);
		List<TdishUnit> tlist=dishUnitService.getUnitsBydishId(id);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dishid", id);
		List<Map<String,Object>> listdishCol=dishService.getdishCol(params);
		ModelAndView mav = new ModelAndView();
		TdishGroup tdishGroup=new TdishGroup();
		tdishGroup.setTdish(tdish);
		tdishGroup.setList(tlist);
		mav.addObject("tdishGroup", tdishGroup);
	    List<Map<String,Object>> listType=dishTypeService.findAll("0");
	    mav.addObject("listType", listType);
	    List<String> UnitHistorylist=dishUnitService.getUnitHistorylist();
	    mav.addObject("UnitHistorylist", UnitHistorylist);
	    mav.addObject("listdishCol", listdishCol);
	    
		return mav;
	}
	/**
	 * 获取菜品信息（包括估清状态）
	 * @param id
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/findDishAndMenuidById/{id}")
	@ResponseBody
	public ModelAndView findDishAndMenuidById(@PathVariable(value = "id") String id, Model model) {
		
		
		Map<String, Object> columnMap = menuService.getMenuColumn();
		
		String  menuid =(String) ((List<Map<String,Object>>)columnMap.get("rows")).get(0).get("menuid");
		Map<String,Object> paramsTtd = new HashMap<String,Object>();
//		paramsTtd.put("menuid","0928b3e2-f6ba-4456-bf46-0f8a8056ef97");
		paramsTtd.put("menuid",menuid);
		paramsTtd.put("dishid", id);
		/**
		 * 根据menuid,dishid获取t_template_dishunit表中的数据
		 */
		List<Map<String,Object>> findOneTtdList = menuService.findOneTtd(paramsTtd);
		//只能有一条数据
		int statusTtd =  (int) ((Map<String,Object>)findOneTtdList.get(0)).get("status");
		Tdish tdish = dishService.findById(id);
		List<TdishUnit> tlist=dishUnitService.getUnitsBydishId(id);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dishid", id);
		List<Map<String,Object>> listdishCol=dishService.getdishCol(params);
		ModelAndView mav = new ModelAndView();
		TdishGroup tdishGroup=new TdishGroup();
		tdishGroup.setTdish(tdish);
		tdishGroup.setList(tlist);
		mav.addObject("tdishGroup", tdishGroup);
	    List<Map<String,Object>> listType=dishTypeService.findAll("0");
	    mav.addObject("listType", listType);
	    List<String> UnitHistorylist=dishUnitService.getUnitHistorylist();
	    mav.addObject("UnitHistorylist", UnitHistorylist);
	    mav.addObject("listdishCol", listdishCol);
	    mav.addObject("menuid",menuid);
//	    mav.addObject("menuid","0928b3e2-f6ba-4456-bf46-0f8a8056ef97");
	    mav.addObject("statusTtd",statusTtd);
		return mav;
	}
	/**
	 * 获取初始化的数据，菜品分类和计量单位的历史记录
	 * @return
	 */
	@RequestMapping("/getInitData")
	@ResponseBody
	public ModelAndView getInitData() {
		ModelAndView mav = new ModelAndView();
		List<Map<String,Object>> listType=dishTypeService.findAll("0");
		mav.addObject("listType", listType);
		List<String> UnitHistorylist=dishUnitService.getUnitHistorylist();
		mav.addObject("UnitHistorylist", UnitHistorylist);
		return mav;
	}
	@RequestMapping("/delete")
	@ResponseBody
	public ModelAndView deleteById(@RequestParam Map<String, Object> params) {
		boolean b = dishService.deleteById(params);
		ModelAndView mav = new ModelAndView();
		if (b) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}

	//返回根节点
	public String getFirstRoot(String id){
		if(!"0".equals(id)){
		TbasicData tbasicData=dishTypeService.findById(id);
		if(tbasicData!=null){
			if(tbasicData.getDepthnum()==1){
				id=tbasicData.getId();
			}else{
				id=getFirstRoot(tbasicData.getFid());
			}
		}else{
			id="0";
		}
		}
		return id;
	}
	/**
	 * 获取所有打印机
	 */
	@RequestMapping("/getPrintersList")
	@ResponseBody
	public  ModelAndView getPrintersList(@RequestParam Map<String, Object> params){
		List<Map<String,Object>> list = printerService.find(params);
		ModelAndView mov=new ModelAndView();
		mov.addObject(list);
		return mov;
	}
	//菜品修改了，如果该菜品已在模板中选择了，就相应修改了模板中的数据
	@SuppressWarnings({ "unchecked" })
	public void updateTemplate(Tdish dish){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("data", dish.getDishid());
		List<Map<String,Object>> list=templateService.getTemplates(map);
		if(list!=null&&list.size()>0){
			for(Map<String,Object> tempMap:list){
				System.out.println("---------->>>>>>>该菜品属于模板："+tempMap.get("name")); 
				String datajsonstr=tempMap.get("data").toString();
				System.out.println("----->>>>>>修改前模板数据"+datajsonstr);
				 Map<String, Object> dataMap = JacksonJsonMapper.jsonToObject(datajsonstr, Map.class);
				 for (Map.Entry<String, Object> entry : dataMap.entrySet()) {    
					LinkedHashMap<String,Object> arra2= (LinkedHashMap<String,Object>) dataMap.get(entry.getKey());
					System.out.println(arra2.get("dishid"));
					if(dish.getDishid().equals(arra2.get("dishid").toString())){
					 Map<Object, Object> dataMap2=new HashMap<Object, Object>();
					 dataMap2.put("dishid", dish.getDishid());
					 dataMap2.put("dishname", dish.getTitle());
					 dataMap2.put("image", dish.getImage());
					 dataMap2.put("normalprice", dish.getPrice());
					 dataMap2.put("discountprice", dish.getVipprice());
					 dataMap2.put("couponsprice", dish.getVipprice());
					 dataMap2.put("cookietype", dish.getUnit());
					 dataMap2.put("cusumers", dish.getOrderNum());
					 dataMap2.put("unit", dish.getUnit());
					 dataMap2.put("introduction", dish.getIntroduction());
					 dataMap2.put("dishtype", dish.getDishtype());
					 dataMap2.put("itemtype", dish.getSource());
					 dataMap2.put("specialflag", dish.getImagetitle());
					 dataMap2.put("unitflag", dish.getHeadsort());
					 dataMap2.put("speciality", dish.getAbbrdesc());
					 dataMap.put(entry.getKey(), dataMap2);
				  }else{
					 dataMap.put(entry.getKey(), arra2);
				  }
				 }
				 tempMap.put("data", JacksonJsonMapper.objectToJson(dataMap));
				 System.out.println("----->>>>>>修改前模板数据"+JacksonJsonMapper.objectToJson(dataMap));
			}
		}
		templateService.updateTemplates(list);
	}

	@RequestMapping("/getDishLists")
	@ResponseBody
	public ModelAndView getDishLists(){
		List<Tdish> dishlist= dishService.getDishLists();
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", dishlist);
		return mav;
	}
//-----------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------
	/**
	 * 跳转到总店菜品管理页面
	 * @return
	 */
	@RequestMapping("/zdindex")
	public ModelAndView zdindex(){
	    ModelAndView mv = new ModelAndView("/dish/dishindex");
	    List<Map<String,Object>> listType=dishTypeService.findAll("0");
	    mv.addObject("listType", listType);
	    String title ="";
	    if(listType!=null&&listType.size()>0){
	    	List<Tdish> listdish=dishService.getDishesByDishType(listType.get(0).get("id").toString());
	    	for(Tdish tdishForEarch:listdish){
	    		title = subTextString(tdishForEarch.getTitle(), 13);
	    		tdishForEarch.setTitle(title);
		    }
	    	
	    	mv.addObject("listdish", listdish);
	    }
	    
	    int navDishesLength = listType.size();
	    mv.addObject("navDishesLength",navDishesLength);
	    
//	    List<Map<String, Object>> listUnit = datadictionaryService.getDatasByType("UNIT");
//	    mv.addObject("listUnit", listUnit);
	    return mv;
	}
	
	/**
	 * 跳转到分店菜品管理页面
	 * @return
	 */
	@RequestMapping("/branchDindex")
	public ModelAndView branchDindex(){
	    ModelAndView mv = new ModelAndView("/dish/branchDishindex");
	    List<Map<String,Object>> listType=dishTypeService.findAll("0");
	    mv.addObject("listType", listType);
	    String title ="";
	    if(listType!=null&&listType.size()>0){
	    	List<Tdish> listdish=dishService.getDishesByDishType(listType.get(0).get("id").toString());
	    	for(Tdish tdishForEarch:listdish){
	    		title = subTextString(tdishForEarch.getTitle(), 13);
	    		tdishForEarch.setTitle(title);
		    }
	    	
	    	mv.addObject("listdish", listdish);
	    }
	    
	    int navDishesLength = listType.size();
	    mv.addObject("navDishesLength",navDishesLength);
	    
//	    List<Map<String, Object>> listUnit = datadictionaryService.getDatasByType("UNIT");
//	    mv.addObject("listUnit", listUnit);
	    return mv;
	}
	/**
	 * 获取分类和分类下面的菜品，所有的，组成json数据，多计量单位分为多个菜品返回
	 * @return
	 * flag=0 没限制 查询所有
	 * flag=1 不包含火锅
	 * flag=2 不包含套餐
	 * flag=3 不包含火锅和套餐
	 */
	@RequestMapping("/getTypeAndDishList/{flag}")
	@ResponseBody
	public ModelAndView getTypeAndDishList(@PathVariable(value = "flag") String flag){
		List<Map<String,List<Map<String,Object>>>> list=new ArrayList<Map<String,List<Map<String,Object>>>>();
		List<Map<String,Object>> listDishType=dishTypeService.findAll("0");
		for(Map<String,Object> map:listDishType){
			Map<String,List<Map<String,Object>>> maptotal=new  HashMap<String,List<Map<String,Object>>>();
			List<Map<String,Object>> dishlist= dishService.getDishListByType(map.get("id").toString(),flag);
			maptotal.put(JacksonJsonMapper.objectToJson(map), dishlist);
		    list.add(maptotal);
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", list);
		return mav;
	}
	/**
	 * 获取分类和分类下面的菜品，所有的，组成json数据,不包括多计量单位，和上面一个接口区分开来
	 * 菜品管理中的功能  将已有菜品添加到该分类中
	 * @return
	 */
	@RequestMapping("/getTypeAndDishMap")
	@ResponseBody
	public ModelAndView getTypeAndDishMap(){
		ModelAndView mav = new ModelAndView();
		List<Map<String,List<Map<String,Object>>>> list=new ArrayList<Map<String,List<Map<String,Object>>>>();
	    List<Map<String,Object>> listDishType=dishTypeService.findAll("0");
	    for(Map<String,Object> map:listDishType){
	      Map<String,List<Map<String,Object>>> maptotal=new  HashMap<String,List<Map<String,Object>>>();
	      List<Map<String,Object>> dishlist= dishService.getDishMapByType(map.get("id").toString());
	      maptotal.put(JacksonJsonMapper.objectToJson(map), dishlist);
	      list.add(maptotal);
	    }
//		List<Map<Map<String,Object>,List<Map<String,Object>>>> listtotal=new ArrayList<Map<Map<String,Object>,List<Map<String,Object>>>>();
////		Map<Map<String,Object>,List<Map<String,Object>>> map=new  HashMap<Map<String,Object>,List<Map<String,Object>>>();
//		List<Map<String,Object>> listDishType=dishTypeService.findAll("0");
//		for(Map<String,Object> map:listDishType){
//			List<Map<String,Object>> dishlist= dishService.getDishListByType(map.get("id").toString());
//			Map<Map<String,Object>,List<Map<String,Object>>> map1=new  HashMap<Map<String,Object>,List<Map<String,Object>>>();
//			map1.put(map, dishlist);
//			listtotal.add(map1);
//		}
//		ModelAndView mav = new ModelAndView();
		mav.addObject("page", list);
		return mav;
	}
	/**
	 * 通过菜品分类的id，获取分类下的所有菜品
	 * @return
	 */
	@RequestMapping("/getDishesByDishType/{Typeid}")
	@ResponseBody
	public ModelAndView getDishesByDishType(@PathVariable(value = "Typeid") String Typeid){
		ModelAndView mav = new ModelAndView();
		List<Tdish> list=dishService.getDishesByDishType(Typeid);
		mav.addObject("list", list);
		return mav;
	}
	/**
	 * 通过菜品分类的id，获取分类下的所有菜品
	 * @return
	 */
	@RequestMapping("/getdishCol/{dishid}")
	@ResponseBody
	public ModelAndView getdishCol(@PathVariable(value = "dishid") String dishid){
		ModelAndView mav = new ModelAndView();
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("dishid", dishid);
		List<Map<String,Object>> list=dishService.getdishCol(map);
		mav.addObject("list", list);
		return mav;
	}
/**
 * 加载添加已有菜品到该分类中的页面
 * @return
 */
  @RequestMapping("/loadDishSelect")
  public ModelAndView loadDishSelect(){
    ModelAndView mav = new ModelAndView("dish/dishSelectPage");
    return mav;
  }
  /**
   * 加载套餐选择的页面到该该页面div
   * @return
   */
  @RequestMapping("/loadcomboDishSelect")
  public ModelAndView loadcomboDishSelect(){
	  ModelAndView mav = new ModelAndView("dish/combodishSelect");
	  return mav;
  }
  /**
   * 加载套餐选择的页面到该该页面div
   * @return
   */
  @RequestMapping("/loadfishpotDishSelect")
  public ModelAndView loadfishpotDishSelect(){
	  ModelAndView mav = new ModelAndView("dish/fishpotdishselect");
	  return mav;
  }
  /**
   * 添加已有菜品至该分类中被选中的菜
   * @param list
   * @return
   */
	@RequestMapping("/addTdishDishType")
	public ModelAndView  addTdishDishType(@RequestBody List<Map<String,Object>> list){
		int b=dishService.addTdishDishType(list);
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView mov=new ModelAndView();
		if(b>=0){
			map.put("message", "success");
		}else{
			map.put("message", "fail");
		}
		mov.addObject(map);
		return mov; 
	}
	/**
	 * 所有分类中菜品名不能重复验证（添加已有菜品至该分类的菜品除外）
	 * @param tdish
	 * @return
	 */
	@RequestMapping("/validateDish")
	@ResponseBody
	public ModelAndView validateArticle(Tdish tdish){
		String itemDesc="";
		ModelAndView mav = new ModelAndView();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("title", tdish.getTitle());
//		params.put("status", 0);
		
		List<Map<String, Object>> list=dishService.findDishes(params);
		Tdish a=dishService.findAllById(tdish.getDishid());
		//新增
		if(ValidateUtils.isEmpty(tdish.getDishid())){
			if(list!=null&&list.size()>0){
				itemDesc=(String) list.get(0).get("itemDesc");
				mav.addObject("message", "菜品名称不能重复");
				mav.addObject("messageDetail","名称在"+itemDesc+"已有");
			}
		}else{
			//修改
			if(!a.getTitle().equals(tdish.getTitle())){
				
				if(list!=null&&list.size()>0){
					itemDesc=(String) list.get(0).get("itemDesc");
					mav.addObject("message", "菜品名称不能重复");
					mav.addObject("messageDetail","名称在"+itemDesc+"已有");
				
					
				}
			}else{
				Map<String,Object> map1=new HashMap<String,Object>();
				
				for(Map<String,Object> param : list){
					if(!param.get("dishid").equals(a.getDishid())){
						map1=param;
					}
				
				}
				if(list!=null&&map1.get("dishid")!=null){
					itemDesc=(String) list.get(1).get("itemDesc");
					mav.addObject("message", "菜品名称不能重复");
					mav.addObject("messageDetail","名称在"+itemDesc+"已有");
					
					
				}
			}
		}
		
		return mav;
	}
	/**
	 * 通过菜品的id，判断是否被菜谱使用
	 * @return
	 */
	@RequestMapping("/comfirmDelDish/{dishid}")
	@ResponseBody
	public ModelAndView comfirmDelDish(@PathVariable(value = "dishid") String dishid){
		ModelAndView mav = new ModelAndView();
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("redishid", dishid);
		List<Map<String,Object>> list=dishService.comfirmDelDish(map);
		mav.addObject("list", list);
		return mav;
	}
	/**
	 * 通过菜品分类的id，判断是否被菜谱使用
	 * @return
	 */
	@RequestMapping("/comfirmDelDishType")
	@ResponseBody
	public ModelAndView comfirmDelDishType(@RequestParam Map<String, Object> params){
		ModelAndView mav = new ModelAndView();
		List<Tdish> listDishes=dishService.getDishesByDishType((String) params.get("dishType"));
		String message = "0";
		for(Tdish tdish:listDishes){
			Map<String,Object> mapDish =new HashMap<String,Object>();
			mapDish.put("redishid", tdish.getDishid());
			List<Map<String,Object>> list=dishService.comfirmDelDish(mapDish);
			if(list.size()!=0){
				message = "1";
				break;
			}
		}

		mav.addObject("message", message);
		
		return mav;
	}
	
	//截取字符串长度(中文2个字节，半个中文显示一个)  
	public static String subTextString(String str,int len){  
	    if(str.length()<len/2)return str;  
	    int count = 0;  
	    StringBuffer sb = new StringBuffer();  
	    String[] ss = str.split("");  
	    for(int i=1;i<ss.length;i++){  
	        count+=ss[i].getBytes().length>1?2:1;  
	        sb.append(ss[i]);  
	        if(count>=len)break;  
	    }  
	    //不需要显示...的可以直接return sb.toString();  
	    return (sb.toString().length()<str.length())?sb.append("...").toString():str;  
	}
	/**
	 * 菜品标签位置固定

	 * @param tbasicDatas
	 * @return
	 */
	@RequestMapping("/updateDishTagListOrder")
	public ModelAndView  updateListOrder(@RequestBody List<Map<String,Object>> dishTag){
		int b=dishTypeService.updateDishTagListOrder(dishTag);
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView mov=new ModelAndView();
		if(b>=0){
			map.put("message", "success");
		}else{
			map.put("message", "fail");
		}
		mov.addObject(map);
		return mov; 
	}
	
	
}
