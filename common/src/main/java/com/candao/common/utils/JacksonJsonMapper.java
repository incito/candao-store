package com.candao.common.utils;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

public class JacksonJsonMapper {
	static volatile ObjectMapper objectMapper = null;

	private JacksonJsonMapper() {
	}

	public static ObjectMapper getInstance() {
		if (objectMapper == null) {
			synchronized (ObjectMapper.class) {
				if (objectMapper == null) {
					objectMapper = new JacksonObjectMapper();
				} 
			}
		}
		return objectMapper;
	}

	public static String objectToJson(Object beanobject) {
		ObjectMapper mapper = JacksonJsonMapper.getInstance();
		String resutl = null;
		try {
			resutl = mapper.writeValueAsString(beanobject);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		return resutl;
	}

	public static <T> T jsonToObject(String jsonString, Class<T> claszz) {
		T t = null;
		try {
			t = JacksonJsonMapper.getInstance().readValue(jsonString, claszz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	/**
	 * json字符串转换为泛型列表
	 * @param jsonString,json字符串
	 * @param claszz,列表中的泛型类
	 * @return
	 */
	public static <E, T> ArrayList<E> jsonToList(String jsonString, Class<T> claszz) {
		ObjectMapper mapper = JacksonJsonMapper.getInstance();
		ArrayList<E> t = null;
		try {
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, claszz);   
			t = mapper.readValue(jsonString, javaType); 
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
}
