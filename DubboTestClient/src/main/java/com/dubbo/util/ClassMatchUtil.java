package com.dubbo.util;

import java.util.regex.Pattern;

public class ClassMatchUtil {
	
	/**
	 * 
	 * @param className
	 * @param rule
	 * @return
	 */
	public static boolean isMatch(String className,String rule){
		
		
		
		//对*的支持支持正则匹配
		String patternRule=rule.replaceAll("\\*", "[\\\\w\\\\W]*");
		if(Pattern.matches(patternRule, className)){
			return true;
		}
		
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
		boolean flag=isMatch("com.zb.Test", "*zb*");
		System.out.println("flag==> "+flag);
		
	}

}
