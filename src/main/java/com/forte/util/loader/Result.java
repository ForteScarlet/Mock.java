package com.forte.util.loader;

/**
 * 方法的返回结果
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date Created in 2018/12/26 17:48
 * @since JDK1.8
 **/
public interface Result<T> {


    /**
     * 获取结果
     * @return 结果
     */
    T getResult();


}
