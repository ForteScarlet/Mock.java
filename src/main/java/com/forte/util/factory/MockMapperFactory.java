package com.forte.util.factory;

import com.forte.util.exception.MockException;
import com.forte.util.mapper.ArrayMapper;
import com.forte.util.mapper.MockArray;
import com.forte.util.mapper.MockValue;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 生成映射Map的工厂
 *
 * @author ForteScarlet <[email]ForteScarlet@163.com>
 * @since JDK1.8
 **/
public class MockMapperFactory {

    /**
     * 通过一个类的注解来生成映射，然后再整合额外的指定参数
     *
     * @param type  类型
     * @param other 额外参数，可以为null
     */
    public static Map<String, Object> getMapper(Class<?> type, Map<String, Object> other) {
        Map<String, Object> mapper = Arrays.stream(type.getDeclaredFields()).map(f -> {
            //由于目前只有两种注解，直接判断下就行了
            //优先使用MockValue的值
            Object value = getValue(f.getAnnotation(MockValue.class));
            if (value == null) {
                value = getValue(f.getAnnotation(MockArray.class));
            }
            return new AbstractMap.SimpleEntry<>(f.getName(), value);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if(other != null){
            mapper.putAll(other);
        }
        return mapper;
    }

    /**
     * 通过一个类的注解来生成映射
     *
     * @param type 类型
     */
    public static Map<String, Object> getMapper(Class<?> type) {
        return getMapper(type, null);
    }

    /**
     * 通过注解MockValue类型来获取value值
     * 如果为null或者值没有（保留空字符），则返回null
     */
    public static Object getValue(MockValue mockValueAnnotation) {
        if (mockValueAnnotation == null || mockValueAnnotation.value().length() <= 0) {
            return null;
        }
        return mockValueAnnotation.value();
    }

    /**
     * 通过注解MockValue类型来获取value值
     */
    public static Object getValue(MockArray mockArrayAnnotation) {
        if (mockArrayAnnotation == null) {
            return null;
        }
        try {
            ArrayMapper arrayMapper = mockArrayAnnotation.mapper().newInstance();
            return arrayMapper.map(mockArrayAnnotation.value());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MockException("无法实例化数组转化器：" + mockArrayAnnotation.mapper());
        }
    }

}
