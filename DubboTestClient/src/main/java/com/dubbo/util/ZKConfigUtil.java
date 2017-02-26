package com.dubbo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZKConfigUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(ZKConfigUtil.class); 
	
	public static List<String> readFile(String zkConfigPath){
		
		List<String> lineList=new ArrayList<String>();
		
		BufferedReader br;
		
		try {
			File file=new File(zkConfigPath);
			
			if(!file.exists()){
				
				file.createNewFile();
			}
			br = new BufferedReader(new FileReader(file));
			
			String buf=null;
			
			while((buf=br.readLine())!=null&&buf.length()!=0){
				
				lineList.add(buf);
				
			}
			
		} catch (Exception e) {
			
			logger.error(zkConfigPath+"文件读取失败，失败原因:"+e.getMessage());
		
		}
		
		return lineList;
		
	}

}
