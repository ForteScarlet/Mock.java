package com.forte.util.utils;

import com.forte.util.function.ExProxyHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

/**
 * 动态代理工具类，为一个接口类型创建动态代理对象
 * @author <a href="https://github.com/ForteScarlet"> ForteScarlet </a>
 */
public class ProxyUtils {

    /**
     * 为一个接口类型创建动态代理对象。
     * @param type             接口的类型
     * @param proxyHandler    动态代理的逻辑处理类
     * @param <T>
     * @return
     */
    public static <T> T proxy(Class<T> type, ExProxyHandler<Method, Object[], Object> proxyHandler){
        if (!Modifier.isInterface(type.getModifiers())) {
            throw new IllegalArgumentException("type ["+ type +"] is not a interface type.");
        }
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (p, m, o) -> proxyHandler.apply(m, o));
    }

    /**
     * 为一个接口类型创建动态代理对象。
     * @param type             接口的类型
     * @param proxyHandler    动态代理的逻辑处理类
     * @param <T>
     * @return
     */
    public static <T> T proxy(Class<T> type, InvocationHandler proxyHandler){
        if (!Modifier.isInterface(type.getModifiers())) {
            throw new IllegalArgumentException("type ["+ type +"] is not a interface type.");
        }
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, proxyHandler);
    }



}
