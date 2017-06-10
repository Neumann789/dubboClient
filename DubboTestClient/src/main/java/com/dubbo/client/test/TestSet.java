package com.dubbo.client.test;

import java.util.HashSet;
import java.util.Set;

public class TestSet {
	
	public static void main(String[] args) {
		
		Set<A> set=new HashSet<A>();
		
		A a1=new A("tom","111");
		
		set.add(a1);
		
	}

}

class A{
	
	private String name;
	
	private String value;
	
	public A(String name,String value) {
		this.name=name;
		this.value=value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
