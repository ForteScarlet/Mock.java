package com.forte.util.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mock接口代理注解标识，标记在接口的抽象方法上。抽象方法
 * @author <a href="https://github.com/ForteScarlet"> ForteScarlet </a>
 */
@Retention(RetentionPolicy.RUNTIME)    //注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.METHOD}) //接口、类、枚举、注解、方法
public @interface MockProxy {

    /**
     * <pre> 是否忽略此方法。如果为是，则接口的最终代理结果为返回一个null。
     * <pre> 如果是基础数据类型相关，数字类型，返回{@code 0}。
     * <pre> 如果是基础数据类型相关，char类型，返回{@code ' '}。
     * <pre> 如果是基础数据类型相关，boolean类型，返回{@code false}。
     */
    boolean ignore() default false;

    /**
     * 如果此参数存在值，则会优先尝试通过name获取MockObject对象
     */
    String name() default "";

    /**
     * <pre> 当接口返回值为数组或者集合的时候，此方法标记其返回值数量大小区间{@link [min, max], 即 max >= size >= min}。是数学上的闭合区间。
     * <pre> 如果此参数长度为0，则返回值为1。
     * <pre> 如果参数长度为1，则相当于不是随机长度。
     * <pre> 如果参数长度大于2，只取前两位。
     */
    int[] size() default {1,1};

    /**
     * <pre> 指定返回值类型，三种可能类型：list类型，array类型，Object其他任意类型。
     * <pre> 当类型为list与array类型的时候，需要通过{@link #genericType()}方法指定泛型的类型，获取mock类型的时候将会通过此方法得到的类型来获取。
     */
    MockProxyType proxyType() default MockProxyType.OBJECT;


    /**
     * 假如类型为List或者数组类型，此处代表泛型的实际类型。
     */
    Class<?> genericType() default Object.class;


}
