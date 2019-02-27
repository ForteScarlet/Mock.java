package com.forte.util.loader;

import com.forte.util.Mock;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 基础的假方法加载者
 * 加载的类或方法需要满足以下要求：<br>
 * <ul>
 *     <li>加载的方法不可与{@link com.forte.util.utils.MockUtil}中出现的方法发生<B>方法名相同，参数数量也相同</B>的情况，如果发生此情况，将会抛出异常。</li>
 *     <li>方法必须有返回值(非void)</li>
 * </ul>
 * <strong>※ 本类目前不保证线程安全</strong>
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date Created in 2018/12/26 17:33
 * @since JDK1.8
 **/
public class DefaultMockMethodLoader implements MethodLoader {

    /** 获取mock方法集，此时的方法集已经通过静态代码块初始化完毕 */
    private final static Map<String, Method> MOCK_METHOD = Mock._getMockMethod();

    /** 要加载的方法，方法不能重复，使用set */
    private Set<Method> waitingMethods = new HashSet<>(10);

    /**
     * 根据方法名加载一个方法，如果方法名对应了多个方法，则会全部进行判断，因此可能会有多个方法
     * @param loadClz       指定类
     * @param methodName    方法名
     * @return  返回自身-链式
     */
    @Override
    public MethodLoader append(Class loadClz, String methodName) {
        //获取全部方法
        Method[] declaredMethods = loadClz.getDeclaredMethods();
        //返回结果
        return appends(Arrays.stream(declaredMethods).filter(m -> m.getName().equals(methodName)).toArray(Method[]::new));
    }

    /**
     * 添加一个方法
     * @param method    要加载的方法
     * @return  返回自身-链式
     */
    @Override
    public MethodLoader append(Method method) {
        if(can(method)){
            this.waitingMethods.add(method);
        }
        return this;
    }

    /**
     * 加载多个方法
     * @param methods   要加载的方法列表
     * @return  返回自身-链式
     */
    @Override
    public MethodLoader appends(Method... methods) {
        //筛选，方法名匹配且参数类型
        Set<Method> q = Arrays.stream(methods).filter(this::can).collect(Collectors.toSet());
        //添加结果
        this.waitingMethods.addAll(q);
        return this;
    }

    /**
     * 加载类中的全部方法
     * @param loadClz   加载方法的类
     * @return  返回自身-链式
     */
    @Override
    public MethodLoader appendAll(Class loadClz) {
        return appends(loadClz.getDeclaredMethods());
    }

    /**
     * 根据方法名筛选方法筛选
     * @param loadClz   加载方法的类
     * @param predicate 匹配规则
     * @return  返回自身-链式
     */
    @Override
    public MethodLoader appendForNameFilter(Class loadClz, Predicate<String> predicate) {
        //先根据方法名筛选过滤
        Method[] q = Arrays.stream(loadClz.getDeclaredMethods()).filter(m -> predicate.test(m.getName())).toArray(Method[]::new);
        return appends(q);
    }

    /**
     * 根据方法筛选方法筛选
     * @param loadClz   加载方法的类
     * @param predicate 匹配规则
     * @return  返回自身-链式
     */
    @Override
    public MethodLoader appendForMethodFilter(Class loadClz, Predicate<Method> predicate) {
        //根据条件筛选
        Method[] q = Arrays.stream(loadClz.getDeclaredMethods()).filter(predicate).toArray(Method[]::new);
        return appends(q);
    }

    /**
     * 加载指定方法名的多个方法
     * @param loadClz   加载方法的类
     * @param names     方法名列表
     * @return  返回自身-链式
     */
    @Override
    public MethodLoader appendByNames(Class loadClz, String[] names) {
        //遍历每个名字并尝试加载
        for (String name : names) {
            append(loadClz , name);
        }
        return this;
    }

    /**
     * 加载指定方法名的多个方法
     * @param loadClz   加载方法的类
     * @param names     方法名列表
     * @return  返回自身-链式
     */
    @Override
    public MethodLoader appendByNames(Class loadClz, List<String> names) {
        return appendByNames(loadClz , names.toArray(new String[0]));
    }

    /**
     * 根据正则规则匹配方法中的方法名
     * @param loadClz   加载方法的类
     * @param regex     正则表达式
     * @return  返回自身-链式
     */
    @Override
    public MethodLoader appendByRegex(Class loadClz, String regex) {
        return appendForNameFilter(loadClz, s -> s.matches(regex));
    }

    /**
     * 过滤
     * @param predicate 过滤规则
     * @return  过滤后的结果
     */
    @Override
    public MethodLoader filter(Predicate<Method> predicate) {
        //过滤并重新赋值
        waitingMethods = waitingMethods.stream().filter(predicate).collect(Collectors.toSet());
        return this;
    }


    /* ———————————————————————— 非链式方法 —————————————————————— */

    /**
     * 加载某类中指定方法名的方法。如果有重载方法将会全部判断
     *
     * @param loadClz    指定类
     * @param methodName 方法名
     * @return 处理结果
     */
    @Override
    public LoadResults add(Class loadClz, String methodName) {
        //获取全部方法
        Method[] declaredMethods = loadClz.getDeclaredMethods();
        //返回结果
        return adds(declaredMethods);

    }

