package com.dubbo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;

public class SpringUtil{

	private static ApplicationContext ac;
	
	private static DefaultListableBeanFactory beanFactory;
	
	private final static Logger logger = LoggerFactory.getLogger(SpringUtil.class); 

	public static void main(String[] args) {
		//ApplicationContext ac = new ClassPathXmlApplicationContext("spring.xml");
		//System.out.println(ac);
		startSpring();
	}
	
	public static void startSpring(){
		ac = new ClassPathXmlApplicationContext("spring.xml");
		
		beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) ac)
				.getBeanFactory();
		
		logger.info("spring启动成功!");
		
		//registZK("zk127.0.0.1:2181", "127.0.0.1:2181");
		
		//registDubboConsumer("com.zb.payment.fmd.facade.FmdQueryFacade");
		
		//logger.info(ac.getBean("zk127.0.0.1:2181").toString());
		
		//logger.info(ac.getBean("com.zb.payment.fmd.facade.FmdQueryFacade").toString());
		
		
	}

	public static void registBean(String beanName, BDBuilder bdb) {
		
		beanFactory.setBeanClassLoader(Thread.currentThread().getContextClassLoader());
		
		if(beanFactory.containsBean(beanName)){//如果bean工厂中bean已存在，则先销毁
			beanFactory.destroySingleton(beanName);
		}
		
		beanFactory.registerBeanDefinition(beanName, bdb.genBeanDefinitionBuilder().getRawBeanDefinition());
		
		logger.info(beanName+"注册成功!");
	}

	public static void registDubboConsumer(final String beanName) {
		
		registBean(beanName, new BDBuilder() {
			public BeanDefinitionBuilder genBeanDefinitionBuilder() {
				BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ReferenceBean.class);
				beanDefinitionBuilder.addPropertyValue("check", "false");
				beanDefinitionBuilder.addPropertyValue("cache", "true");
				beanDefinitionBuilder.addPropertyValue("interface", beanName);
				
				return beanDefinitionBuilder;
			}
		});
	}
	
	public static void registZK(final String beanName,final String address) {
		registBean(beanName, new BDBuilder() {
			public BeanDefinitionBuilder genBeanDefinitionBuilder() {
				BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RegistryConfig.class);
				beanDefinitionBuilder.addPropertyValue("protocol", "zookeeper");
				beanDefinitionBuilder.addPropertyValue("address", address);
				
				return beanDefinitionBuilder;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public static  <T> T getBean(String beanName){
		return (T)beanFactory.getBean(beanName);
		
	}
	
	public static void stopSpring(){
		beanFactory.destroySingletons();
	}


}
