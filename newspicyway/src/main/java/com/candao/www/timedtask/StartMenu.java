package com.candao.www.timedtask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.dao.TmenuDao;
import com.candao.www.data.model.Tmenu;
import com.candao.www.spring.SpringContext;
import com.candao.www.webroom.service.MenuService;

//具体执行任务的类
public class StartMenu implements Job {
	MenuService menuService = (MenuService) SpringContext.getApplicationContext().getBean(MenuService.class);
	TmenuDao menuDao = (TmenuDao) SpringContext.getApplicationContext().getBean(TmenuDao.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("定时任务执行中，更新定时启动的菜谱");
		Map<String, Object> params = new HashMap<String, Object>();
		boolean flag=true;
		System.out.println("是否是分店："+PropertiesUtils.getValue("isbranch"));
		if("N".equals(PropertiesUtils.getValue("isbranch"))){
			//先将定时启用的全部启用
			params.put("ststus", "2");
			List<Tmenu> list = menuService.findEffectMenu(params);
			if(list!=null&&list.size()>0){
				for(Tmenu tmenu:list){
					tmenu.setStatus(1);
					flag=flag&&menuDao.update(tmenu)>0;
					if(flag){
						System.out.println(tmenu.getMenuname()+"启用成功！");
						//这边调家荣那边的消息推送接口，让pad更新菜谱
					}
				}
			}
			params.put("ststus", "1");
			List<Tmenu> listeffect = menuService.findEffectMenu(params);
			if(listeffect!=null&&listeffect.size()>1){
			for(int i=0;i<listeffect.size();i++){
				for(int j=i+1;j<listeffect.size();j++){
					if(listeffect.get(i).getBranchidlist()!=null
							&&listeffect.get(j).getBranchidlist()!=null
							&&listeffect.get(i).getStatus()==1&&listeffect.get(j).getStatus()==1){
						if(listeffect.get(i).getBranchidlist().containsAll(listeffect.get(j).getBranchidlist())){
							Tmenu tmenu=listeffect.get(j);
							tmenu.setStatus(4);
							menuDao.update(tmenu);
						}
						
					}
				}
			}
		}
		}else{
			params.put("ststus", "1,2");
			List<Tmenu> list = menuService.findEffectMenu(params);
			if(list!=null&&list.size()>0){
				//获取第一个菜谱
				Tmenu firstmenu=list.get(0);
				if(firstmenu.getStatus()!=1){
				//如果第一个菜谱不是启用状态  设置为启用
					firstmenu.setStatus(1);
					if(menuDao.update(firstmenu)>0){
						System.out.println(firstmenu.getMenuname()+"启用成功！");
						//这边调家荣那边的消息推送接口，让pad更新菜谱
					}
				}
				//循环剩下的菜谱，如果是启用状态，就设置为未启用
				for(int i=1;i<list.size();i++){
					if(list.get(i).getStatus()==1){
						list.get(i).setStatus(4);
						menuDao.update(list.get(i));
					}
				}
			}else{
				System.out.println("该门店没有定时启用和已启用的菜谱");
			}
		}
	}
}