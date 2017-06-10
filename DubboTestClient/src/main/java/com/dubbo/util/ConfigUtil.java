package com.dubbo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.client.comm.FHBException;
import com.client.ui.dubbo.DubboServiceEntity;

public class ConfigUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(ConfigUtil.class); 
	
	public static Set<String> readFile(String filePath){
		
		Set<String> lineSet=new HashSet<String>();
		
		BufferedReader br;
		
		try {
			File file=new File(filePath);
			
			if(!file.exists()){
				
				file.createNewFile();
			}
			br = new BufferedReader(new FileReader(file));
			
			String buf=null;
			
			while((buf=br.readLine())!=null&&buf.length()!=0){
				
				lineSet.add(buf);
				
			}
			
		} catch (Exception e) {
			
			logger.error(filePath+"文件读取失败，失败原因:"+e.getMessage());
		
		}
		
		return lineSet;
		
	}
	
	/**
	 * 
	 * @param filePath
	 * @param split     指定分割符
	 * @return
	 */
	public static String readFile(String filePath,String split){
		
		
		BufferedReader br;
		
		String result="";
		
		try {
			File file=new File(filePath);
			
			if(!file.exists()){
				
				file.createNewFile();
			}
			br = new BufferedReader(new FileReader(file));
			
			String buf=null;
			
			
			
			while((buf=br.readLine())!=null&&buf.length()!=0){
				
				result+=buf+split;
				
			}
			
			result=result.substring(0, result.lastIndexOf(split));
			
		} catch (Exception e) {
			
			logger.error(filePath+"文件读取失败，失败原因:"+e.getMessage());
		
		}
		
		return result;
		
	}
	
	

	/**
	 * 网关服务|com.zb.payment.MgwService|方华保
	 * @param path
	 * @return
	 */
	public static TreeMap<Integer, DubboServiceEntity> readDubboServices(String path){
		
		
		
		
		TreeMap<Integer, DubboServiceEntity> treeMap=new TreeMap<Integer, DubboServiceEntity>();
		
		int i=1;
		
		try {
			
			Set<String> lineSet= readFile(path);

			for(String line:lineSet){
				
				String[] lineArr=line.split("\\|");
				
				DubboServiceEntity dubboServiceEntity=new DubboServiceEntity();
				
				dubboServiceEntity.setServiceName(lineArr[0]);
				
				dubboServiceEntity.setServiceRule(lineArr[1]);
				
				dubboServiceEntity.setServiceDeveloper(lineArr[2]);
				
				treeMap.put(i, dubboServiceEntity);
				
				i++;
				
			}
		
		} catch (Exception e) {
			throw new FHBException(path+"文件解析异常："+e.getMessage());
		}
		
		return treeMap;
	}
	
	
	public static void main(String[] args) {
		String path="D:\\dubbclient\\dubbo.list";
		TreeMap<Integer, DubboServiceEntity> treeMap=readDubboServices(path);
		
		System.out.println(treeMap);
	}
	

}
