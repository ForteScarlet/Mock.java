package com.forte.util.invoker;

import com.forte.util.utils.MethodUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法执行者，是{@link com.forte.util.invoker.Invoker}的是实现类，代表了一个方法的执行
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class MethodInvoker implements Invoker {
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
     * 获取一个普通的{@link MethodInvoker}
     * @param obj    实例
     * @param args   参数列表
     * @param method 方法实例
     * @return {@link MethodInvoker}
     */
    public static MethodInvoker getInstance(Object obj, Object[] args, Method method){
        return new MethodInvoker(obj, args, method);
    }

    /**
     * 获取一个常量{@link MethodInvoker}
     * @param constValue 常量值
     * @return {@link MethodInvoker}
     */
    public static MethodInvoker getInstance(Object constValue){
        return new ConstValueMethodInvoker(constValue);
    }

    /**
     * 执行方法
     *
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Override
    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        // 普通的执行者
        return MethodUtil.invoke(obj, args, method);
    }

    /**
     * 构造
     */
    MethodInvoker(Object obj, Object[] args, Method method) {
        this.obj = obj;
        if(method != null){
            this.methodName = method.getName();
        }else{
            this.methodName = null;
        }
        this.args = args;
        this.method = method;
    }


    /**
     * 常量值方法执行者
     */
    static class ConstValueMethodInvoker extends MethodInvoker {
        /**
         * 如果是一个空执行者，将会将对象返回
         */
        private final Object constValue;

        ConstValueMethodInvoker(Object constValue){
            super(null, null, null);
            this.constValue = constValue;
        }

        /**
         * 返回常量值
         */
        @Override
        public Object invoke() {
            return constValue;
        }
    }
}
