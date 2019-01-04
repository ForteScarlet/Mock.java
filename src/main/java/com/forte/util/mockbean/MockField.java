package com.forte.util.mockbean;

import com.forte.util.fieldvaluegetter.FieldValueGetter;
import com.forte.util.utils.FieldUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 假字段值的字段封装对象的抽象类
 * 假的字段有几种类型：
 * <ul>
 * <li>字符串</li>
 * <li>整数</li>
 * <li>浮点数</li>
 * <li>集合</li>
 * <li>数组</li>
 * <li>引用对象</li>
 * </ul>
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class MockField {

    /**
     * 字段值获取器
     */
    private final FieldValueGetter valueGetter;

    /**
     * 字段名称
     */
    private final String fieldName;


    /**
     * 为传入的对象的对应的参数赋值
     * 通过FieldUtils工具类使用setter方法赋值
     * @see FieldUtils
     *
     * @param
     */
    public void setValue(Object bean) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        FieldUtils.objectSetter(bean, fieldName, getValue());
    }

    /**
     * 获取字段的值
     *
     * @return 字段的值
     */
    public Object getValue() {
        return valueGetter.value();
    }

    /**
     * 获取字段的名称
     *
     * @return 字段名
     */
    public String getFieldName() {
        return this.fieldName;
    }


    /**
     * 构造
     */
    public MockField(String fieldName, FieldValueGetter fieldValueGetter) {
        //考虑是否要加上匹配的class对象
        //似乎不需要
        this.fieldName = fieldName;
        this.valueGetter = fieldValueGetter;
    }

    @Override
    public String toString() {
        return "MockField{StringName='"+fieldName+"'}";
    }
}