    /**
     * 加载指定方法
     *
     * @param method 要加载的方法
     * @return 处理结果
     */
    @Override
    public LoadResults add(Method method) {
        //返回结果
        return adds(method);
    }

    /**
     * 直接加载方法
     *
     * @param methods 要加载的方法列表
     * @return 处理结果
     */
    @Override
    public LoadResults adds(Method... methods) {
        //筛选，方法名匹配且参数类型
        Set<Method> q = Arrays.stream(methods).filter(this::can).collect(Collectors.toSet());

        //直接添加方法
        return load(q);
    }

    /**
     * 加载class中的全部方法
     *
     * @param loadClz 加载方法的类
     * @return 处理结果
     */
    @Override
    public LoadResults addAll(Class loadClz) {
        return adds(loadClz.getDeclaredMethods());
    }

    /**
     * 根据匹配规则对类中的方法名进行过滤
     *
     * @param loadClz   加载方法的类
     * @param predicate 匹配规则
     * @return 处理结果
     */
    @Override
    public LoadResults addForNameFilter(Class loadClz, Predicate<String> predicate) {
        Method[] q = Arrays.stream(loadClz.getDeclaredMethods()).filter(m -> predicate.test(m.getName())).toArray(Method[]::new);
        return adds(q);
    }

    /**
     * 根据匹配规则对类中的方法进行过滤
     *
     * @param loadClz   加载方法的类
     * @param predicate 匹配规则
     * @return 处理结果
     */
    @Override
    public LoadResults addForMethodFilter(Class loadClz, Predicate<Method> predicate) {
        //根据条件筛选
        Method[] q = Arrays.stream(loadClz.getDeclaredMethods()).filter(predicate).toArray(Method[]::new);
        return adds(q);
    }

    /**
     * 根据方法名列表加载class中的指定方法
     *
     * @param loadClz 加载方法的类
     * @param names   方法名列表
     * @return 处理结果
     */
    @Override
    public LoadResults addByNames(Class loadClz, String[] names) {
        //根据名称获取全部方法
        Method[] methods = loadClz.getMethods();
        //将方法名匹配的方法留下
        Method[] endMethod = Arrays.stream(methods).filter(m -> Arrays.stream(names).anyMatch(n -> n.equals(m.getName()))).toArray(Method[]::new);

        //返回结果
        return adds(endMethod);
    }

    /**
     * 根据方法名列表加载class中的指定方法
     *
     * @param loadClz 加载方法的类
     * @param names   方法名列表
     * @return 处理结果
     */
    @Override
    public LoadResults addByNames(Class loadClz, List<String> names) {
        return addByNames(loadClz, names.toArray(new String[0]));
    }

    /**
     * 根据正则对方法名匹配并加载class中符合条件的方法
     *
     * @param loadClz 加载方法的类
     * @param regex   正则表达式
     * @return 处理结果
     */
    @Override
    public LoadResults addByRegex(Class loadClz, String regex) {
        return addForNameFilter(loadClz , s -> s.matches(regex));
    }

    /* ———————————————————————— 终结方法 —————————————————————— */

    /**
     * 要加载的内容是否为空
     * @return  是否为空
     */
    @Override
    public boolean isEmpty() {
        return waitingMethods.isEmpty();
    }

    /**
     * 加载
     * @return  加载结果
     */
    @Override
    public LoadResults load() {
       return load(waitingMethods);
    }


    /**
     * 将传入的方法加载至随机方法集中并返回添加结果报告
     * @param methods
     * @return
     */
    private LoadResults load(Set<Method> methods){
        //遍历要加载的方法并添加，并获取结果返回值
        Set<BranchResult<Method>> collect = methods.stream().flatMap(m -> {
            Map<String, Method> methodMap = new HashMap<>(5);
            //格式化方法名，并作为key
            String key = m.getName() + "("
                    + Arrays.stream(m.getParameterTypes())
                    .map(Class::getName)
                    .collect(Collectors.joining(",")) +
                    ")";
            methodMap.put(key, m);

            //添加记录
            MOCK_METHOD.put(key, m);

            return methodMap.entrySet().stream();
        }).map(e -> {
            //遍历所有的Entry对象
            try {
                Method put = Mock._getMockMethod().put(e.getKey(), e.getValue());
                //如果添加成功则保存
                return MockMethodLoadResult.success(put);
            } catch (Exception err) {
                //如果添加失败则返回错误
                return MockMethodLoadResult.fail(e.getValue(), err);
            }
        }).collect(Collectors.toSet());

        //返回结果集封装
        return new MockMethodLoadReport(collect);
    }


    /* ———————————————————————— 部分getter的api —————————————————————— */


    /**
     * 等待加载的方法集
     *
     * @return
     */
    @Override
    public Set<Method> waiting() {
        return waitingMethods;
    }

    /**
     * 等待加载的方法集的数量
     *
     * @return
     */
    @Override
    public int waitingNum() {
        return waitingMethods.size();
    }


}
