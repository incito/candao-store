package com.candao.common.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;

public class JacksonObjectMapper extends ObjectMapper {

	public JacksonObjectMapper() {
		super();
		this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		this.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
		this.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, true);
		//空值转换-异常情况
		this.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;  
		this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
				jg.writeString("");
			}
		});
	}
}