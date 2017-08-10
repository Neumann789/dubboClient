package com.dubbo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

public final class RuntimeUtil {
	 
    public static String exec(String command) {
        StringBuilder sb = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            process.getOutputStream().close();
            reader.close();
            process.destroy();
        } catch (Exception e) {
            LoggerUtil.error("执行外部命令错误，命令行:" + command, e);
        }
        return sb.toString();
    }
 
    public static String jps() {
        return exec("jps -l");
    }
    
    public static String java(){
    	
    	return exec("java -version");
    	
    }
    
	public static void killSelf(){
		String name = ManagementFactory.getRuntimeMXBean().getName();
		String pid = name.split("@")[0];
		try {
			Runtime.getRuntime().exec("taskkill /PID "+pid+" /F");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    
    
    
    
    public static void main(String[] args) {
		System.out.println(java());
	}
}
