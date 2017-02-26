package com.dubbo.entity;

import java.util.List;

public class ServiceClass {
	
	private String className;
	
	private List<ServiceMethod> serviceMethods;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<ServiceMethod> getServiceMethods() {
		return serviceMethods;
	}

	public void setServiceMethods(List<ServiceMethod> serviceMethods) {
		this.serviceMethods = serviceMethods;
	}

	@Override
	public String toString() {
		return "ServiceClass [className=" + className + ", serviceMethods=" + serviceMethods + "]";
	}
	
	

}
