package com.forte.util.factory;

import com.forte.util.exception.MockException;
import com.forte.util.mapper.ArrayMapper;
import com.forte.util.mapper.MockArray;
import com.forte.util.mapper.MockValue;
import org.apache.commons.beanutils.ConvertUtils;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
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
            Map.Entry<Supplier<Object>, String> valueAndParam = getValue(f.getAnnotation(MockValue.class));
            if (valueAndParam == null) {
                valueAndParam = getValue(f.getAnnotation(MockArray.class));
            }
            return new AbstractMap.SimpleEntry<>(f.getName() + valueAndParam.getValue(), valueAndParam.getKey().get());
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
     * @return {@code Entry<Supplier<Object>, fieldParam>} , 返回一个entry，key为value中的值的获取函数，value为map映射中字段的区间参数值。
     */
    public static Map.Entry<Supplier<Object>, String> getValue(MockValue mockValueAnnotation) {
        if (mockValueAnnotation == null || mockValueAnnotation.value().length() <= 0) {
            return null;
        }
        final String mapValue = mockValueAnnotation.value();
        final Class<?> valueType = mockValueAnnotation.valueType();

        Supplier<Object> valueGetter = valueType.equals(String.class) ? () -> mapValue : () -> ConvertUtils.convert(mapValue, valueType);

        String param = mockValueAnnotation.param().trim();
        if(param.length() > 0 && !param.startsWith("|")){
            param = "|" + param;
        }
        return new AbstractMap.SimpleEntry<>(valueGetter, param);
    }

    /**
     * 通过注解MockValue类型来获取value值
     * @return {@code Entry<Supplier<Object>, fieldParam>} , 返回一个entry，key为value中的值的获取函数，value为map映射中字段的区间参数值。
     */
    public static Map.Entry<Supplier<Object>, String> getValue(MockArray mockArrayAnnotation) {
        if (mockArrayAnnotation == null) {
            return null;
        }
        try {
            ArrayMapper arrayMapper = mockArrayAnnotation.mapper().newInstance();
            final String[] mockArrayValue = mockArrayAnnotation.value();
            final Supplier<Object> valueGetter = () -> arrayMapper.map(mockArrayValue);
            String param = mockArrayAnnotation.param().trim();
            if(param.length() > 0 && !param.startsWith("|")){
                param = "|" + param;
            }
            return new AbstractMap.SimpleEntry<>(valueGetter, param);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MockException("无法实例化数组转化器 Cannot instantiate an array converter ：" + mockArrayAnnotation.mapper());
        }
    }


}
