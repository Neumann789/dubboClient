package com.dubbo.client.test;

import java.util.TreeMap;

public class TestTreeMap {
	
	public static void main(String[] args) {
		
		TreeMap<Integer, String> treeMap=new TreeMap<Integer, String>();
		
		treeMap.put(2, "aa");
		treeMap.put(7, "aa");
		treeMap.put(1, "aa");
		treeMap.put(9, "aa");
		treeMap.put(0, "aa");
		treeMap.put(2, "bb");
		
		System.out.println(treeMap);
		
		
	}

}
