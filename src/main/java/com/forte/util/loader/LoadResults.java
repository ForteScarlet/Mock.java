package com.forte.util.loader;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * 方法加载后的返回值接口
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date Created in 2018/12/26 17:40
 * @since JDK1.8
 **/
public interface LoadResults {

    /**
     * 将成功加载的结果返回
     * @return  成功结果
     */
    Set<Method> loadSuccessResults();

    /**
     * 将结果作为结果返回，分为成功和失败两种结果
     * @return
     */
    Map<Boolean, Set<Method>> loadResults();

    /**
     * 成功结果的数量
     * @return
     */
    int successNums();

    /**
     * 失败的结果数量
     * @return
     */
    int failNums();

    /**
     * 将加载错误的方法返回，并告知其失败原因
     * @return
     */
    Map<Method, Exception> whyFail();

}
