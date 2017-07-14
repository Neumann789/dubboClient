package com.dubbo.util;

import java.net.URL;
import java.net.URLClassLoader;

public class JarClassLoader extends URLClassLoader{
	
    private static final URL[] EMPTY_URL_ARRAY = new URL[0];
	

	public JarClassLoader(URL[] urls) {
		super(urls);
	}
	
	public JarClassLoader() {
		super(EMPTY_URL_ARRAY);
	}
	
	
    
    public void addURL(URL url) {
        super.addURL(url);
    }

}
