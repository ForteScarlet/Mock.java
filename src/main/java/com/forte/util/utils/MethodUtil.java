package com.forte.util.utils;

import com.forte.util.exception.ParameterSizeException;
import com.forte.util.invoker.Invoker;
import com.forte.util.invoker.MethodInvoker;
import org.apache.commons.beanutils.ConvertUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * 方法执行工具
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class MethodUtil {


    /**
     * 执行一个方法，可以为基本的数据类型进行转化
     *
     * @param obj
     * @param args
     * @param method
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object invoke(Object obj, Object[] args, Method method) throws InvocationTargetException, IllegalAccessException {
        //获取参数的数据类型数组，准备转化数据类型
        Parameter[] parameters = method.getParameters();
        //如果传入参数与方法参数数量不符 ，
        //不知道是否能识别 String... args 这种参数
        if (args.length != parameters.length) {
            throw new ParameterSizeException();
        }
        //创建一个新的Object数组保存转化后的参数，如果使用原数组的话会抛异常：ArrayStoreException
        Object[] newArr = new Object[args.length];
        //遍历参数并转化
        for (int i = 0; i < parameters.length; i++) {
            //使用BeanUtils的数据类型器对参数的数据类型进行转化
            newArr[i] = ConvertUtils.convert(args[i], parameters[i].getType());
        }

        //返回方法的执行结果
        return method.invoke(obj, newArr);
    }

    /**
     * 执行一个方法，可以为基本的数据类型进行转化
     *
     * @param obj
     * @param args
     * @param methodName 方法名
     * @return
     * @throws NoSuchMethodException
     */
    public static Object invoke(Object obj, Object[] args, String methodName) throws NoSuchMethodException {
        //通过反射获取此方法
        Method[] methods = Arrays.stream(obj.getClass().getMethods()).filter(m -> m.getName().equals(methodName) && m.getParameters().length == args.length).toArray(Method[]::new);
        for (Method m : methods) {
            try {
                return invoke(obj, args, m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        throw new NoSuchMethodException();
    }


    /**
     * 将一个方法存至缓存
     */
    private void saveChcheMethod() {
        //TODO 完成方法的缓存，缓存方法的获取方式：根据类：方法名获取、字段获取

    }


    /**
     * js中的eval函数，应该是只能进行简单的计算
     * 利用js脚本完成
     *
     * @param str 需要进行eval执行的函数
     * @return 执行后的结果
     */
    public static Object eval(String str) throws ScriptException {
        //创建一个js脚本执行器
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine se = manager.getEngineByName("js");
        //脚本执行并返回结果
        Object eval = se.eval(str);
        return eval;
    }

    /**
     * 创建一个方法执行者
     *
     * @return
     */
    public static Invoker createMethodInvoker(Object obj, Object[] args, Method method) {
        return new MethodInvoker(obj, args, method);
    }

    /**
     * 创建一个方法为空的方法执行者，代表没有方法，将会输出指定的字符串
     *
     * @return
     */
    public static Invoker createNullMethodInvoker(Object nullValue) {
        return new MethodInvoker(nullValue);
    }


    /* ———————— 内部类 ———————— */

}

/**
 * TODO 完成此工具类的优化工作 - 实现此内部类
 * 内部类，用于记录曾经读取过的Method对象，用做缓存，优化此工具的执行效率
 */
class CacheMethod {

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 此方法存在于的类的class对象
     */
    private Class objectClass;

    /**
     * 此方法的参数列表
     */
    private Parameter[] parameters;


    CacheMethod() {

    }

}


