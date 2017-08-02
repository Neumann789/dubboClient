package com.dubbo.util;

import java.util.Random;

public class CommonUtil {
	
	
	public static int getRandom(int size){
		
		return new Random().nextInt(size);
	}
	
	public static void main(String[] args) {
		
		for(int i=0;i<20;i++){
			System.out.println(getRandom(5));
		}
		
	}

}
