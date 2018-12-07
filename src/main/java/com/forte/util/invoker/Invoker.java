package com.forte.util.invoker;

/**
 * 执行者接口，定义了一个执行者的函数，执行者会通过invoke()方法获得结果
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
@FunctionalInterface
public interface Invoker {

    /**
     * 返回方法执行的结果
     * @return
     * @throws Exception
     */
    public Object invoke() throws Exception;
}
