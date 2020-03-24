package com.forte.util.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 数组类型的注解，类型为整数类型，
 * 可以指定一个转化规则使得这个字符串数组可以转化为其他类型
 *
 * @author ForteScarlet <[email]ForteScarlet@163.com>
 * @since JDK1.8
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //字段
public @interface MockArray {

    /**
     * 数组参数
     */
    String[] value();

    /**
     * 类型转化器实现类，需要存在无参构造
     * 默认不变
     */
    Class<? extends ArrayMapper> mapper() default ArrayMapperType.ToString.class;


    /**
     * 区间参数，如果有值，则代表了字段之前的区间参数。默认没有值
     * 例如当字段{@code age} 的注解参数为 {@code param = "10-20"} 的时候, 相当于字段值为{@code "age|10-20"}。参数中的那个竖线不需要写。写了也会被去除的。
     * @since  1.6.0
     */
    String param() default "";
}
