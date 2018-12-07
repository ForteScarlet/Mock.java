package com.forte.util.parser;

import com.forte.util.fieldvaluegetter.FieldValueGetter;
import com.forte.util.invoker.Invoker;
import com.forte.util.utils.MethodUtil;
import com.sun.istack.internal.Nullable;

import java.util.*;

/**
 * double浮点型参数解析器，相对与指令解析，此解析器相对比较简单。
 * 只需要根据区间参数，获取相应的浮点数即可
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
class DoubleParserBase extends BaseFieldParser {

    /**
     * 参数默认值，唯一构造保证其必定有值，默认值为0
     */
    private Double defaultValue;


    /**
     * 当字段既不是集合也不是数组的时候，获取其字段值获取器
     * 字段不是集合也不是数组，且参数为Double，则说明区间参数代表了取值的随机范围
     * @return
     */
    @Override
    public FieldValueGetter parserForNotListOrArrayFieldValueGetter() {
        //判断小数部分的区间参数 - 将参数判断提为单独的方法
        //获取区间参数
        Integer[][] intervalsData = getIntervalsData();

        //整理结果
        //map 转化为doubleFieldValueGetter
        //如果为空则返回默认值
        return Optional.ofNullable(intervalsData).map(i -> (FieldValueGetter<Double>) getDoubleFieldValueGetter(i[0][0], i[0][1], i[1][0], i[1][1])).orElse(() -> defaultValue);
    }

    /**
     * 当字段是集合的时候，获取其字段值获取器
     * 如果字段为集合，参数为Double
     * 则区间参数变为List集合的长度区间，将无视小数部分的区间参数
     * list集合的值为默认值
     * @return
     */
    @Override
    public FieldValueGetter parserForListFieldValueGetter() {
        //获取整数区间参数
        Integer[] intIntervalsData = getIntIntervalsData();
        //将区间参数转化为list集合，如果不存在则返回空集合
        //并返回
        return Optional.ofNullable(intIntervalsData).map(i -> {
            //获取空值执行者
            Invoker nullMethodInvoker = MethodUtil.createNullMethodInvoker(defaultValue);
            //返回结果
            return getListFieldValueGetter(new Invoker[]{nullMethodInvoker}, i);
    }).orElse(ArrayList::new);
    }

    /**
     * 当字段是数组的时候，获取其字段值获取器
     * 如果字段为数组，参数为Double
     * 则区间参数变为数组的长度区间，将无视小数部分的区间参数
     * @return
     */
    @Override
    public FieldValueGetter parserForArrayFieldValueGetter() {
        // 将结果整合为数组并返回
        // 方式类似于List集合，仅返回值不同
        //获取整数区间参数
        Integer[] intIntervalsData = getIntIntervalsData();
        //将区间参数转化为数组，如果不存在则返回空集合
        //并返回
        return Optional.ofNullable(intIntervalsData).map(i -> {
            //获取空值执行者
            Invoker nullMethodInvoker = MethodUtil.createNullMethodInvoker(defaultValue);
            //返回结果
            return getArrayFieldValueGetter(new Invoker[]{nullMethodInvoker}, i);
        }).orElse(() -> new Double[0]);
    }



    /**
     * 仅获取整数部分的区间参数
     * @return
     */
    private Integer[] getIntIntervalsData(){
        //准备数组
        Integer[] intIntervals = new Integer[2];

        //初始化参数
        Integer intIntervalMin = intervalMin;
        Integer intIntervalMax = intervalMax;

        //判断整数位区间参数
        if (intIntervalMin == null) {
            //如果没有整数位左参数，判断是否存在右参数
            if (intIntervalMax == null) {
                // 如果右参数也为null，直接返回null
                return null;
            } else {
                // 有右参数，没有左参数，按照仅有左参数处理
                intIntervalMin = intIntervalMax;
            }
        }else{
            //有左参数，判断是否有右参数
            if (intIntervalMax == null) {
                // 如果右参数为null，赋值为左参数
                intIntervalMax = intIntervalMin;
            }
            // 两参数都有，不变
        }

        //为结果赋值
        intIntervals[0] = intIntervalMin;
        intIntervals[1] = intIntervalMax;

        //返回结果
        return intIntervals;
    }

    /**
     * 仅获取小数部分的区间参数
     * @return
     */
    private Integer[] getDoubleIntervalsData(){
        //准备结果
        Integer[] doubleIntervals = new Integer[2];

        //初始化参数
        Integer doubleIntervalMin = intervalDoubleMin;
        Integer doubleIntervalMax = intervalDoubleMax;

        //判断小数区间，判断方法与整数基本一致
        if (doubleIntervalMin == null) {
            //如果没有整数位左参数，判断是否存在右参数
            if (doubleIntervalMax == null) {
                // 如果右参数也为null，将两值设为0
                doubleIntervalMin = doubleIntervalMax = 0;
            } else {
                // 有右参数，没有左参数，按照仅有左参数处理
                doubleIntervalMin = doubleIntervalMax;
            }
        }else{
            //有左参数，判断是否有右参数
            if (doubleIntervalMax == null) {
                // 如果右参数为null，赋值为左参数
                doubleIntervalMax = doubleIntervalMin;
            }
            // 两参数都有，不变
        }


        //为结果赋值
        doubleIntervals[0] = doubleIntervalMin;
        doubleIntervals[1] = doubleIntervalMax;

        //返回结果
        return doubleIntervals;
    }

    /**
     * 获取转化好的区间参数
     * @return
     * 返回值为二维数组，
     * 索引0为整数部分的区间数组，[0]为左参数，[1]为右参数
     * 索引1为小数部分的区间参数，[0]为左参数，[1]为右参数
     * 假如返回值为null，则说明没有区间参数
     */
    private Integer[][] getIntervalsData(){
        Integer[] intIntervals = new Integer[2];
        Integer[] doubleIntervals = new Integer[2];
        Integer[][] intervals = new Integer[][]{intIntervals , doubleIntervals};

        //获取整数部分区间参数,可能为null
        @Nullable Integer[] intIntervalsData = getIntIntervalsData();
        //获取小数部分区间参数,此结果必定不为null
        Integer[] doubleIntervalsData = getDoubleIntervalsData();

        //返回结果
        //如果整数区间不为null，则转化为二维数组并返回，如果不存在则返回null
        return Optional.ofNullable(intIntervalsData).map(i -> new Integer[][]{intIntervalsData , doubleIntervalsData}).orElse(null);
    }


    /**
     * 构造
     *
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     * @param defaultValue 默认值，如果未null则默认为0
     */
    public DoubleParserBase(Class objectClass, String fieldName, String intervalStr, Double defaultValue) {
        super(objectClass, fieldName, intervalStr);
        //如果默认值为null，赋值为0
        this.defaultValue = defaultValue == null ? 0 : defaultValue;
    }

}
