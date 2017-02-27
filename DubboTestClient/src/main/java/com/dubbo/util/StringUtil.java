package com.dubbo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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
