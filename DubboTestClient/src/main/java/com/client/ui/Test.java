package com.client.ui;

import com.dubbo.util.ClassPathHacker;

import groovy.lang.GroovyClassLoader;

public class Test {

	public static void main(String[] agr) {
		
		//printTrigle(10);
		//System.out.println(Thread.currentThread().getContextClassLoader());
		//System.out.println(Test.class.getClassLoader());
		String jarFilePath="D:\\dubbclient\\jars\\payment-fmd-facade-1.0-SNAPSHOT.jar";
		
/*		System.out.println(System.getProperty("java.class.path"));
		System.setProperty("java.class.path", jarFilePath+";"+System.getProperty("java.class.path"));
		System.out.println(System.getProperty("java.class.path"));
		
		try {
			Class.forName("com.zb.payment.fmd.facade.FmdQueryFacade");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		jarFilePath="D:\\dubboclient\\jars\\payment-front-yw-facade-1.0-SNAPSHOT.jar";
		
		
		try {
			ClassPathHacker.addFile(jarFilePath);
			//Class clazz=Class.forName("com.zb.payment.front.facade.service.FrontService");
			
			GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
			
			
			Class clazz=Class.forName("com.zb.payment.front.facade.service.FrontService", true, classLoader);
			System.out.println(clazz.hashCode());
			Class clazz1=Class.forName("com.zb.payment.front.facade.service.FrontService", true, classLoader);
			System.out.println(clazz1.hashCode());
			jarFilePath="D:\\dubboclient\\payment-front-yw-facade-1.0-SNAPSHOT.jar";
			ClassPathHacker.addFile(jarFilePath);
			Class clazz2=Class.forName("com.zb.payment.front.facade.service.FrontService", true, classLoader);
			System.out.println(clazz2.hashCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//URL url=Test.class.getResource("");
		//System.out.println("url==>> "+url);
		
		//System.out.println(System.getProperty("PATH"));
/*		try {
			Runtime.getRuntime().exec("cmd set classpath="+"E:\\workspace\\DubboTestClient\\target\\classes");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		RuntimeMXBean rmb=(RuntimeMXBean)ManagementFactory.getRuntimeMXBean();  
		
		
		
		Method[] methods=RuntimeMXBean.class.getDeclaredMethods();
		
		for(Method method:methods){
			
			if(method.getName().startsWith("get")){
				try {
					Object obj=method.invoke(rmb, null);
					System.out.println(method.getName()+"==>> "+JSON.toJSONString(obj));
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}*/
		
		//System.setProperty("java.library.path","classpath·��");
/*		for(int i=0;i<10;i++){
			new Thread(){
				public void run() {
					
					System.out.println(Thread.currentThread().getContextClassLoader());
					
				};
			}.start();
		}
*/
	}

	public static void printTrigle(int n) {

		for (int i = 1; i <= n; i++) {

			for (int j = 1; j <= n - i; j++) {
				System.out.print(" ");
			}

			for (int k = 1; k <= i; k++) {
				System.out.print(" *");
			}
			
			System.out.println(" ");

		}

	}
}
