/**
 * 
 */
package kaiying;

import java.io.IOException;




import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.candao.common.compress.impl.GzipCompress;
import com.candao.common.utils.CompressOperate;
import com.candao.common.utils.HttpUtil;
import com.candao.www.timedtask.BranchDataSyn;

/**
 * @author yuchenxu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:branch.xml","classpath:branchmq.xml","classpath:servlet-context.xml"})
public class MQTest_Clint {
	
	private static final Logger logger = LoggerFactory.getLogger(BranchDataSyn.class);
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier("centerQueue")
	private Destination destination;
	@Autowired
	private BranchDataSyn branchDataSyn;
	
	//@Test
	public static void synData() throws HttpException, IOException{
		System.out.println("###############start");
		String url = "http://localhost:8080/newspicyway/padinterface/jdesyndata";
		Map<String,String> map = new HashMap<String,String>();
		map.put("synkey", "candaosynkey");
		String json = JSON.toJSONString(map);
		Map<String,String> map2 = new HashMap<String,String>();
		map2.put("json", json);
		String result = HttpUtil.doPost(url, map2, null, "UTF-8");
		System.out.println(result);
	}
	//@Test
	public void recive(){
		
	}
	
	public static void main(String args[]) throws IOException{
		//加载spring容器
       /* ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:branchmq.xml","classpath:servlet-context.xml");    
        context.start();  
        System.out.println("Press any key to exit.");
        System.in.read();*/   
		synData();
	}

}
