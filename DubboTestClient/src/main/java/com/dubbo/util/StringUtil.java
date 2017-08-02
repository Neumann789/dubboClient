package com.dubbo.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

public class StringUtil {
	
	public static String addFileStamp(String fileName){
		
		String preFileName=fileName.substring(0, fileName.lastIndexOf("."));
		String suffixFileName=fileName.substring(fileName.lastIndexOf("."));
		String timeStamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return preFileName+"_"+timeStamp+suffixFileName;
	}
	
	public static String[] extractNames(String[] names){
		
		
		
		return null;
	}
	
	
	public static String genJsonStrPrettyFormat(final Object instance){
		return JSON.toJSONString(instance, new ValueFilter() {
			
			public Object process(Object object, String name, Object value) {
				
				Method method=null;
				Class  type=null;
				if(value==null){
				
				try {
					method=object.getClass().getMethod("get"+changeFirstCharacterCase(name,true), null);
					
					type=method.getReturnType();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				if(type == String.class){
					value="";
				}else if(type == Byte.class || 
						 type ==  Character.class ||
						 type ==  Short.class||
						 type ==  Integer.class||
						 type ==  Long.class){
					value=0;
					
				}else if(type ==  Boolean.class){
					
					value = true;
					
				}else if(type ==  Float.class){
					value = 0.0f;
				}else if(type ==  Double.class){
					value = 0.0;
				}else if(type ==  Timestamp.class){
					value = new Timestamp(new Date().getTime());
				}else if(type ==  BigDecimal.class){
					value =new BigDecimal(0);
				}else if(type ==  List.class){
					value =new ArrayList<>();
				}else if(type ==  Map.class){
					value =new HashMap<>();
				}else if(type.isArray()){
					value ="[{}]";
				}else{
					
					try {
						if(instance.getClass()==type){//��ζ����а���
							value=new HashMap<>();
						}else{
							value=type.newInstance();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}
			
				return value;
			}
				return value;
				
		}}, SerializerFeature.PrettyFormat);
	}
	
	
	
	public static String genJsonStrPrettyFormatNew(Class clazz,final Object instance){
		
		Map map=new HashMap<>();
		
		
		
		return JSON.toJSONString(instance, new ValueFilter() {
			
			public Object process(Object object, String name, Object value) {
				
				Method method=null;
				Class  type=null;
				if(value==null){
				
				try {
					method=object.getClass().getMethod("get"+changeFirstCharacterCase(name,true), null);
					
					type=method.getReturnType();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				if(type == String.class){
					value="";
				}else if(type == Byte.class || 
						 type ==  Character.class ||
						 type ==  Short.class||
						 type ==  Integer.class||
						 type ==  Long.class){
					value=0;
					
				}else if(type ==  Boolean.class){
					
					value = true;
					
				}else if(type ==  Float.class){
					value = 0.0f;
				}else if(type ==  Double.class){
					value = 0.0;
				}else if(type ==  Timestamp.class){
					value = new Timestamp(new Date().getTime());
				}else if(type ==  BigDecimal.class){
					value =new BigDecimal(0);
				}else if(type ==  List.class){
					value =new ArrayList<>();
				}else if(type ==  Map.class){
					value =new HashMap<>();
				}else if(type.isArray()){
					value ="[{}]";
				}else{
					
					try {
						if(instance.getClass()==type){//��ζ����а���
							value=new HashMap<>();
						}else{
							value=type.newInstance();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}
			
				return value;
			}
				return value;
				
		}}, SerializerFeature.PrettyFormat);
	}
	
	public static Map chgInstance2Map(Class clazz,final Object instance){
		Map map=new HashMap<>();
		Method[] methods=clazz.getDeclaredMethods();
		for(Method m:methods){
			
		}
		
		return null;
	}
	
	public static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str.length());
		if (capitalize) {
			sb.append(Character.toUpperCase(str.charAt(0)));
		}
		else {
			sb.append(Character.toLowerCase(str.charAt(0)));
		}
		sb.append(str.substring(1));
		return sb.toString();
	}
	
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}
	

	
	public static String jsonPrettyFormat(String jsonStr){
		
		Map resultMap=JSON.parseObject(jsonStr, Map.class);
		
		return JSON.toJSONString(resultMap, true);
	}
	
	
	
	public static void main(String[] args) {
		//System.out.println(addFileStamp("D:\\dubbclient\\jars\\test-facade-0.0.1-SNAPSHOT.jar"));
		
		/*
		 * D:\\dubbclient\\jars\\test-facade-0.0.1-SNAPSHOT_20170612.jar
		 * D:\\dubbclient\\jars\\test-facade-0.0.1-SNAPSHOT_20170603.jar
		 * D:\\dubbclient\\jars\\test-facade-0.0.1-SNAPSHOT_20170512.jar
		 * D:\\dubbclient\\jars\\test2-facade-0.0.1-SNAPSHOT_20170516.jar
		 * D:\\dubbclient\\jars\\test2-facade-0.0.1-SNAPSHOT_20170519.jar
		 * 
		 * */
	}

}
