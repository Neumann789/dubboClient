package com.dubbo.util;

import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

public class TestJSON {
	
	public static void main(String[] args) {
		String name="";
		System.out.println(JSON.toJSONString(name));
		
		A a=new A();
		//a.setAddress("www");
		//a.setName("fhb");
		
		String jsonStr=JSON.toJSONString(a, new ValueFilter() {
			
			public Object process(Object object, String name, Object value) {
				
				if(value==null){
					
				
				
				if(value instanceof String){
					value="";
				}else if(value instanceof Byte || 
						value instanceof Character ||
						value instanceof Short||
						value instanceof Integer||
						value instanceof Long){
					value=0;
					
				}else if(value instanceof Boolean){
					
					value = true;
					
				}else if(value instanceof Float){
					value = 0.0f;
				}else if(value instanceof Double){
					value = 0.0;
				}else{
					
					try {
						Method method=object.getClass().getMethod("get"+StringUtil.capitalize(name), null);
						
						value=method.getReturnType().newInstance();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
				}	
				
				}
			
				return value;
			}
		}, SerializerFeature.PrettyFormat);
		
		System.out.println(jsonStr);
	}

}

class A {
	
	private String name;
	
	private String address;
	
	private B b;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}
	
	
	
}

class B{
	
	private String b1;
	
	private String b2;
	
	private int b3;
	
	private byte b4;
	
	private short b5;
	
	private long b6;
	
	private double b7;
	
	private float b8;
	
	private char b9;
	
	private boolean b10;

	public String getB1() {
		return b1;
	}

	public void setB1(String b1) {
		this.b1 = b1;
	}

	public String getB2() {
		return b2;
	}

	public void setB2(String b2) {
		this.b2 = b2;
	}

	public int getB3() {
		return b3;
	}

	public void setB3(int b3) {
		this.b3 = b3;
	}

	public byte getB4() {
		return b4;
	}

	public void setB4(byte b4) {
		this.b4 = b4;
	}

	public short getB5() {
		return b5;
	}

	public void setB5(short b5) {
		this.b5 = b5;
	}

	public long getB6() {
		return b6;
	}

	public void setB6(long b6) {
		this.b6 = b6;
	}

	public double getB7() {
		return b7;
	}

	public float getB8() {
		return b8;
	}

	public void setB8(float b8) {
		this.b8 = b8;
	}

	public char getB9() {
		return b9;
	}

	public void setB9(char b9) {
		this.b9 = b9;
	}

	public boolean isB10() {
		return b10;
	}

	public void setB10(boolean b10) {
		this.b10 = b10;
	}

	public void setB7(double b7) {
		this.b7 = b7;
	}
	
	
	
	
}
