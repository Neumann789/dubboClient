package com.dubbo.client.test;

import java.io.IOException;
import java.lang.management.ManagementFactory;

public class TestRunTime {
	
	public static String pid="";
	
	public static void main(String[] args) throws Exception {
		String name = ManagementFactory.getRuntimeMXBean().getName();  
		System.out.println(name);  
 
		pid = name.split("@")[0];  
		System.out.println("Pid is:"+pid);  
		
		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
					Runtime.getRuntime().exec("taskkill /PID "+pid+" /F");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}.start();
		
		System.in.read();

	}
	
	public void killSelf(){
		String name = ManagementFactory.getRuntimeMXBean().getName();
		String pid = name.split("@")[0];
		try {
			Runtime.getRuntime().exec("taskkill /PID "+pid+" /F");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
