package com.forte.util.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用于注解映射, 使用在字段上
 * 映射值为字符串类型
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @since JDK1.8
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //字段
public @interface MockValue {

    /**
     * 映射值，如果为空则视为无效
     */
    String value();

    /**
     * 区间参数，如果有值，则代表了字段之前的区间参数。默认没有值
     * 例如当字段{@code age} 的注解参数为 {@code param = "10-20"} 的时候, 相当于字段值为{@code "age|10-20"}。参数中的那个竖线不需要写。写了也会被去除的。
     * @since  1.6.0
     */
    String param() default "";

    /**
     * 参数value的最终类型，在转化的时候会使用beanutils中的工具类{@link org.apache.commons.beanutils.ConvertUtils}进行类型转化, 默认为String类型。
     * @return
     */
    Class<?> valueType() default String.class;

}
