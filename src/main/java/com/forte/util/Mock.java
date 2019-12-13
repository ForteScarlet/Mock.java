package com.forte.util;


import com.forte.util.exception.MockException;
import com.forte.util.factory.MockMapperFactory;
import com.forte.util.factory.MockObjectFactory;
import com.forte.util.loader.DefaultMockMethodLoader;
import com.forte.util.loader.MethodLoader;
import com.forte.util.mockbean.*;
import com.forte.util.parser.ParameterParser;
import com.forte.util.utils.MockUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <h4>
 * javaBean假数据生成工具
 * </h4>
 * <p>
 * 使用静态方法：{@link #set(Class, Map)} 来添加一个类的假数据类型映射<br>
 * 语法基本与Mock.js中的类似，字符串参数中可以使用<code>@+方法名</code>的方式指定随机方法(随机方法详见{@link MockUtil},此类也可以直接使用)
 * </p>
 * <p>
 * <ul>
 *     <li>使用的时候，请务必保证填充假数据的字段有他的getter方法</li>
 *     <li>使用多层级赋值的时候，请注意保证多层级中涉及的深层对象有无参构造方法</li>
 * </ul>
 * </p>
 * <p>
 *  为类中的引用类型对象赋值的时候，有两种方式：
 *  <ul>
 *      <li>
 *          <code>
 *              map.set("user" , new HashMap<String , Object>)
 *          </code>
 *         ->  即为字段再配置一个map映射集合
 *      </li>
 *      <li>
 *          <code>
 *              map.set("user.name" , "@cname")
 *          </code>
 *         ->  使用"."分割，即使用多层级对象赋值，此方式需要保证引用类型的对象有无参构造，且字段有getter方法
 *      </li>
 *  </ul>
 * </p>
 *
 * @author ForteScarlet
 * @version 0.5-beta
 */
public class Mock {

    /* 静态代码块加载资源 */
    static {
        //创建线程安全的map集合,保存全部映射记录
        MOCK_OBJECT = new ConcurrentHashMap<>(32);
        MOCK_MAP = new ConcurrentHashMap<>(32);

        //创建map，这里的map理论上不需要线程同步
        Map<String, Method> mockUtilMethods;

        //加载这些方法，防止每次都使用反射去调用方法。
        //直接调用的话无法掌控参数，所以必须使用反射的形式进行调用
        Class<MockUtil> mockUtilClass = MockUtil.class;
        //只获取公共方法
        Method[] methods = mockUtilClass.getMethods();
        /*
            过滤Object中的方法、
            将MockUtil中的全部方法名格式化 格式：方法名(参数类型class地址，参数类型class地址.....)、
            转化为<方法名：方法>的map集合
         */
        mockUtilMethods =
                Arrays.stream(methods)
                        //过滤掉Object中继承过来的方法
                        .filter(m -> Arrays.stream(Object.class.getMethods()).noneMatch(om -> om.equals(m)))
                        //格式化方法名，格式：方法名(参数类型class地址，参数类型class地址.....)
                        .flatMap(m -> {
                            Map<String, Method> methodMap = new HashMap<>();
                            //格式化方法名，并作为key
                            String key = m.getName() + "("
                                    + Arrays.stream(m.getParameterTypes())
                                    .map(Class::getName)
                                    .collect(Collectors.joining(",")) +
                                    ")";
                            methodMap.put(key, m);
                            return methodMap.entrySet().stream();
                        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        //保存MockUtil中的全部方法
        MOCK_METHOD = mockUtilMethods;
    }

    /**
     * 保存全部记录的class与其对应的假对象{@link MockBean}
     */
    private static final Map<Class, MockNormalObject> MOCK_OBJECT;

    /**
     * Map类型假对象
     * TODO 后期考虑合并MOCK_OBJECT 和 MOCK_MAP两个字段
     */
    private static final Map<String, MockMapObject> MOCK_MAP;


    /**
     * MockUtil中的全部方法
     */
    private static final Map<String, Method> MOCK_METHOD;



    /**
     * 添加一个数据映射
     * @param objClass  映射类型
     * @param map       映射对应值
     * @return          映射结果表
     */
    public static <T> MockBean<T> setResult(Class<T> objClass, Map<String, Object> map, boolean reset) {
        //如果不是重新设置且此映射已经存在，并且objClass对象存在，将会抛出异常
        if(!reset && MOCK_OBJECT.get(objClass) != null){
            throw new MockException("此映射已存在！");
        }

            MockBean<T> parser;

            //使用参数解析器进行解析
            parser = ParameterParser.parser(objClass, map);

            //如果类型不是Map类型，添加
            MOCK_OBJECT.put(objClass, new MockNormalObject<>(parser));

            //提醒系统的垃圾回收
            System.gc();

            return parser;
    }

    /**
     * 添加一个map类型的映射
     * @param resultName    映射名
     * @param map           映射值
     * @param reset         是否覆盖
     */
    public static MockMapBean setResult(String resultName, Map<String, Object> map, boolean reset){
        //如果不是重新设置且此映射已经存在，并且objClass对象存在，将会抛出异常
        if(!reset && MOCK_MAP.get(resultName) != null){
            throw new MockException("此映射已存在！");
        }

        MockMapBean parser;

        //使用参数解析器进行解析
        parser = ParameterParser.parser(map);
        MOCK_MAP.put(resultName, MockObjectFactory.createMapObj(parser));

        //提醒系统的垃圾回收
        System.gc();

        return parser;
    }




    /**
     * 添加数据记录，如果要添加的映射已存在，则会抛出异常
     *
     * @param objClass 映射的class
     * @param map      <p>映射的规则对象</p>
     *                 <p>
     *                 <ul>
     *                  <li>key:对应的字段</li>
     *                  <li>
     *                      value:映射参数，可以是：
     *                      <ul>
     *                           <li>字符串</li>
     *                           <li>若干随机方法指令(指令详见{@link MockUtil})</li>
     *                           <li>整数(Integer)</li>
     *                           <li>浮点数(Double)</li>
     *                           <li>数组或集合类型</li>
     *                           <li>Map集合类型(可作为新的映射，也可直接作为参数)</li>
     *                           <li>任意引用数据类型</li>
     *                      </ul>
     *                  </li>
     *                 </ul>
     *                 </p>
     *                 <p>
     *                  如果映射的对象中有多层级对象，支持使用多层级字段映射，例如：<br>
     *                      <code>
     *                          map.put("friend.name" , "@cname");
     *                      </code>
     *                 </p>
     *
     */
    public static <T> void set(Class<T> objClass, Map<String, Object> map) {
        //设置并保存映射，不可覆盖
        setResult(objClass, map, false);
    }

    /**
     * 通过注解来获取映射
     */
    public static <T> void set(Class<T> objClass){
        //获取映射Map
        Map<String, Object> mapper = MockMapperFactory.getMapper(objClass);
        setResult(objClass, mapper, false);
    }

    /**
     * 通过注解来获取映射, 并提供额外的、难以用注解进行表达的映射参数
     */
    public static <T> void setWithOther(Class<T> objClass, Map<String, Object> other){
        //获取映射Map
        Map<String, Object> mapper = MockMapperFactory.getMapper(objClass, other);
        setResult(objClass, mapper, false);
    }


    /**
     * 添加数据记录，如果要添加的映射已存在，则会抛出异常
     * @param resultName
     * @param map
     */
    public static void set(String resultName, Map<String, Object> map) {
        //设置并保存映射，不可覆盖
        setResult(resultName, map, false);
    }


    /**
     * 添加数据记录，如果要添加的映射已存在，则会覆盖
     * @param objClass
     * @param map
     * @param <T>
     */
    public static <T> void reset(Class<T> objClass, Map<String, Object> map){
        //设置并保存映射
        setResult(objClass, map, true);
    }

    /**
     * 通过注解来获取映射
     */
    public static <T> void reset(Class<T> objClass){
        //获取映射Map
        Map<String, Object> mapper = MockMapperFactory.getMapper(objClass);
        setResult(objClass, mapper, true);
    }

    /**
     * 通过注解来获取映射, 并提供额外的、难以用注解进行表达的映射参数
     */
    public static <T> void resetWithOther(Class<T> objClass, Map<String, Object> other){
        //获取映射Map
        Map<String, Object> mapper = MockMapperFactory.getMapper(objClass, other);
        setResult(objClass, mapper, true);
    }


    /**
     * 添加数据记录，如果要添加的映射已存在，则会覆盖
     * @param resultName
     * @param map
     * @param <T>
     */
    public static <T> void reset(String resultName, Map<String, Object> map){
        //设置并保存映射
        setResult(resultName, map, true);
    }


    /**
     * 获取一个实例对象
     *
     * @param objClass
     * @param <T>
     * @return
     */
    public static <T> MockObject<T> get(Class<T> objClass) {
        return Optional.ofNullable(MOCK_OBJECT.get(objClass)).orElse(null);
    }


    /**
     * 获取一个实例对象
     *
     * @param resultName
     * @param <T>
     * @return
     */
    public static <T> MockObject<Map> get(String resultName) {
        return Optional.ofNullable(MOCK_MAP.get(resultName)).orElse(null);
    }


    /**
     * 获取方法加载器
     * @return
     */
    public static MethodLoader mockMethodLoader(){
        return new DefaultMockMethodLoader();
    }

    /**
     * 获取Mock方法集合
     * @return
     * 全部已被加载的映射方法
     */
    public static Map<String, Method> _getMockMethod() {
//        return MOCK_METHOD.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return MOCK_METHOD;
    }



}
