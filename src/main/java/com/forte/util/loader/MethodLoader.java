package com.forte.util.loader;

import com.forte.util.Mock;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 假方法加载类<br>
 * 加载的类或方法需要满足以下要求：<br>
 * <ul>
 *     <li>加载的方法不可与{@link com.forte.util.utils.MockUtil}中出现的方法发生<B>方法名相同，参数数量也相同</B>的情况，如果发生此情况，将会抛出异常。</li>
 *     <li>方法必须为有返回值的(非void)</li>
 *     <li>方法如果存在参数，请使用引用数据类型，避免使用基本数据类型</li>
 * </ul>
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date Created in 2018/12/24 20:36
 * @version 1.1
 * @since JDK1.8
 **/
public interface MethodLoader {

    /* —————————————————————————— 预载单方法 —————————————————————————— */

    /**
     * 加载某类中指定方法名的方法。如果有重载方法将会全部判断
     * @param loadClz       指定类
     * @param methodName    方法名
     * @return  处理结果
     */
    MethodLoader append(Class loadClz , String methodName);

    /**
     * 加载指定方法
     * @param method    要加载的方法
     * @return  处理结果
     */
    MethodLoader append(Method method);

    /* —————————————————————————— 预载多方法 —————————————————————————— */

    /**
     * 直接加载方法
     * @param methods   要加载的方法列表
     * @return  处理结果
     */
    MethodLoader appends(Method... methods);


    /**
     * 加载class中的全部方法
     * @param loadClz   加载方法的类
     * @return  处理结果
     */
    MethodLoader appendAll(Class loadClz);

    /**
     * 根据匹配规则对类中的方法名进行过滤
     * @param loadClz   加载方法的类
     * @param predicate 匹配规则
     * @return  处理结果
     */
    MethodLoader appendForNameFilter(Class loadClz , Predicate<String> predicate);

    /**
     * 根据匹配规则对类中的方法进行过滤
     * @param loadClz   加载方法的类
     * @param predicate 匹配规则
     * @return  处理结果
     */
    MethodLoader appendForMethodFilter(Class loadClz , Predicate<Method> predicate);

    /**
     * 根据方法名列表加载class中的指定方法
     * @param loadClz   加载方法的类
     * @param names     方法名列表
     * @return  处理结果
     */
    MethodLoader appendByNames(Class loadClz , String[] names);

    /**
     * 根据方法名列表加载class中的指定方法
     * @param loadClz   加载方法的类
     * @param names     方法名列表
     * @return  处理结果
     */
    MethodLoader appendByNames(Class loadClz , List<String> names);

    /**
     * 根据正则对方法名匹配并加载class中符合条件的方法
     * @param loadClz   加载方法的类
     * @param regex     正则表达式
     * @return  处理结果
     */
    MethodLoader appendByRegex(Class loadClz , String regex);

    /* —————————————————————————— 数据处理 —————————————————————————— */

    /**
     * 对方法进行过滤
     * @param predicate 过滤规则
     * @return 处理结果
     */
    MethodLoader filter(Predicate<Method> predicate);


    /**
     * 判断此方法是否可行
     * @param method
     * @return
     */
    default boolean can(Method method){
        //如果此方法没有返回值则直接返回false
        if(method.getReturnType().equals(void.class)){
            return false;
        }
        //获取已经加载的方法
        Map<String, Method> methodMap = Mock._getMockMethod();
        String keyName = method.getName() + "(" + Arrays.stream(method.getParameters()).map(p -> p.getType().getTypeName()).collect(Collectors.joining(",")) + ")";
        return methodMap.entrySet().stream().noneMatch(
                e -> keyName.equals(e.getKey())
                && (method.getParameters().length == e.getValue().getParameters().length)
        );
    }



    /* —————————————————————————— 加载/终结方法 —————————————————————————— */


    /**
     * 将预载内容加载至方法集
     * @return  加载成功数量
     */
    LoadResults load();



    /* —————————————————————————— 加载/终结方法-非链式 —————————————————————————— */

    /* —————————————————————————— 预载单方法 —————————————————————————— */

    /**
     * 加载某类中指定方法名的方法。如果有重载方法将会全部判断
     * @param loadClz       指定类
     * @param methodName    方法名
     * @return  处理结果
     */
    LoadResults add(Class loadClz , String methodName);

    /**
     * 加载指定方法
     * @param method    要加载的方法
     * @return  处理结果
     */
    LoadResults add(Method method);

    /* —————————————————————————— 预载多方法 —————————————————————————— */

    /**
     * 直接加载方法
     * @param methods   要加载的方法列表
     * @return  处理结果
     */
    LoadResults adds(Method... methods);


    /**
     * 加载class中的全部方法
     * @param loadClz   加载方法的类
     * @return  处理结果
     */
    LoadResults addAll(Class loadClz);

    /**
     * 根据匹配规则对类中的方法名进行过滤
     * @param loadClz   加载方法的类
     * @param predicate 匹配规则
     * @return  处理结果
     */
    LoadResults addForNameFilter(Class loadClz , Predicate<String> predicate);

    /**
     * 根据匹配规则对类中的方法进行过滤
     * @param loadClz   加载方法的类
     * @param predicate 匹配规则
     * @return  处理结果
     */
    LoadResults addForMethodFilter(Class loadClz , Predicate<Method> predicate);

    /**
     * 根据方法名列表加载class中的指定方法
     * @param loadClz   加载方法的类
     * @param names     方法名列表
     * @return  处理结果
     */
    LoadResults addByNames(Class loadClz , String[] names);

    /**
     * 根据方法名列表加载class中的指定方法
     * @param loadClz   加载方法的类
     * @param names     方法名列表
     * @return  处理结果
     */
    LoadResults addByNames(Class loadClz , List<String> names);

    /**
     * 根据正则对方法名匹配并加载class中符合条件的方法
     * @param loadClz   加载方法的类
     * @param regex     正则表达式
     * @return  处理结果
     */
    LoadResults addByRegex(Class loadClz , String regex);

    /* —————————————————————————— 一些getter方法等api —————————————————————————— */

    /**
     * 等待加载的方法集
     * @return
     */
    Set<Method> waiting();

    /**
     * 等待加载的方法集的数量
     * @return
     */
    int waitingNum();

    /**
     * 判断预载内容是否为空
     * @return  判断结果
     */
    boolean isEmpty();





}
