package com.forte.util.parser;

import com.forte.util.fieldvaluegetter.FieldValueGetter;
import com.forte.util.invoker.Invoker;
import com.forte.util.utils.MethodUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * List类型字段解析器
 * 解析方式与过程大致与{@link ArraysParser}相同
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
class ListParser extends BaseFieldParser {


    /** 参数传入的数组 */
    private final List<?> defaultList;

    /**
     * 字段类型既不是list集合也不是数组的时候
     * 说明从默认集合中随机获取一个并返回
     * @return
     */
    @Override
    public FieldValueGetter parserForNotListOrArrayFieldValueGetter() {
        //创建一个集合元素获取执行者
        Invoker invoker = MethodUtil.createListElementInvoker(defaultList);
        return getObjectFieldValueGetter(invoker);
    }

    /**
     * 字段类型是list集合的时候
     * @return
     */
    @Override
    public FieldValueGetter parserForListFieldValueGetter() {
        //转化并返回结果
        return getListFieldValueGetter();
    }

    /**
     * 字段类型是数组的时候
     * @return
     */
    @Override
    public FieldValueGetter parserForArrayFieldValueGetter() {
        //转化并返回结果
        return getArrayFieldValueGetter();
    }

    /**
     * 获取数组字段值获取器
     * @return
     */
    private FieldValueGetter getArrayFieldValueGetter(){
        //获取随机元素值执行者
        Invoker invoker = MethodUtil.createListElementInvoker(defaultList);
        //因为区间不可能为null，直接转化并返回
        return Optional.of(getIntervalData()).map(i -> {
            //如果有区间参数,根据区间参数获取字段值获取器
            return getArrayFieldValueGetter(new Invoker[]{invoker}, i);
        }).get();
    }

    /**
     * 获取集合字段值获取器
     * @return
     */
    private FieldValueGetter getListFieldValueGetter(){
        //获取随机元素值执行者
        Invoker invoker = MethodUtil.createListElementInvoker(defaultList);
        //因为区间不可能为null，直接转化并返回
        return Optional.of(getIntervalData()).map(i -> {
            //如果有区间参数,根据区间参数获取字段值获取器
            return getListFieldValueGetter(new Invoker[]{invoker}, i);
        }).get();
    }



    /**
     * 获取区间参数区间，如果没有区间参数则返回区间[1,1]
     * @return
     */
    private Integer[] getIntervalData(){
        //获取参数
        Integer min = intervalMin;
        Integer max = intervalMax;

        //判断区间参数
        if(min == null){
            //如果没左参数
            if(max == null){
                //如果右参数也没有，直接返回一个[1,1]的区间
                return new Integer[]{1,1};
            }else{
                //如果有右参数，参数同化
                min = max;
            }
        }else{
            //有左参数，判断右参数
            if(max == null){
                //没有右参数，同化
                max = min;
            }
            //否则都有，不变
        }
        //返回结果
        return new Integer[]{min ,max};
    }


    /**
     * 构造
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     */
    public ListParser(Class objectClass, String fieldName, String intervalStr, List<?> defaultList) {
        super(objectClass, fieldName, intervalStr);
        //参数集合，复制一份而并非使用原来的 ->浅拷贝
        this.defaultList = new ArrayList<>(defaultList);
    }

}
