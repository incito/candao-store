package com.candao.www.webroom.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.candao.common.utils.PropertiesUtils;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.model.TbPictures;
import com.candao.www.utils.SessionUtils;
import com.candao.www.webroom.service.PicturesService;

@Controller
@RequestMapping("/pictures")
public class PicturesController {
	@Autowired
	private PicturesService picturesService;

	@RequestMapping("/page")
	@ResponseBody
	public ModelAndView page(@RequestParam Map<String, Object> params, int page, int rows) {
		Page<Map<String, Object>> pageMap = picturesService.grid(params, page, rows);
		ModelAndView mav = new ModelAndView();
		mav.addObject("page", pageMap);
		// return JacksonJsonMapper.objectToJson(page);
		return mav;
	}

	@RequestMapping("/index")
	public String index() {
		return "pictures/pictures";
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/save")
	@ResponseBody
	public String save(HttpServletRequest request, TbPictures tdpictures, @RequestParam("file1") MultipartFile file1) {
		Boolean a = false;
		String id = tdpictures.getPicid();
//		tdpictures.setLabel(tdpictures.getLabel().replaceAll("，", ","));// 防止页面填中文,号
		File file = null;
		if (!file1.isEmpty() && ValidateUtils.isEmpty(tdpictures.getPicpath())) {
			Date time = new Date();
			String fileName = file1.getOriginalFilename();
			String dirTime = String.valueOf(time.getTime());
			String extName = fileName.substring(fileName.lastIndexOf("."));
			String fileupload=PropertiesUtils.getValue("image.path");
			file = new File(request.getRealPath(fileupload), dirTime + extName);
			try {
				file1.transferTo(file);
//				 String fileUrlpath = fileService.uploadFile(file1.getInputStream(),extName);
				tdpictures.setPicpath(fileupload.substring(1)+"/"+ dirTime + extName);
//				 tdpictures.setImage(Constant.FILEURL_PREFIX  + fileUrlpath);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	
		try {
			if (ValidateUtils.isEmpty(id)) {// 增加
				tdpictures.setPicid(IdentifierUtils.getId().generate().toString());
				
				tdpictures.setStatus(1);
			
				a = picturesService.save(tdpictures);
			} else {// 修改articleService
				
				a = picturesService.update(tdpictures);
				//菜品修改了，如果该菜品已在模板中选择了，就相应修改了模板中的数据
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (!ValidateUtils.isEmpty(a)) {
			if (ValidateUtils.isEmpty(id)) {
				map.put("message", "添加成功");
				map.put("tdpictures", tdpictures);
			} else {
				map.put("message", "修改成功");
				map.put("tdpictures", tdpictures);
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
	@RequestMapping("/validatePictures")
	@ResponseBody
	public ModelAndView validateArticle(TbPictures tbPictures){
		ModelAndView mav = new ModelAndView();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("orderNo", tbPictures.getOrderNo());
//		params.put("status", 0);
		
		List<Map<String, Object>> list=picturesService.find(params);
		
		TbPictures a=picturesService.findById(tbPictures.getPicid());
		//新增
		if(ValidateUtils.isEmpty(tbPictures.getPicid())){
			if(list!=null&&list.size()>0){
				mav.addObject("message", "图片编号不能重复");
			}
		}else{
			//修改
			if(!a.getOrderNo().equals(tbPictures.getOrderNo())){
				if(list!=null&&list.size()>0){
					mav.addObject("message", "图片编号不能重复");
				}
			}else{
				if(list!=null&&list.size()>1){
					mav.addObject("message", "图片编号不能重复");
				}
			}
		}
		
		return mav;
	}

	
	
	@RequestMapping("/findById/{id}")
	@ResponseBody
	public ModelAndView findById(@PathVariable(value = "id") String id, Model model) {
		TbPictures tbPictures = picturesService.findById(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("tbPictures", tbPictures);
		return mav;
	}

	@RequestMapping("/delete/{id}")
	@ResponseBody
	public ModelAndView deleteById(@PathVariable(value = "id") String id) {
		boolean b = picturesService.deleteById(id);
		ModelAndView mav = new ModelAndView();
		if (b) {
			mav.addObject("message", "删除成功");
		} else {
			mav.addObject("message", "删除失败");
		}
		return mav;
	}



	
	
}
