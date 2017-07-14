package com.dubbo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.client.comm.FHBException;


public class FileUtil {
	

	public static void moveFile(String srcPath,String destPath) {
		
		File file=new File(srcPath);
		
		File destFile=new File(destPath);
		
		OutputStream os=null;
		
		InputStream  is=null;
		
		try {
			if(destFile.exists()){
				if(destFile.delete()){
					destFile.createNewFile();
				}else{
					throw new Exception("存在同名文件!");
				}
			}
			
			os=new FileOutputStream(destFile);
			
			is=new FileInputStream(file);
			
			byte[] buf=new byte[1024];
			int temp = 0;
			while(true){
	            temp = is.read(buf,0,buf.length);
	            if(temp == -1){
	                break;
	            }
	            os.write(buf,0,temp);
	        } 
		} catch (Exception e) {
			LoggerUtil.error("移动文件失败:"+e.getMessage());
			throw new RuntimeException("移动文件失败:"+e.getMessage());
		}finally {
			try{
				if(is!=null){
					is.close();
				}
				if(os!=null){
					os.close();
				}
            }
            catch(Exception e2){
                LoggerUtil.error("移动文件失败:"+e2.getMessage());
                throw new RuntimeException("移动文件失败:"+e2.getMessage());
            }
		}
		
	}
	
	
	public static  List<String> getLineMapList(String filePath){
		List<String> lineList=new ArrayList<String>();
		BufferedReader br=null;
		
		String buf=null;
		
		try {
			
			br=new BufferedReader(
					new InputStreamReader(FileUtil.class.getResourceAsStream(filePath)
				));
			
			while((buf=br.readLine())!=null){
				if(buf.trim().length()!=0&&!buf.startsWith("#")){
					lineList.add(buf);
				}
			}
		} catch (Exception e) {
			LoggerUtil.error("文件解析失败:"+e.getMessage());
			throw new FHBException("文件解析失败:"+e.getMessage());
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		return lineList;
	}
	
	public static void saveFile(String content,String path){
		PrintWriter pw=null;
		try {
			
			pw=new PrintWriter(path);
			
			pw.print(content);
			
		} catch (Exception e) {
			
			throw new FHBException("保存文件异常："+e.getMessage());
			
		}finally {
			
			if(pw!=null){
				pw.close();
			}
			
		}
		
	}
	
	
	
}
