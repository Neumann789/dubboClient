package com.dubbo.util;

import java.lang.reflect.Modifier;

import com.alibaba.fastjson.JSON;


public class Hello {
	
	public static void main(String[] args) throws Exception {
		 sayHello();
	}
	
	public static void sayHello() throws Exception{
		
/*		System.out.println("hellorrrr5555rrrrrrrrrrrrrrrrr");
		
		System.out.println(Modifier.isAbstract(B.class.getModifiers()));
		
		System.out.println(Modifier.isAbstract(C.class.getModifiers()));
		
		System.out.println(Modifier.isAbstract(String.class.getModifiers()));
		
		System.out.println(int.class.isPrimitive());
		
		System.out.println(B.class.isPrimitive());
		
		System.out.println(char.class.isPrimitive());*/
		
		String result=StringUtil.genJsonStrPrettyFormat(Children.class.newInstance());
		/*Children children=new Children();
		
		children.set
		 result=JSON.toJSONString(Children.class.newInstance());*/
		System.out.println(result);
	}
	
}

class Parent{
	
	private String name;
	
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	
}

class Children extends Parent{
	
	private String childrenName;
	
	private String childrenAge;

	public String getChildrenName() {
		return childrenName;
	}

	public void setChildrenName(String childrenName) {
		this.childrenName = childrenName;
	}

	public String getChildrenAge() {
		return childrenAge;
	}

	public void setChildrenAge(String childrenAge) {
		this.childrenAge = childrenAge;
	}
	
	
	
	
}




interface C{}