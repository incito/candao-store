package kaiying;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.candao.common.utils.JacksonJsonMapper;

public class Test4 {

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("age", "23");
		map.put("age2", "23");
		map.put("age3", "23");
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("age", "23");
		map2.put("age2", "23");
		map2.put("age3", "23");
		
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		list.add(map);
		list.add(map2);
		
		String json = JacksonJsonMapper.objectToJson(list);
		System.out.println(json);

	}

}
