/*package com.dubbo.util;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.registry.zookeeper.ZookeeperRegistry;
import com.zb.payment.yw.mgw.facade.service.YWTransReceiverService;

public class TestComsumer {
	public static void main(String[] args) {
		
		ApplicationConfig application=new ApplicationConfig();
		application.setName("ttt");
		RegistryConfig registryConfig=new RegistryConfig();
		registryConfig.setProtocol("zookeeper");
		registryConfig.setAddress("192.168.224.199:2181");
		registryConfig.setUsername("aaa");
		registryConfig.setPassword("aaa");
		
		ReferenceConfig referenceConfig=new ReferenceConfig<>();
		
		referenceConfig.setApplication(application);
		
		referenceConfig.setRegistry(registryConfig);
		
		referenceConfig.setInterface(YWTransReceiverService.class);
		
		//referenceConfig.setVersion("1.0");
		
		YWTransReceiverService ywTransReceiverService=(YWTransReceiverService)referenceConfig.get();
		
		ywTransReceiverService.syncSendAndReceive(null);
		
	}
}
*/