package com.dubbo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

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
					throw new Exception("删除文件失败!");
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
			logger.error("移动文件失败:"+e.getMessage());
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
                logger.error("移动文件失败:"+e2.getMessage());
                throw new RuntimeException("移动文件失败:"+e2.getMessage());
            }
		}
		
	}
	
}
