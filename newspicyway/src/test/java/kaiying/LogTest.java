package kaiying;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.candao.www.data.model.Torder;
import com.candao.www.data.model.Tworklog;
import com.candao.www.webroom.service.DataDictionaryService;
import com.candao.www.webroom.service.OrderService;
import com.candao.www.webroom.service.WorkLogService;
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml","classpath:servlet-context.xml"})
public class LogTest {

	@Autowired
	private OrderService orderService; 

	@Autowired
	private WorkLogService workLogService;
	
	@Autowired
	private DataDictionaryService datadictionaryService;
	
 
	
//	@Test
	public void test1(){
		Torder torder = new Torder();
		torder.setTableNo("015");
		torder.setUsername("admin");
		torder.setManNum(1);
		torder.setWomanNum(1);
//		orderService.startOrder(torder);
	/*	PadInterfaceController ss = new PadInterfaceController();
		ss.startOrderInfo(torder);*/
		System.out.println(orderService.startOrder(torder));
	}
	
//	@Test
	public void test2() throws ParseException{
		Tworklog tworklog = new Tworklog();
		tworklog.setWorkid(UUID.randomUUID().toString());
		tworklog.setWorktype("3");
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
		tworklog.setBegintime(new Date());
		tworklog.setEndtime(new Date());
		tworklog.setIpaddress("127.0.0.1");
		tworklog.setStatus(1);
		tworklog.setTableid(null);;
		tworklog.setOrderid(null);
		workLogService.saveLog(tworklog);
		System.out.println("成功！1234");
	}
	
//	@Test
	public void test3() throws Exception{
		List<Map<String, Object>> list = datadictionaryService.getDatasByType("WORKTYPE");
		for(int i=0;i<list.size();i++){
			if(list.get(i).get("itemDesc").equals("开桌")){
				System.out.println(list.get(i).get("itemid").toString());
			};
		}
	}
}
