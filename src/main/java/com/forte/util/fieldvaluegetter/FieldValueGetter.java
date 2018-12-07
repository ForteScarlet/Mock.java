package com.forte.util.fieldvaluegetter;

/**
 * 字段值获取器
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
@FunctionalInterface
public interface FieldValueGetter<T> {

    /**
     * 获取这个字段的参数
     *
     * @return
     */
    public T value();

}
