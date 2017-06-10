package com.dubbo.util;

public class ClassMatchUtil {
	
	/**
	 * 
	 * @param className
	 * @param rule
	 * @return
	 */
	public static boolean isMatch(String className,String rule){
		
		String[] classNameArr=className.split("\\.");
		String[] ruleArr=rule.split("\\.");
		
		if(classNameArr.length!=ruleArr.length){
			return false;
		}
		
		boolean isMatch=true;
		
		for(int i=0;i<classNameArr.length;i++){
			if(classNameArr[i].equals(ruleArr[i])){
				continue;
			}else{
				
				if("*".equals(ruleArr[i])){
					continue;
				}else{
					isMatch=false;
					break;
				}
				
			}
		}
		
		return isMatch;
	}
	
	public static void main(String[] args) {
		boolean flag=isMatch("com.zb.Test", "com.zb2.*");
		System.out.println("flag==> "+flag);
		
	}

}
