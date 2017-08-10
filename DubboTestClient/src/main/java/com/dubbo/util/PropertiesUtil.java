package com.dubbo.util;

import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {
	
	private static Properties pro=new Properties();
	
	static{
		
		loadProperties("/client.properties");
		
	}
	
	
	public static Properties loadProperties(String configPath){
		
		
		try {
			//防止中文乱码
			pro.load(new InputStreamReader(PropertiesUtil.class.getResourceAsStream(configPath), "UTF-8"));
			
//			System.out.println(PropertiesUtil.class.getResourceAsStream("").);
		} catch (Exception e) {
			LoggerUtil.error("tabs.properties加载异常:"+e.getMessage());
		}
		return pro;
		
	}
	
	public static String getVal(String key){
		
		return pro.getProperty(key);
		
	}
	
	public static void main(String[] args) {
		System.out.println(pro.get("mainui.title"));
	}

}
