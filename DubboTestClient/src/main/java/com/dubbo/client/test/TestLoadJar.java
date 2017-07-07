package com.dubbo.client.test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.GroovyClassLoader;

public class TestLoadJar {
	
	private final static Logger logger = LoggerFactory.getLogger(TestLoadJar.class);
	
	public static void main(String[] args) throws Exception {
		
		GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
		
//		classLoader.addClasspath("D:\\dubboclient\\jars\\payment-front-yw-facade-1.0-SNAPSHOT.jar");
//		
//		classLoader.loadClass("com.zb.payment.front.facade.service.FrontService");
		
		//testJava(classLoader);
		//testJar(classLoader);
		
		//testJar2(classLoader);
		
		//testJar3(classLoader);
		
		//testJar4();
		
		//testJar5();
		
		testJar6();
		
	}
	
	
	private static void testJar2(GroovyClassLoader classLoader) throws Exception{
		
		String jarFilePath="D:\\dubboclient\\jars\\payment-front-yw-facade-1.0-SNAPSHOT.jar";
		
		List<String> classList=getClassListByJarFile(new File(jarFilePath).getAbsolutePath(), true);
		
		for(String className:classList){
			System.out.println(className);
		}
		
	}
	
	
	private static void testJar3(GroovyClassLoader classLoader) throws Exception{
		
		String jarFilePath="D:\\dubboclient\\jars\\payment-front-yw-facade-1.0-SNAPSHOT.jar";
		
		classLoader.addURL(new File(jarFilePath).toURL());
		
		//Class clazz=classLoader.loadClass("com.zb.payment.front.facade.service.FrontService");
		Class clazz=classLoader.parseClass(new File("F:\\GIT20170610\\ServiceClass.java"));
		
		System.out.println(clazz.getName());
		
		System.out.println(clazz.getClassLoader());
		
		Class[] clazzArr=classLoader.getLoadedClasses();
		
		for(Class cls:clazzArr){
			System.out.println(cls.getName());
		}
		
		
	}
	
	private static void testJar4() throws Exception{
		
		GroovyClassLoader classLoader=new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
		
		String jarFilePath="D:\\dubboclient\\jars\\payment-front-yw-facade-1.0-SNAPSHOT.jar";
		
		classLoader.addURL(new File(jarFilePath).toURL());
		
		List<String> classList=getClassListByJarFile(jarFilePath,true);
		
		for(String className:classList){
			
			Class clazz=classLoader.loadClass(className);
			
			System.out.println(clazz+"==="+clazz.hashCode());
		
		}
		
		
	}
	
	private static void testJar5() throws Exception{
		testJar4();
		System.out.println("#######################");
		testJar4();
		System.out.println("#######################");
		testJar4();
		
	}
	
	private static void testJar6() throws Exception{
		
		String basePath="D:\\dubboclient\\jars";
		
		Map<String,Map<String,Class>> jarContainer=new HashMap<>();
		
		while(true){
			
			Thread.currentThread().sleep(5000);
			
			logger.info("每5秒检查一次!");
			
			List<File>  jarFileList=listBaseFiles(basePath);
			
			for(File jarFile:jarFileList){
				logger.info("加载jar:"+jarFile.getAbsolutePath());
				loadJar(jarFile, jarContainer);
				
			}
			
			logger.info("当前容器已加载jar的数量是："+jarContainer.size());
			
			Map<String,Class> classMap=jarContainer.get(basePath+"\\dbframe-0.0.1-SNAPSHOT.jar");
			
			Class clazz=classMap.get("com.test.Hello");
			
			Method m=clazz.getMethod("sayHello", new Class[]{});
			
			m.invoke(null, new Object[]{});
			
			
			
			
			
		}
		
		
	}
	
	private static GroovyClassLoader loadJar(File jarFile,Map<String,Map<String,Class>> jarContainer) throws Exception{
		
		GroovyClassLoader classLoader=new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
		
		
		classLoader.addURL(jarFile.toURL());
		
		List<String> classNameList=getClassListByJarFile(jarFile.getAbsolutePath(),true);
		
		Map<String,Class> classMap=new HashMap<>();
		
		for(String className:classNameList){
			
			Class clazz=classLoader.loadClass(className);
			
			classMap.put(clazz.getName(), clazz);
			
			//logger.info(clazz+"==="+clazz.hashCode());
		
		}
		
		jarContainer.put(jarFile.getAbsolutePath(), classMap);
		
		return classLoader;
	}
	
	private static List<File> listBaseFiles(String basePath){
		
		List<File>  jarFileList=new ArrayList<>();
		
		File file=new File(basePath);
		
		File[] baseFiles=file.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				
				if(file.isFile()&&file.getName().endsWith(".jar")){
					return true;
				}
				return false;
			}
		});
		
		for(File f:baseFiles){
			logger.info(f.getAbsolutePath());
			jarFileList.add(f);
		}
		
		return jarFileList;
		
	}
	
	
	private static void testJar(GroovyClassLoader classLoader) throws Exception{
		
		String jarFilePath="D:\\dubboclient\\jars\\payment-front-yw-facade-1.0-SNAPSHOT.jar";
		
		classLoader.addURL(new File(jarFilePath).toURL());
		
		
		Class clazz=classLoader.loadClass("com.zb.payment.front.facade.service.FrontService");
		
		System.out.println(clazz.hashCode());

		jarFilePath="D:\\dubboclient\\payment-front-yw-facade-1.0-SNAPSHOT.jar";
		
		classLoader.clearCache();
		
		classLoader.addURL(new File(jarFilePath).toURL());
		
		Class clazz1=classLoader.loadClass("com.zb.payment.front.facade.service.FrontService");
		
		System.out.println(clazz1.hashCode());
	}

	private static void testJava(GroovyClassLoader classLoader) throws IOException {
		Class clazz=classLoader.parseClass(new File("F:\\GIT20170610\\dubboClient\\DubboTestClient\\src\\main\\java\\com\\dubbo\\entity\\ServiceClass.java"));
		
		
		System.out.println(clazz.hashCode());
		
		Class clazz1=classLoader.parseClass(new File("F:\\GIT20170610\\dubboClient\\DubboTestClient\\src\\main\\java\\com\\dubbo\\entity\\ServiceClass.java"));
		
		System.out.println(clazz1.hashCode());
		
		Class clazz2=classLoader.parseClass(new File("F:\\GIT20170610\\ServiceClass.java"));
		
		System.out.println(clazz2.hashCode());
		
		System.out.println(clazz2==clazz1);
	}
	
	
	
	/**
	 * 从jar获取某包下所有类
	 * 
	 * @param jarPath
	 *            jar文件路径
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	private static List<String> getClassListByJarFile(String jarFilePath, boolean childPackage) {
		List<String> classList = new ArrayList<String>();
		JarFile jarFile=null;
		try {
			jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(".class")) {
					if (childPackage) {
						entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
						classList.add(entryName);
					} else {
						int index = entryName.lastIndexOf("/");
						String myPackagePath;
						if (index != -1) {
							myPackagePath = entryName.substring(0, index);
						} else {
							myPackagePath = entryName;
						}
						entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
						classList.add(entryName);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally {
			
			if(jarFile!=null){
				try {
					jarFile.close();
				} catch (IOException e) {
					logger.error(e.getMessage(),e);
				}
			}
			
		}
		return classList;
	}
	
	

}
