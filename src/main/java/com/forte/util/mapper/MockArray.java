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


}
