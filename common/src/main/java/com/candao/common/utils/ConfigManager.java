package com.candao.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
	private static Properties properties;
	private static String filename = "/config.properties";

	public static String getProperty(String key) {		
		if (null == properties) {
			properties = new Properties();
			InputStream in = ConfigManager.class.getResourceAsStream(filename);
			try {
				properties.load(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return properties.getProperty(key);
	}
}
