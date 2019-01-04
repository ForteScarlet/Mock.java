package com.forte.util.parser;

import com.forte.util.fieldvaluegetter.FieldValueGetter;
import com.forte.util.invoker.Invoker;

/**
 * 引用数据类型字段解析器
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
class ObjectParser extends BaseFieldParser {


    /** 参数中的默认值 */
    private Object defaultValue;

    /**
     * 当字段类型既不是集合也不是数组的时候
     * @return
     */
    @Override
    public FieldValueGetter parserForNotListOrArrayFieldValueGetter() {
       //直接获取一个额默认值参数获取器 - 使用Lambda表达式创建
        return () -> defaultValue;
    }

    /**
     * 如果字段类型是集合类型
     * @return
     */
    @Override
    public FieldValueGetter parserForListFieldValueGetter() {
        //使用集合字段类型获取器
        return getListFieldValueGetter(new Invoker[]{() -> defaultValue} , getIntervalData());
    }

    /**
     * 如果字段类型是数组类型
     * @return
     */
    @Override
    public FieldValueGetter parserForArrayFieldValueGetter() {
        //使用集合字段类型获取器
        return getArrayFieldValueGetter(new Invoker[]{() -> defaultValue} , getIntervalData());
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
     *
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     */
    public ObjectParser(Class objectClass, String fieldName, String intervalStr, Object defaultValue) {
        super(objectClass, fieldName, intervalStr);
        //赋值
        this.defaultValue = defaultValue;

    }

}
