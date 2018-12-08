package com.forte.util.parser;

import com.forte.util.fieldvaluegetter.FieldValueGetter;
import com.forte.util.invoker.Invoker;
import com.forte.util.utils.MethodUtil;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Integer整数类型字段解析器
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
class IntegerParser extends BaseFieldParser {

    /** 默认值 */
    private Integer defaultValue;


    /**
     * 当字段类型既不是list集合也不是数组的时候，获取字段值获取器
     * @return
     */
    @Override
    public FieldValueGetter parserForNotListOrArrayFieldValueGetter() {
        //既不是list也不是数组，则结果为一个整数
        //获取区间参数并获取随机整数的方法执行者
        //判断区间参数
        if(intervalMin == null){
            //如果没有左参数，判断是否有右参数
            if(intervalMax == null){
                //如果右参数也为0，则直接返回默认值字段值获取器 -> 使用Lambda表达式
                return () -> defaultValue;
            }else{
                //如果有右参数，将有参数视为唯一参数，获取字段值获取器
                return getIntegerFieldValueGetter(intervalMax);
            }
        }else{
            //如果有左参数，判断有没有右参数
            if(intervalMax == null){
                //如果没有右参数，则将左参数视为唯一参数，获取字段值获取器
                return getIntegerFieldValueGetter(intervalMin);
            }else{
                //如果两参数都有，获取字段值获取器
                return getIntegerFieldValueGetter(intervalMin, intervalMax);
            }
        }
    }


    /**
     * 当字段值是List集合时，获取字段值获取器
     * @return
     */
    @Override
    public FieldValueGetter parserForListFieldValueGetter() {
        //字段值是List集合形式，则区间参数为list集合的长度，输出区间内数量的默认值
        //获取一个默认值方法执行者
        Invoker nullMethodInvoker = MethodUtil.createNullMethodInvoker(defaultValue);
        //获取区间参数数组
        Integer[] interValData = getInterValData();
        //如果区间参数不为null，获取List字段值获取器，否则获取一个空的集合字段值获取器
        return Optional.ofNullable(interValData).map(i -> getListFieldValueGetter(new Invoker[]{nullMethodInvoker}, i)).orElse(ArrayList::new);
    }


    /**
     * 当字段值是数组时，获取字段值获取器
     * @return
     */
    @Override
    public FieldValueGetter parserForArrayFieldValueGetter() {
        //字段值是数组形式，则区间参数为数组的长度，输出区间内数量的默认值
        //获取一个默认值方法执行者
        Invoker nullMethodInvoker = MethodUtil.createNullMethodInvoker(defaultValue);
        //获取区间参数数组
        Integer[] interValData = getInterValData();
        //如果区间参数不为null，获取数组字段值获取器，否则获取一个空的数组字段值获取器
        return Optional.ofNullable(interValData).map(i -> getArrayFieldValueGetter(new Invoker[]{nullMethodInvoker}, i)).orElse(() -> new Integer[0]);
    }


    /**
     * 获取区间参数数组，用于获取List或数组类型的字段值获取器
     * 长度为2，分别代表最小值，最大值
     * @return
     */
    private Integer[] getInterValData(){
        Integer min = intervalMin;
        Integer max = intervalMax;
        //判断区间参数
        if(min == null){
            //如果没有左参数，判断是否有右参数
            if(max == null){
                //如果右参数也为0，则直接返回null
                return null;
            }else{
                //如果有右参数，区间参数同化
                min = max;
            }
        }else{
            //如果有左参数，判断有没有右参数
            if(max == null){
                //如果没有右参数，区间参数同化
                max = min;
            }
                //如果两参数都有，参数不变
        }

        //返回区间参数数组
        return new Integer[]{min, max};
    }


    /**
     * 构造
     *
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     */
    public IntegerParser(Class objectClass, String fieldName, String intervalStr, Integer defaultValue) {
        super(objectClass, fieldName, intervalStr);
        //默认值赋值
        this.defaultValue = defaultValue;

    }
}
