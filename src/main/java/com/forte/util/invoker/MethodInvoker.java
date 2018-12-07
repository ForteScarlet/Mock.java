package com.forte.util.invoker;

import com.forte.util.utils.MethodUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * 方法执行者，是{@link com.forte.util.invoker.Invoker}的是实现类，代表了一个方法的执行
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class MethodInvoker implements Invoker {

    /**
     * 如果是一个空执行者，将会将对象返回
     */
    private final Object nullValue;
    /**
     * 执行方法的对象
     */
    private final Object obj;
    /**
     * 方法的名字
     */
    private final String methodName;
    /**
     * 方法的参数
     */
    private final Object[] args;
    /**
     * 方法的主体 - MockUtil中的静态方法
     */
    private final Method method;

    /**
     * 执行方法
     *
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Override
    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        //三元运算，如果是个空执行者(空值不为空)，返回空值返回值，否则执行方法
        return nullValue != null ?
                nullValue :
                MethodUtil.invoke(null, args, method);
    }

    /**
     * 构造
     */
    public MethodInvoker(Object obj, Object[] args, Method method) {
        this.obj = obj;
        this.methodName = method.getName();
        this.args = args;
        this.method = method;
        //空值输出为空，说明这不是一个空执行者
        this.nullValue = null;
    }

    /**
     * 空执行者的构造
     */
    public MethodInvoker(Object nullValue) {
        this.nullValue = nullValue;
        //其他值设为null
        this.obj = null;
        this.methodName = null;
        this.args = null;
        this.method = null;
    }

}
