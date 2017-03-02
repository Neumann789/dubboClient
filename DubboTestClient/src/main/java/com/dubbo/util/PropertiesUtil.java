package com.dubbo.util;

import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	
	public static Properties loadProperties(String configPath){
		
		Properties pro=new Properties();
		
		try {
			pro.load(PropertiesUtil.class.getResourceAsStream(configPath));
			
//			System.out.println(PropertiesUtil.class.getResourceAsStream("").);
		} catch (Exception e) {
			logger.error("tabs.propertiesº”‘ÿ“Ï≥£:"+e.getMessage());
		}
		return pro;
		
	}
	
	public static void main(String[] args) {
		Properties pro=loadProperties("/tabs.properties");
		Set<Object> keySet=pro.keySet();
		for(Object key:keySet){
			
			System.out.println((String)key+"   "+pro.get(key));
			
		}
	}

}
