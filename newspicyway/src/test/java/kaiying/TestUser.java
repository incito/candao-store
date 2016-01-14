package kaiying;

import java.util.ArrayList;
import java.util.List;

import com.candao.common.utils.JacksonJsonMapper;
import com.candao.www.data.model.TbUser;

public class TestUser {

	public static void main(String [] args) {
		
		List <TbUser> list = new ArrayList<TbUser>();
	   TbUser user = new TbUser();
	   user.setFullname("abc");
	   user.setUsername("赵新生");
	   list.add(user);
	   System.out.println( JacksonJsonMapper.objectToJson(list)); 
	}
	
	
}
