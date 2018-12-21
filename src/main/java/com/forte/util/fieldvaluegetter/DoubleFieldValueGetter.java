package com.forte.util.fieldvaluegetter;

import com.forte.util.invoker.Invoker;
import com.forte.util.utils.MethodUtil;


/**
 * Double类型字段值获取器
 * 既然使用了此字段值获取器，则说明已经确定了字段的类型，则必定不会出现多个执行者。
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class DoubleFieldValueGetter implements FieldValueGetter<Double> {

    /**
     * 方法执行者，用于获取double值
     * 方法执行者必定为1个
     */
    private Invoker invoker;

    @Override
    public Double value() {
        //直接返回执行结果
        try {
            return (Double) invoker.invoke();
        } catch (Exception e) {
            //如果出现异常，返回空值
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 构造方法，只需要一个方法执行者
     */
    public DoubleFieldValueGetter(Invoker invoker) {
        this.invoker = invoker;
    }

}
