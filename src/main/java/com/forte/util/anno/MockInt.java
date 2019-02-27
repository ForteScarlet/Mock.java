package com.forte.util.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 整数类型的参数
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date Created in 2019/1/4 18:45
 * @since JDK1.8
 **/
@Mock
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //字段、枚举的常量
public @interface MockInt {

    /**
     * 映射参数
     * @return
     */
    int value();


    /**
     * 区间参数，例如：
     * 5-6|2-3
     * 3-8|3
     * 4-6
     * @return
     */
    String interval();


}
