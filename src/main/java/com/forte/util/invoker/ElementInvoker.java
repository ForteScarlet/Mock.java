package com.forte.util.invoker;

import com.forte.util.utils.RandomUtil;
import com.sun.istack.internal.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * 随机元素值执行者
 * 两个字段，一个有值，一个为null
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2018/12/7 20:37
 */
public class ElementInvoker implements Invoker {

    /** 集合参数 */
    private Object[] arr;

    /** 数组参数 */
    private List list;

    /**
     * 执行者，获取随机元素
     * @return
     * @throws Exception
     */
    @Override
    public Object invoke() throws Exception {
        //返回数组或者集合的随机值
        if(arr != null){
            //如果有数组，使用数组
            return RandomUtil.getRandomElement(arr);
        }else{
            //如果没有数组，使用集合
            return RandomUtil.getRandomElement(list);
        }
    }

    /**
     * 数组构造
     * @param arr
     */
    public ElementInvoker(@NotNull Object[] arr){
        this.arr = arr;
    }

    /**
     * 集合构造
     * @param list
     */
    public ElementInvoker(@NotNull List list){
        this.list = list;
    }

}
