package com.candao.www.bossstore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: 张文栋
 * Date: 15/7/23
 * Desc: 属性文件工具类
 */
@Component
public class PropertiesUtil {
    @Autowired
    @Qualifier("appconfig")
    private Properties properties;
    private static PropertiesUtil piu;

    /**
     * 初始化工具类默认属性
     */
    @PostConstruct
    public void init() {
        piu = this;
        piu.properties = this.properties;
    }

    public static String getValue(String key) {
        return piu.properties.getProperty(key, null);
    }

    /**
     * 获取属性列表，根据传入参数的模糊匹配
     *
     * @param key,需要查找属性前缀
     * @return
     * @describe 比如属性文件中有
     * mail.smtp.host=smtp.incito.com.cn
     * mail.smtp.port=25
     * mail.smtp.auth=true
     * 通过该方法，便可以使用findValues("mail.smtp")将所有相关属性获取，并放入一个LinkedHashMap中
     */
    public static Map<String, String> findValues(String key) {
        Map<String, String> propMap = new LinkedHashMap<String, String>();
        Iterator<Object> it = piu.properties.keySet().iterator();
        while (it.hasNext())
        {
            String k = String.valueOf(it.next());
            if (k.startsWith(key))
            {
                String v = piu.properties.getProperty(k, null);
                propMap.put(k, v);
            }
        }
        return propMap;
    }
}
