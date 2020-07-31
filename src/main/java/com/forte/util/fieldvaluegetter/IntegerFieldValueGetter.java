package com.forte.util.fieldvaluegetter;

import com.forte.util.exception.MockException;
import com.forte.util.invoker.Invoker;


/**
 * 整数类型字段值获取器
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class IntegerFieldValueGetter implements FieldValueGetter<Integer> {

    /**
     * 方法执行者用于获取整数类型的字段值
     * 执行者必然只有一个
     */
    private Invoker invoker;


    /**
     * 获取一个整数类型的字段值
     * @return
     */
    @Override
    public Integer value() {
        try {
            return (Integer) invoker.invoke();
        } catch (Exception e) {
            throw new MockException(e);
        }
    }


    /**
     * 构造方法，只需要一个方法执行者
     */
    public IntegerFieldValueGetter(Invoker invoker) {
        this.invoker = invoker;
    }


}
