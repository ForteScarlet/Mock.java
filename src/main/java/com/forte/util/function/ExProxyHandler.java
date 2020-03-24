package com.forte.util.function;

/**
 * 带着异常处理的BiFunction，用于构建动态代理的参数
 */
@FunctionalInterface
public interface ExProxyHandler<T, U, R> {
    /**
     * 函数接口
     *
     * @param t 第一参数
     * @param u 第二参数
     * @return 返回值
     * @throws Throwable 任意异常
     */
    R apply(T t, U u) throws Throwable;
}