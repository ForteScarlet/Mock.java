package com.forte.util;

import com.forte.util.mockbean.MockObject;
import com.forte.util.parser.ParameterParser;
import com.forte.util.utils.MockUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <h4>
 * javaBean假数据生成工具
 * </h4>
 * <p>
 * 使用静态方法：{@link #set(Class, Map)} 来添加一个类的假数据类型映射
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
 *      <li>map.set("user" , new HashMap<String , Object></>)
 *         ->  即为字段再配置一个map映射集合
 *      </li>
 *      <li>map.set("user.name" , "@cname")
 *         ->  使用"."分割，即使用多层级对象赋值，此方式需要保证引用类型的对象有无参构造，且字段有getter方法
 *      </li>
 *  </ul>
 * </p>
 *
 * @author ForteScarlet
 */
public class Mock {

    /* 静态代码块加载资源 */
    static {
        //创建线程安全的map集合,保存全部记录
        MOCK_OBJECT = new ConcurrentHashMap<>();

        //创建map，这里的map理论上不需要线程同步
        Map<String, Method> mockUtilMethods;
        //加载这些方法，防止每次都使用反射去调用方法。
        //直接调用的话无法掌控参数，所以必须使用反射的形式进行调用
        Class<MockUtil> mockUtilClass = MockUtil.class;
        //只获取公共方法
        Method[] methods = mockUtilClass.getMethods();
        /*
            使用lambda表达式与stream流操作：
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
     * 保存全部记录的class与其对应的假对象{@link MockObject}
     */
    private static final Map<Class, MockObject> MOCK_OBJECT;

    /**
     * MockUtil中的全部方法
     */
    private static final Map<String, Method> MOCK_METHOD;

    /**
     * 添加数据记录
     *
     * @param objClass 映射的class
     * @param map      映射的规则对象
     *                 key:对应的字段
     *                 可解析的对象：
     */
    public static <T> void set(Class<T> objClass, Map<String, Object> map) {

        //使用参数解析器进行解析
        MockObject<T> parser = ParameterParser.parser(objClass, map);


        //添加
        MOCK_OBJECT.put(objClass, parser);
    }

    /**
     * 获取一个实例对象
     *
     * @param objClass
     * @param <T>
     * @return
     */
    public static <T> T get(Class<T> objClass) {
        MockObject<T> mockObject = MOCK_OBJECT.get(objClass);
        //如果经过了解析，获取，如果没有，返回null
        if (mockObject == null) {
            return null;
        } else {
            return mockObject.getObject();
        }
    }


    /**
     * 获取方法集合，方法名为了不常见而不被外界使用
     * @return
     */
    public static Map<String, Method> _getMockMethod() {
        return MOCK_METHOD;
    }


}
