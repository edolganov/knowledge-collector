package ru.chapaj.util.lang;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyUtil
{
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(ClassLoader cl,InvocationHandler handler,Class<?>... interfaces)
	{
		return (T)Proxy.newProxyInstance(cl,interfaces,handler);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Object classLoaderObject,InvocationHandler handler,Class<?>... interfaces)
	{
		return (T)createProxy(classLoaderObject.getClass().getClassLoader(), handler, interfaces);
	}
}

