package com.forte.util.invoker;

import java.util.List;

/**
 * 随机元素值执行者
 * 两个字段，一个有值，一个为null
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2018/12/7 20:37
 */
public abstract class ElementInvoker<T> implements Invoker {

    public abstract T getRandomElement();

    /**
     * 执行者，获取随机元素
     */
    @Override
    public Object invoke() {
        return getRandomElement();
    }

    public static <T> ElementInvoker<T> getInstance(T... array){
        return new ArrayElementInvoker<>(array);
    }

    public static <T> ElementInvoker<T> getInstance(List<T> list){
        return new ListElementInvoker<>(list);
    }



}
