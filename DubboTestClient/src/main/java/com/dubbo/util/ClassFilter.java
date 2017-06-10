package com.dubbo.util;

import com.dubbo.entity.ServiceClass;

public abstract class ClassFilter {
	
	
	public boolean filter(Class clazz){
		return true;
	};
	
	public ServiceClass filterDubboService(Class clazz){
		return null;
	};

}
