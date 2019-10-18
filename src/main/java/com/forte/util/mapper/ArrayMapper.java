package com.forte.util.mapper;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * 映射器，可以指定将字符串数组内的值转化为其他类型
 * 需要存在一个无参构造
 * @author ForteScarlet <[email]ForteScarlet@163.com>
 * @since JDK1.8
 **/
public interface ArrayMapper<T> extends Function<String, T> {

    /**
     * 给你一个数组长度，返回一个数组实例的function，用于数组的实例化获取
     * @return 数组实例获取函数
     */
    IntFunction<T[]> getArrayParseFunction();

    /**
     * 进行转化, 将String类型的数组转化为某指定类型
     * @param array String array
     */
    default T[] map(String[] array){
        //真正被使用的对外接口
        return Arrays.stream(array).map(this).toArray(this.getArrayParseFunction());
    }


}
