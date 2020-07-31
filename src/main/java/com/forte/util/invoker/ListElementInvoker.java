package com.forte.util.invoker;

import com.forte.util.utils.RandomUtil;

import java.util.List;

/**
 *
 * list类型的{@link ElementInvoker}
 *
 * @author ForteScarlet <ForteScarlet@163.com>
 * @date 2020/8/1
 */
public class ListElementInvoker<T> extends ElementInvoker<T> {
    /** 集合参数 */
    private List<T> list;



    /**
     * 集合构造
     * @param list
     */
    public ListElementInvoker(List<T> list){
        this.list = list;
    }

    @Override
    public T getRandomElement() {
        return RandomUtil.getRandomElement(list);
    }
}
