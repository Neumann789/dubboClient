package com.dubbo.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.dubbo.entity.ServiceClass;
import com.dubbo.entity.ServiceMethod;
import com.dubbo.entity.ServiceParam;

public class PackageUtil {

	private static URLClassLoader myClassLoader;

	private final static Logger logger = LoggerFactory.getLogger(PackageUtil.class);

	public static void main(String[] args) throws Exception {

		String jarFilePath = "E:\\workspace\\test-facade\\target\\test-facade-0.0.1-SNAPSHOT.jar";

		List<ServiceClass> serviceClassList = new ArrayList<ServiceClass>();

		loadFacadeClassFromJar(serviceClassList, jarFilePath);

	}

	public static void loadFacadeClassFromJar(List<ServiceClass> serviceClassList, String jarFilePath)
			throws Exception {
		List<Class> classList = loadJarFileNew(jarFilePath);

		List<Class> facadeList = extractClassListByFilter(classList, new ClassFilter() {

			public boolean filter(Class clazz) {
				if (clazz.getName().contains("facade")) {
					return true;
				} else {
					return false;
				}
			}
		});

		for (Class clazz : facadeList) {

			ServiceClass serviceClass = new ServiceClass();

			serviceClass.setClassName(clazz.getName());
			serviceClass.setServiceMethods(extractServiceMethods(clazz));
			serviceClassList.add(serviceClass);

			System.out.println(serviceClass.toString());

		}
	}

	public static void loadFacadeClassFromJars(List<ServiceClass> serviceClassList, String[] jarFilePathArr)
			throws Exception {
		// List<Class> classList=loadJarFiles(jarFilePathArr);
		List<Class> classList = loadJarFilesNew(jarFilePathArr);

		List<Class> facadeList = extractClassListByFilter(classList, new ClassFilter() {

			public boolean filter(Class clazz) {
				if (clazz.getName().contains("facade")) {
					return true;
				} else {
					return false;
				}
			}
		});

		for (Class clazz : facadeList) {

			ServiceClass serviceClass = new ServiceClass();

			serviceClass.setClassName(clazz.getName());
			serviceClass.setServiceMethods(extractServiceMethods(clazz));
			serviceClassList.add(serviceClass);

			// System.out.println(serviceClass.toString());

		}
	}

	public static Method[] extractMethodsFromClass(Class clazz) {

		Method[] methods = clazz.getDeclaredMethods();

		return methods;

	}

