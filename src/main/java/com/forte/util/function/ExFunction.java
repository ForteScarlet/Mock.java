package com.forte.util.function;

/**
 * 三个参数的function
 * @author <a href="https://github.com/ForteScarlet"> ForteScarlet </a>
 */
@FunctionalInterface
public interface ExFunction<T, U1, U2, R>  {

    /**
     * 接收三个参数，返回一个结果
     */
    R apply(T t, U1 u1, U2 u2);
}
