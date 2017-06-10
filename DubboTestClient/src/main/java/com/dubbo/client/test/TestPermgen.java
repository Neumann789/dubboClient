package com.dubbo.client.test;

import java.util.HashMap;
import java.util.Map;

public class TestPermgen {
	public static Map map=new HashMap();
	public static int num=0;
	public static void main(String[] args) throws Exception {
		
		while(true){
			Thread.sleep(2000);
			map.put(num, new byte[100*1024*1024]);
		}
		
	}

}
