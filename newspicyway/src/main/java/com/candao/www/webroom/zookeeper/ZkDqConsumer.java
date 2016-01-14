package com.candao.www.webroom.zookeeper;

import java.util.HashMap;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.state.ConnectionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TSynSqlMapper;
import com.candao.www.webroom.model.SynSqlObject;

@Service
public class ZkDqConsumer implements QueueConsumer<SynSqlObject> {

	
    @Autowired
    TSynSqlMapper  tSynSqlMapper;
    
    
	public void stateChanged(CuratorFramework framework, ConnectionState state) {
		
		System.out.println("State [" + state + "]");
		
	}

	public void consumeMessage(SynSqlObject synSqlObject) throws Exception {
		
		//插入到数据表  t_sys_sql
		//为了保持分店和总店的同样的格式，使用同样的同步方式
//		String comsulerStr = ZipUtils.unzip(receData);
//		System.out.println("分店开始同步数据");	
		
		if(synSqlObject != null){
			//分店接收的数据
//			String flag = receData.getFlag();
			if("0".equals(synSqlObject.getFlag())){
				 
				System.out.println("分店开始同步数据");	
				
				synSqlObject.setStatus(0);
			    tSynSqlMapper.insert(synSqlObject);
			   
			   String result = "0";
	   	       Map<String, Object> mapParam = new HashMap<String, Object>();
	   	       mapParam.put("branchid", synSqlObject.getBranchid());
	   	       mapParam.put("result", result);
	   	       tSynSqlMapper.synData(mapParam);
		   	  
			}
		}
	}


}
