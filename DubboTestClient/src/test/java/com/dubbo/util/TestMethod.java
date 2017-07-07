package com.dubbo.util;

public class TestMethod {
	
	public static void main(String[] args) {
		test("jack");
	}
	
	public static void test(String name){
		
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println("name==>> "+name);
		System.out.println("Thread.currentThread().getStackTrace()[1].getMethodName()==>"+Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println("Thread.currentThread().getStackTrace()[1].getFileName()==>"+Thread.currentThread().getStackTrace()[1].getFileName());
		System.out.println("Thread.currentThread().getStackTrace()[1].getLineNumber()==>"+Thread.currentThread().getStackTrace()[1].getLineNumber());
		System.out.println("Thread.currentThread().getStackTrace()[1].getLineNumber()==>"+Thread.currentThread().getStackTrace()[0]);
		
	}

}
