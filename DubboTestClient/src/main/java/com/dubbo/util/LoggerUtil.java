package com.dubbo.util;

import com.alibaba.fastjson.JSON;

public class LoggerUtil {
	
	public static void print(Object obj){
		System.out.println(JSON.toJSON(obj));
	}
	
	public static void print(String msg){
		System.out.println(msg);
	}
	
	public static void info(Object obj){
		print(obj);
	}
	
	public static void info(String msg){
		print(msg);
	}
	
	public static void error(Object obj){
		print(obj);
	}
	
	
	public static void error(Object obj,Throwable t){
		print(obj);
		t.printStackTrace();
	}
	
	public static void error(String msg,Throwable t){
		print(msg);
		t.printStackTrace();
	}
}
