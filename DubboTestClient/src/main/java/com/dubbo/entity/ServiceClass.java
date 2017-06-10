package com.dubbo.entity;

import java.util.List;

public class ServiceClass {
	
	private String ownerName;
	
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

	
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public boolean equals(ServiceClass serviceClass) {
		// TODO Auto-generated method stub
		if(serviceClass==null){
			return false;
		}
		
		if(serviceClass.getClassName().equals(this.getClassName())){
			return true;
		}
		
		return false;
	}
	
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj==null){
			return false;
		}
		ServiceClass serviceClass=(ServiceClass)obj;
		if(serviceClass.getOwnerName().equals(this.getOwnerName())&&serviceClass.getClassName().equals(this.getClassName())){
			return true;
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "ServiceClass [ownerName=" + ownerName + ", className=" + className + ", serviceMethods="
				+ serviceMethods + "]";
	}

	
	

}
