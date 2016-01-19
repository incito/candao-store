package kaiying;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.candao.common.utils.JacksonJsonMapper;

public class jsonToObject {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
//		String jsonString="{A2:{title:\"ttt\",source:\"tt\",id:\"28f7d728-48c1-40eb-b185-09249268f8bc\",image:\"\"},A1:{title:\"ttt\",source:\"tt\",id:\"28f7d728-48c1-40eb-b185-09249268f8bc\",image:\"\"}}";
	    String jsonString="{\"result\":\"1\"}";
	   System.out.println(jsonString.replace("\\", "")); 
		/*@SuppressWarnings("unchecked")*/
		Map<String, Object> map =JacksonJsonMapper.jsonToObject(jsonString, Map.class);
		String json=JacksonJsonMapper.objectToJson(map.get("result"));
		System.out.println(map.get("result"));
		System.out.println(map.get("result"));
		System.out.println(json);
		
		
		String jsonStringtest="[{title:\"ttt\",source:\"tt\",id:\"28f7d728-48c1-40eb-b185-09249268f8bc\",image:\"\"},{title:\"ttt\",source:\"tt\",id:\"28f7d728-48c1-40eb-b185-09249268f8bc\",image:\"\"}]";
		List list =JacksonJsonMapper.jsonToObject(jsonStringtest, ArrayList.class);
		System.out.println(list);
		
		
	}
}

 
