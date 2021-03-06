package com.client.ui.dubbo;

public class DubboServiceEntity {
	
	private String id;
	
	private String serviceName;
	
	private String serviceRule;
	
	private String serviceDeveloper;
	
	public DubboServiceEntity(){}
	
	public DubboServiceEntity(String serviceName,String serviceRule,String serviceDeveloper){
		this.serviceName=serviceName;
		this.serviceRule=serviceRule;
		this.serviceDeveloper=serviceDeveloper;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceRule() {
		return serviceRule;
	}

	public void setServiceRule(String serviceRule) {
		this.serviceRule = serviceRule;
	}

	public String getServiceDeveloper() {
		return serviceDeveloper;
	}

	public void setServiceDeveloper(String serviceDeveloper) {
		this.serviceDeveloper = serviceDeveloper;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "DubboServiceEntity [id=" + id + ", serviceName=" + serviceName + ", serviceRule=" + serviceRule
				+ ", serviceDeveloper=" + serviceDeveloper + "]";
	}
	
	
	
	

}
