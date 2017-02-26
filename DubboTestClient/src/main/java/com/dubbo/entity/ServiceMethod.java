package com.dubbo.entity;

import java.util.Arrays;
import java.util.List;

public class ServiceMethod {
	
	private String methodName;
	
	private String returnType;
	
	private List<ServiceParam> serviceParams;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<ServiceParam> getServiceParams() {
		return serviceParams;
	}

	public void setServiceParams(List<ServiceParam> serviceParams) {
		this.serviceParams = serviceParams;
	}

	@Override
	public String toString() {
		return "ServiceMethod [methodName=" + methodName + ", returnType=" + returnType + ", serviceParams="
				+ serviceParams + "]";
	}
	
	

}
