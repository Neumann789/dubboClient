package com.dubbo.util;

import java.util.List;

import com.dubbo.entity.ServiceMethod;

public class PackageUtilTest {
	
	
	
	public static void main(String[] args) {
		extractServiceMethods();
	}
	
	
	public static void extractServiceMethods(){
		
		List<ServiceMethod> list=PackageUtil.extractServiceMethods(TestService.class);
		
		for(ServiceMethod sm:list){
			System.out.println(sm);
		}
		
	}

}

interface TestService extends BaseService{
	
	public void sayHello();
	
}

interface BaseService{
	
	public void handle();
	
}


