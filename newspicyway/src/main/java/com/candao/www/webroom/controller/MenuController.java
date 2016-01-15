package com.candao.www.webroom.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.candao.common.utils.IdentifierUtils;
import com.candao.common.utils.JacksonJsonMapper;
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.ValidateUtils;
import com.candao.file.fastdfs.service.FileService;
import com.candao.www.constant.Constant;
import com.candao.www.data.model.Tdish;
import com.candao.www.data.model.Tmenu;
import com.candao.www.utils.ImageCompress;
import com.candao.www.utils.SessionUtils;
import com.candao.www.webroom.model.MenuGroup;
import com.candao.www.webroom.service.MenuService;
//*
@Controller
@RequestMapping("/menu")
public class MenuController {
	@Autowired
	private MenuService menuService;
	@Autowired
	FileService  fileService;
	/**  
	 * 创建菜谱
	 * @return
	 */
	@RequestMapping("/createmenu")
	public String createmenu(){
		return "/dish/menucreate";
	}
	/**
	 * 编辑菜谱
	 * @return
	 */
	@RequestMapping("/editmenu")
	public ModelAndView editmenu(@RequestParam String menuId){
		ModelAndView mav=new ModelAndView("/dish/menuedit");
		mav.addObject("menuId", menuId);
		mav.addObject("operateType", "edit_menu");
		return mav;
	}
	
