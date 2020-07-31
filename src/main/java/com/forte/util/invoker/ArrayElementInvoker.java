package com.forte.util.invoker;

import com.forte.util.utils.RandomUtil;

/**
 *
 * 数组类型的{@link ElementInvoker}
 *
 * @author ForteScarlet <ForteScarlet@163.com>
 * @date 2020/8/1
 */
public class ArrayElementInvoker<T> extends ElementInvoker<T> {

    /** 集合参数 */
    private T[] arr;
    /**
     * 数组构造
     * @param arr
     */
    public ArrayElementInvoker(T[] arr){
        this.arr = arr;
    }

    @Override
    public T getRandomElement() {
        return RandomUtil.getRandomElement(arr);
    }
}
