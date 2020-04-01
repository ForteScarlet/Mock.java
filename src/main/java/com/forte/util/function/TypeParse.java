package com.forte.util.function;

import com.forte.util.parser.FieldParser;

/**
 * 用于{@link com.forte.util.parser.ParameterParser}中，来注册各种参数类型的解析器。
 * 参数类型一般代表的是Map<String, Object>中的这个Object
 * @author <a href="https://github.com/ForteScarlet"> ForteScarlet </a>
 */
@FunctionalInterface
public interface TypeParse {

    /**
     * 接收部分参数，得到一个解析结果
     * @param objectClass 封装类型
     * @param fieldName   字段名称
     * @param intervalStr 区间字符串
     * @param value       参数
     * @return 字段解析器
     */
    FieldParser parse(Class<?> objectClass, String fieldName, String intervalStr, Object value);

}
