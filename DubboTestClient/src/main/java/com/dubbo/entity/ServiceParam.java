package com.dubbo.entity;

public class ServiceParam {
	
	private String paramName;
	
	private String paramJsonContent;

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
