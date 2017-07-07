package com.dubbo.entity;

import java.util.Map;

public class ServiceParam {
	
	private String paramName;
	
	private String paramJsonContent;
	
	private boolean isAbstract=false;
	
	private Map<String,ServiceParam> childrenParamMap;
	
	private String realUsePramName;
	
	
	
	public String getRealUsePramName() {
		return realUsePramName;
	}

	public void setRealUsePramName(String realUsePramName) {
		this.realUsePramName = realUsePramName;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public Map<String, ServiceParam> getChildrenParamMap() {
		return childrenParamMap;
	}

	public void setChildrenParamMap(Map<String, ServiceParam> childrenParamMap) {
		this.childrenParamMap = childrenParamMap;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamJsonContent() {
		return paramJsonContent;
	}

	public void setParamJsonContent(String paramJsonContent) {
		this.paramJsonContent = paramJsonContent;
	}

	@Override
	public String toString() {
		return "ServiceParam [paramName=" + paramName + ", paramJsonContent=" + paramJsonContent + "]";
	}

	
	

}
