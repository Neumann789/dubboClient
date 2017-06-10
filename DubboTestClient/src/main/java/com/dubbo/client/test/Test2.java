package com.dubbo.client.test;

public class Test2 {
	
	
	public static void main(String[] args) throws Exception {
		
		while(true){
			
			Thread.sleep(100);
			
		int result=	new T().add(3, 4);
		System.out.println("result==>> "+result);
			
		}
		
	}
	
	

}

class T{
	
	public int add(int a,int b){
		return a+b;
	}
	
}