	public static List<ServiceMethod> extractServiceMethods(Class clazz){
		
		Method[] methods=clazz.getDeclaredMethods();
		
		List<ServiceMethod> serviceMethods=new ArrayList<ServiceMethod>();
		
		for(int i=0;i<methods.length;i++){
			
			ServiceMethod serviceMethod=new ServiceMethod();
			
			serviceMethod.setMethodName(methods[i].getName());
			
			serviceMethod.setReturnType(methods[i].getReturnType().getName());
			
			Class[] paramTypes=methods[i].getParameterTypes();
			
			List<ServiceParam> serviceParams=new ArrayList<ServiceParam>();
			
			for(int j=0;j<paramTypes.length;j++){
				
				ServiceParam serviceParam=new ServiceParam();
				
				serviceParam.setParamName(chgPrimitiveType(paramTypes[j].getName()));
				
				try {
					serviceParam.setParamJsonContent(JSON.toJSONString(paramTypes[j].newInstance(), new ValueFilter() {
						
						public Object process(Object object, String name, Object value) {
							
							Method method=null;
							Class  type=null;
							if(value==null){
								
							
							try {
								method=object.getClass().getMethod("get"+StringUtils.capitalize(name), null);
								
								type=method.getReturnType();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
							
							if(type == String.class){
								value="";
							}else if(type == Byte.class || 
									 type ==  Character.class ||
									 type ==  Short.class||
									 type ==  Integer.class||
									 type ==  Long.class){
								value=0;
								
							}else if(type ==  Boolean.class){
								
								value = true;
								
							}else if(type ==  Float.class){
								value = 0.0f;
							}else if(type ==  Double.class){
								value = 0.0;
							}else if(type ==  Timestamp.class){
								value = new Timestamp(new Date().getTime());
							}else{
								
								try {
									value=type.newInstance();
								} catch (Exception e) {
									e.printStackTrace();
								}
							
							}
						
							return value;
						}
							return value;
							
					}}, SerializerFeature.PrettyFormat));
				} catch (Exception e) {
					logger.error(e.getMessage());
				} 
				
				serviceParams.add(serviceParam);
			}
			
			serviceMethod.setServiceParams(serviceParams);
			
			serviceMethods.add(serviceMethod);
			
		}
		
		return serviceMethods;
		
	}

	public static List<Class> extractClassListByFilter(List<Class> clazzList, ClassFilter filter) {

		List<Class> newClassList = new ArrayList<Class>();

		for (Class clazz : clazzList) {

			if (filter.filter(clazz)) {
				newClassList.add(clazz);
				// System.out.println(clazz.getName());
			}
		}

		return newClassList;
	}

	public static List<Class> loadJarFile(String jarFilePath) throws MalformedURLException {
		// "file:D:/jarload/test.jar"
		URL url = new URL("file:" + jarFilePath);
		List<Class> classList = new ArrayList<Class>();
		List<String> classNameList = getClassListByJarFile(jarFilePath, true);
		URLClassLoader myClassLoader = new URLClassLoader(new URL[] { url },
				Thread.currentThread().getContextClassLoader());
		for (String className : classNameList) {

			Class<?> clazz;
			try {
				clazz = myClassLoader.loadClass(className);
				classList.add(clazz);
				// System.out.println(clazz.getName());
			} catch (Throwable e) {
				System.out.println(className + "加载失败,失败原因：" + e.getMessage());
			}

		}

		return classList;
	}
	
	public static List<Class> loadJarFileNew(String jarFilePath) throws Exception {
		// "file:D:/jarload/test.jar"
		URL url = new URL("file:" + jarFilePath);
		List<Class> classList = new ArrayList<Class>();
		List<String> classNameList = getClassListByJarFile(jarFilePath, true);
		
		ClassPathHacker.addURL(url);
		
		for (String className : classNameList) {

			Class<?> clazz;
			try {
				clazz = Class.forName(className);
				classList.add(clazz);
				// System.out.println(clazz.getName());
			} catch (Throwable e) {
				System.out.println(className + "加载失败,失败原因：" + e.getMessage());
			}

		}

		return classList;
	}

	public static List<Class> loadJarFiles(String[] jarFilePathArr) throws MalformedURLException {
		// "file:D:/jarload/test.jar"

		List<Class> classList = new ArrayList<Class>();

		List<String> classNameList = new ArrayList<String>();

		URL[] urls = new URL[jarFilePathArr.length];

		for (int i = 0; i < jarFilePathArr.length; i++) {

			urls[i] = new URL("file:" + jarFilePathArr[i]);

			classNameList.addAll(getClassListByJarFile(jarFilePathArr[i], true));

		}

		if (myClassLoader != null) {
			try {
				myClassLoader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		myClassLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());

		// System.out.println("myClassLoader==>"+myClassLoader);

		Thread.currentThread().setContextClassLoader(myClassLoader);

		for (String className : classNameList) {

			Class<?> clazz;
			try {
				clazz = myClassLoader.loadClass(className);
				classList.add(clazz);
				// System.out.println(clazz.getName());
			} catch (Throwable e) {
				System.out.println(className + "加载失败,失败原因：" + e.getMessage());
			}

		}

		return classList;
	}

	public static List<Class> loadJarFilesNew(String[] jarFilePathArr) throws Exception {
		// "file:D:/jarload/test.jar"

		List<Class> classList = new ArrayList<Class>();

		List<String> classNameList = new ArrayList<String>();

		URL[] urls = new URL[jarFilePathArr.length];

		for (int i = 0; i < jarFilePathArr.length; i++) {

			urls[i] = new URL("file:" + jarFilePathArr[i]);

			ClassPathHacker.addURL(urls[i]);

			classNameList.addAll(getClassListByJarFile(jarFilePathArr[i], true));

		}

		for (String className : classNameList) {

			Class<?> clazz;
			try {
				clazz = Class.forName(className);
				classList.add(clazz);
				// System.out.println(clazz.getName());
			} catch (Throwable e) {
				System.out.println(className + "加载失败,失败原因：" + e.getMessage());
			}

		}

		return classList;
	}

	public static List<Class> getClassListForJarFile(String jarFilePath, ClassFilter filter) throws Exception {
		// "file:D:/jarload/test.jar"
		URL url = new URL("file:" + jarFilePath);
		List<Class> classList = new ArrayList<Class>();
		List<String> classNameList = getClassListByJarFile(jarFilePath, true);
		URLClassLoader myClassLoader = new URLClassLoader(new URL[] { url },
				Thread.currentThread().getContextClassLoader());

		for (String className : classNameList) {

			Class<?> clazz;
			try {
				clazz = myClassLoader.loadClass(className);
				if (filter.filter(clazz)) {
					classList.add(clazz);
					// System.out.println(clazz.getName());
				}
			} catch (Throwable e) {
				System.out.println(className + "加载失败,失败原因：" + e.getMessage());
			}

		}

		return classList;
	}

	/**
	 * 获取某包下（包括该包的所有子包）所有类
	 * 
	 * @param packageName
	 *            包名
	 * @return 类的完整名称
	 */
	public static List<String> getClassName(String packageName) {
		return getClassName(packageName, true);
	}

	/**
	 * 获取某包下所有类
	 * 
	 * @param packageName
	 *            包名
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	public static List<String> getClassName(String packageName, boolean childPackage) {
		List<String> fileNames = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String packagePath = packageName.replace(".", "/");
		URL url = loader.getResource(packagePath);
		if (url != null) {
			String type = url.getProtocol();
			if (type.equals("file")) {
				fileNames = getClassNameByFile(url.getPath(), null, childPackage);
			} else if (type.equals("jar")) {
				fileNames = getClassNameByJar(url.getPath(), childPackage);
			}
		} else {
			fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage);
		}
		return fileNames;
	}

	/**
	 * 从项目文件获取某包下所有类
	 * 
	 * @param filePath
	 *            文件路径
	 * @param className
	 *            类名集合
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	private static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				if (childPackage) {
					myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage));
				}
			} else {
				String childFilePath = childFile.getPath();
				if (childFilePath.endsWith(".class")) {
					childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9,
							childFilePath.lastIndexOf("."));
					childFilePath = childFilePath.replace("\\", ".");
					myClassName.add(childFilePath);
				}
			}
		}

		return myClassName;
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
	private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		String[] jarInfo = jarPath.split("!");
		String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
		String packagePath = jarInfo[1].substring(1);
		try {
			JarFile jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(".class")) {
					if (childPackage) {
						if (entryName.startsWith(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							myClassName.add(entryName);
						}
					} else {
						int index = entryName.lastIndexOf("/");
						String myPackagePath;
						if (index != -1) {
							myPackagePath = entryName.substring(0, index);
						} else {
							myPackagePath = entryName;
						}
						if (myPackagePath.equals(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							myClassName.add(entryName);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myClassName;
	}

	/**
	 * 从所有jar中搜索该包，并获取该包下所有类
	 * 
	 * @param urls
	 *            URL集合
	 * @param packagePath
	 *            包路径
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		if (urls != null) {
			for (int i = 0; i < urls.length; i++) {
				URL url = urls[i];
				String urlPath = url.getPath();
				// 不必搜索classes文件夹
				if (urlPath.endsWith("classes/")) {
					continue;
				}
				String jarPath = urlPath + "!/" + packagePath;
				myClassName.addAll(getClassNameByJar(jarPath, childPackage));
			}
		}
		return myClassName;
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
		try {
			JarFile jarFile = new JarFile(jarFilePath);
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
			e.printStackTrace();
		}
		return classList;
	}

	public static URLClassLoader getMyClassLoader() {
		return myClassLoader;
	}

	public static void setMyClassLoader(URLClassLoader myClassLoader) {
		PackageUtil.myClassLoader = myClassLoader;
	}
	
	private static String chgPrimitiveType(String typeName){
		if("int".equals(typeName)){
			return Integer.class.getName();
		}else if("byte".equals(typeName)){
			return Byte.class.getName();
		}else if("short".equals(typeName)){
			return Short.class.getName();
		}else if("char".equals(typeName)){
			return Character.class.getName();
		}else if("long".equals(typeName)){
			return Long.class.getName();
		}else if("float".equals(typeName)){
			return Float.class.getName();
		}else if("double".equals(typeName)){
			return Double.class.getName();
		}else if("boolean".equals(typeName)){
			return Boolean.class.getName();
		}
		
		return typeName;
	}
}
