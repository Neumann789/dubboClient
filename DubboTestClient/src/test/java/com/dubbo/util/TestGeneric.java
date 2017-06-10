package com.dubbo.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TestGeneric {
	
	public static void main(String[] args) throws Throwable {
		
		Class clazz=Class.forName("com.zb.payment.fmd.facade.service.FmdQueryService");
		
		Method[] methods=clazz.getMethods();
		
		for(Method method:methods){
			
			
			for(Type paramType: method.getGenericParameterTypes()){
				
				/*if(paramClazz.gett instanceof ParameterizedType){
					
				}*/
				
					Type[] typesto = ((ParameterizedType) paramType).getActualTypeArguments();
					for(Type t:typesto){
						try {
							Field f=t.getClass().getDeclaredField("name");
							f.setAccessible(true);
							System.out.println(f.get(t));
							/*for(Field f:fields){
								System.out.println(f.getName());
							}*/
						} catch (Exception e) {
						
					
						}
					}
					
			}
			
		}
		
	}

}


