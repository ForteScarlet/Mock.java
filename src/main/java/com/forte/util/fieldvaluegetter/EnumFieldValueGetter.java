package com.forte.util.fieldvaluegetter;

/**
 *
 * 枚举类型的字段值获取器
 *
 * @deprecated 尚未实现完成
 *
 * @author ForteScarlet <ForteScarlet@163.com>
 * @date 2020/8/1
 */
@Deprecated
public class EnumFieldValueGetter<E extends Enum<E>> implements FieldValueGetter<Enum<E>> {

    @Override
    public Enum<E> value() {
        return null;
    }
}