	/**
	 * 总店查看菜谱
	 * @return
	 */
	@RequestMapping("/viewmenu")
	public ModelAndView viewmenu(@RequestParam String menuId){
		ModelAndView mav=new ModelAndView("/dish/menuview");
		mav.addObject("menuId", menuId);
		return mav;
	}
	/**
	 * 菜谱总店管理页面
	 * @return
	 */
	@RequestMapping("/menucontrol")
	public String menulist(){
		return "/dish/menucontrol";
	}
	/**
	 * 保存菜谱
	 * @return
	 */
	@RequestMapping("/savemenu")
	@ResponseBody
	public String savemenu(@RequestBody MenuGroup menuGroup,HttpServletRequest request){
		boolean b = false;
		String id = menuGroup.getMenu().getMenuid();
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				menuGroup.getMenu().setMenuid(IdentifierUtils.getId().generate().toString());
//				menuGroup.getMenu().setCreateuserid(((User)request.getSession().getAttribute(com.candao.www.constant.Constant.CURRENT_USER)).getId());
				menuGroup.getMenu().setCreateuserid(SessionUtils.getCurrentUser().getId());
				b = menuService.saveMenu(menuGroup);
			} else {// 修改
				b = menuService.updateMenu(menuGroup);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (b) {
			if (ValidateUtils.isEmpty(id)) {
				map.put("message", "添加成功");
				map.put("menuid", menuGroup.getMenu().getMenuid());
			} else {
				map.put("message", "修改成功");
				map.put("menuid", menuGroup.getMenu().getMenuid());
			}
		} else {
			if (ValidateUtils.isEmpty(id)) {
				map.put("message", "添加失败");
				map.put("menuid", "");
			} else {
				map.put("message", "修改失败");
				map.put("menuid", "");
			}
		}
		return JacksonJsonMapper.objectToJson(map);
	}
	/**
	 * 根据条件查询所有菜谱
	 * @param params
	 * sorttype 1表示按状态排序  2表示按时间排序
	 * @return
	 */
	@RequestMapping("/getMenuList")
	@ResponseBody
	public ModelAndView getMenuList(@RequestParam Map<String, Object> params){
		List<Tmenu> list=menuService.getMenuList(params);
		ModelAndView mav=new ModelAndView();
		mav.addObject("all", list);
		return mav;
	}
	/**
	 * 根据menuid获取菜谱
	 * @param menuid
	 * @return
	 */
	@RequestMapping("/getMenuById/{id}")
	@ResponseBody
	public ModelAndView getMenuById(@PathVariable(value = "id") String menuid){
		MenuGroup menuGroup=menuService.getMenuById(menuid);
		ModelAndView mav=new ModelAndView();
		mav.addObject("menuGroup", menuGroup);
		return mav;
	}
	/**
	 * 根据menuid删除菜谱
	 * @param menuid
	 * @return
	 */
	@RequestMapping("/deleteMenuById/{id}")
	@ResponseBody
	public ModelAndView deleteMenuById(@PathVariable(value = "id") String menuid){
		boolean flag=menuService.deleteMenuById(menuid);
		ModelAndView mav=new ModelAndView();
		if (flag) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}
	/**
	 * 根据menuid复制菜谱
	 * @param oldmenuid
	 * @param menuGroup
	 * @return
	 */
	@RequestMapping("/copyMenu/{id}")
	@ResponseBody
	public ModelAndView copyMenu(@PathVariable(value = "id") String oldmenuid,@RequestBody MenuGroup menuGroup){
		String id=IdentifierUtils.getId().generate().toString();
		menuGroup.getMenu().setMenuid(id);
		boolean flag=menuService.copyMenu(oldmenuid,menuGroup);
		ModelAndView mav=new ModelAndView();
		if (flag) {
			mav.addObject("message", "复制成功");
			mav.addObject("menuid", id);
		} else {
			mav.addObject("message", "复制失败");
			mav.addObject("menuid", "");
		}
		return mav;
	}
	/**
	 * 修改菜谱中菜品的估清
	 * {"dishid":"","menuid":"","status":""}
	 * @author shen
	 * @throws IOException 
	 * @date:2015年5月11日下午7:58:02
	 * @Description: TODO
	 * {"result":["{\"Data\":\"1\",\"workdate\":\"\",\"Info\":\"\"}"]}
	 */
	@RequestMapping("/updateDishStatus")
	@ResponseBody
	public ModelAndView updateDishStatus(@RequestBody Map<String, Object> params) throws IOException{
		ModelAndView mav=new ModelAndView();
		boolean flag=menuService.updateDishStatus(params);
		//推送接口先注释掉
		if (flag) {
			mav.addObject("message", "1");
			StringBuffer str=new StringBuffer(Constant.TS_URL);
			if("1".equals(String.valueOf(params.get("status")))){
				str.append(Constant.MessageType.msg_1003+"/"+params.get("dishid"));
				System.out.println("菜品估清推送："+str.toString());
			}else{
				str.append(Constant.MessageType.msg_1007+"/"+params.get("dishid"));
				System.out.println("菜品取消估清推送："+str.toString());
			}
			new Thread(new TsThread(str.toString())).run();
//			URL urlobj = new URL(str.toString());
//			URLConnection urlconn = urlobj.openConnection();
//			urlconn.connect();
//			InputStream myin = urlconn.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
//			String content = reader.readLine();
//			JSONObject object = JSONObject.fromObject(content.trim());
//			List<Map<String,Object>> resultList = (List<Map<String, Object>>) object.get("result");
//			//根据返回中的一个状态判断是否发送成功
//			if("1".equals(String.valueOf(resultList.get(0).get("Data")))){
//				mav.addObject("message", "1");
//			}else{
//				mav.addObject("message", "0");
//			}
		} else {
			mav.addObject("message", "0");
		}
		return mav;
	}
	public class TsThread extends Thread{
		   String  str ;
		   public TsThread(String  str){
			   this.str = str;
		   }
		   @Override
		   public void run(){
			   //根据动作打印不同的小票
				URL urlobj;
				try {
				urlobj = new URL(str);
				URLConnection	urlconn = urlobj.openConnection();
				urlconn.connect();
				InputStream myin = urlconn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(myin));
				String content = reader.readLine();
				JSONObject object = JSONObject.fromObject(content.trim());
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> resultList = (List<Map<String, Object>>) object.get("result");
				if("1".equals(String.valueOf(resultList.get(0).get("Data")))){
					System.out.println("推送成功");
				}else{
					System.out.println("推送失败");
				}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   }
	   }
	/**
	 * 调整图片，图片裁剪
	 * {"image":"","oldimage":"","flag":"1or2(1表示直接拖进去2图片裁剪)","type":"1,2,3,4","x":"","y":"","h":"","w":""}
	 * image01/M00/00/03/wKhmLlVzvVmABlZZAAI_VpvlSkg213.png
	 * @author shen
	 * @date:2015年5月18日上午10:13:57
	 * @Description: TODO
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping("/adjustpic")
	@ResponseBody
	public String adjustpic(HttpServletRequest request,@RequestBody Map<String, Object> params){
		System.out.println(JacksonJsonMapper.objectToJson(params));
		Date time = new Date();
		String image=Constant.INNERFILEURL_PREFIX+String.valueOf(params.get("image"));
//		String oldimage=String.valueOf(params.get("oldimage"));
		String flag=String.valueOf(params.get("flag"));
		String type=String.valueOf(params.get("type"));
		int imageX=Math.round(Float.valueOf(String.valueOf(params.get("x")==null||params.get("x")==""?"0":params.get("x"))));
		int imageY=Math.round(Float.valueOf(String.valueOf(params.get("y")==null||params.get("y")==""?"0":params.get("y"))));
		int imageH=Math.round(Float.valueOf(String.valueOf(params.get("h")==null||params.get("h")==""?"0":params.get("h"))));
		int imageW=Math.round(Float.valueOf(String.valueOf(params.get("w")==null||params.get("w")==""?"0":params.get("w"))));
//		int imageX=Integer.valueOf(String.valueOf(params.get("x")==null||params.get("x")==""?"0":params.get("x")));
//		int imageY=Integer.valueOf(String.valueOf(params.get("y")==null||params.get("y")==""?"0":params.get("y")));
//		int imageH=Integer.valueOf(String.valueOf(params.get("h")==null||params.get("h")==""?"0":params.get("h")));
//		int imageW=Integer.valueOf(String.valueOf(params.get("w")==null||params.get("w")==""?"0":params.get("w")));
		
		String fileupload=PropertiesUtils.getValue("image.path");
//		D:\workspace\baseline-parent\newspicyway\src\main\webapp
//		System.out.println("----------->>>>>>>>>>>>."+request.getRealPath("/")+File.separator+image);
//		String srcImagePath=request.getRealPath("/")+File.separator+image;
		String dirTime = String.valueOf(time.getTime());
		String extName = image.substring(image.lastIndexOf("."));
		String inputDir=request.getRealPath("/")+fileupload;
		String outputDir=request.getRealPath("/")+fileupload;
//		String inputFileName=image.substring(image.lastIndexOf("/"));
		String outputFileName=dirTime+extName;
//		File file=new File(request.getRealPath("/")+File.separator+image);
		String imageurl="";
		ImageCompress imageCompress=new ImageCompress();
		String newfilename= imageCompress.saveToFile(image, outputDir, outputFileName);
//		if(file.exists()){
			/**
			 * A:  770*1094       B:770*540   
			 * C:  378*540        D:378*263
			 */
			//直接拖入菜谱中，压缩图片
			if("1".equals(flag)){
				int width=0;
				int height=0;
				switch (type) {
				case "1":
					width=770;
					height=1094;
					break;
				case "2":
					width=770;
					height=540;
					break;
				case "3":
					width=378;
					height=540;
					break;
				case "4":
					width=378;
					height=263;
					break;
				default:
					break;
				}
				imageurl=imageCompress.compressPic(inputDir, outputDir, newfilename, outputFileName, width, height, false);
				//从文件服务器下载下来，编辑
				if(!"".equals(imageurl)){
					imageurl=uploadfileToFastDfs(outputDir+File.separator+imageurl,extName.substring(1));
					deleteFile(outputDir+File.separator+imageurl);
				}
			}
			//裁剪图片
			if("2".equals(flag)){
				imageurl=imageCompress.imgCut(inputDir,newfilename,imageX,imageY,imageW,imageH);
				if(!"".equals(imageurl)){
					imageurl=uploadfileToFastDfs(outputDir+File.separator+imageurl,extName.substring(1));
				deleteFile(outputDir+File.separator+imageurl);
				}
			}
//		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("image", imageurl);
		return JacksonJsonMapper.objectToJson(map);
	}
	public String uploadfileToFastDfs(String path,String extName){
		//
		File file=new File(path);
		String fileUrlpath="";
		if(file.exists()){
			try {
				fileUrlpath= fileService.uploadFile(file,extName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return fileUrlpath;
		}else{
			return "";
		}
		
	}
	public void deleteFile(String path){
	    File file = new File(path);  
	    // 路径为文件且不为空则进行删除  path
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	    }  
	}
	/**
	 * 跳转到分店菜品管理页面
	 * @return
	 */
	@RequestMapping("/branchDindex")
	public ModelAndView branchDindex(){
	    ModelAndView mv = new ModelAndView("/dish/branchDishindex");
	    return mv;
	}
	
	/**
	 * 分店菜品管理页面获取菜品数据
	 * @return
	 */
	@RequestMapping("/branchDishMenuData")
	public ModelAndView branchDishMenuData(){
//	    ModelAndView mv = new ModelAndView("/dish/branchDishindex");
		ModelAndView mv = new ModelAndView();
	    Map<String, Object> params=new HashMap<String, Object>();
	  //获取菜谱基本信息
	    List<Map<String,Object>> list=menuService.findMenuByBrachid();
	    if(list!=null&&list.size()>0){
	    	mv.addObject("menu", list.get(0));
	    	String menuid=String.valueOf(list.get(0).get("menuid"));
	    	params.put("menuid",menuid);
	    	List<Map<String,Object>> columList=menuService.getBranchMenuColumn(params);
	    	mv.addObject("columList", columList);
	    	if(columList!=null&&columList.size()>0){
	    		for(int i=0;i<columList.size();i++){
	    			params.put("columnid",columList.get(i).get("id"));
	    			List<Map<String,Object>> dishList=menuService.getBranchMenuDishByType(params);
	    			columList.get(i).put("countDish",dishList.size());
	    			if(i==0){
	    				mv.addObject("listdish", dishList);
	    			}
	    		}
	    	}
	    	
	    }
	    return mv;
	}
	@RequestMapping("/getMenuDishByType")
	@ResponseBody
	public ModelAndView getMenuDishByType(@RequestBody Map<String,Object> params ){
		ModelAndView mv = new ModelAndView();
		List<Map<String,Object>> dishList=menuService.getBranchMenuDishByType(params);
		mv.addObject(dishList);
		return mv;
	}
	@RequestMapping("/getMenuDishDetailById")
	@ResponseBody
	public ModelAndView getMenuDishDetailById(@RequestBody Map<String,Object> params ){
		ModelAndView mv = new ModelAndView();
		Map<String,Object> map=menuService.getMenuDishDetailById(params);
		mv.addObject(map);
		return mv;
	}
	/**
	 * 分店查看菜谱
	 * @return
	 */
	@RequestMapping("/branchViewmenu")
	public ModelAndView branchViewmenu(@RequestParam String menuId){
		ModelAndView mav=new ModelAndView("/dish/branchMenuview");
		mav.addObject("menuId", menuId);
		return mav;
	}
	
	/* * 菜谱分店管理页面
	 * @return
	 */
	@RequestMapping("/branchMenucontrol")
	public String branchMenucontrol(){
		return "/dish/branchMenucontrol";
	}
	/**
	 * 所有菜谱名不能重复验证
	 * @param 
	 * @return
	 */
	@RequestMapping("/validateMenu")
	@ResponseBody
	public ModelAndView validateArticle(Tmenu tmenu){
		String itemDesc="";
		ModelAndView mav = new ModelAndView();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("menuname", tmenu.getMenuname());
//		params.put("status", 0);
		
		List<Tmenu> list=menuService.getMenuList(params);
		Tmenu a=menuService.getMenuNameById(tmenu.getMenuid());
		//新增
		if(ValidateUtils.isEmpty(tmenu.getMenuid())){
			if(list!=null&&list.size()>0){
				mav.addObject("message", "菜谱名称不能重复");
			}
		}else{
			//修改
			if(!a.getMenuname().equals(tmenu.getMenuname())){
				
				if(list!=null&&list.size()>0){
					mav.addObject("message", "菜谱名称不能重复");
				}
			}else{
				Tmenu tmenu1=new Tmenu();
				
				for(Tmenu param : list){
					if(!param.getMenuid().equals(a.getMenuid())){
						tmenu1=param;
					}
				
				}
				if(list!=null&&tmenu1.getMenuid()!=null){
				
					mav.addObject("message", "菜谱名称不能重复");
					
					
				}
			}
		}
		
		return mav;
	}
}
