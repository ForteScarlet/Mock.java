package com.forte.util.fieldvaluegetter;


import com.forte.util.invoker.Invoker;
import com.forte.util.utils.MethodUtil;
import com.forte.util.utils.RandomUtil;

import java.util.Collections;

/**
 *
 * 字符串类型字段值的获取者
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class StringFieldValueGetter implements FieldValueGetter<String> {

    /**
     * 方法执行者，顺序与解析出来的方法和多余字符之间的顺序一致
     */
    private final Invoker[] invokers;

    /**
     * 多余字符，如果不为null，则长度必然是 方法执行者invokers 的数量的+1或者相同
     */
    private final String[] moreStr;

    /**
     * 区间参数,重复最终输出,参数期望中长度为2，0索引为最小值，1为最大值
     * 默认值为[1,1],即为不重复
     */
    private final Integer[] integerInterval;

    /**
     * 获取字段值
     * @return
     */
    @Override
    public String value() {
        StringBuilder sb = new StringBuilder();
        //同时遍历方法与多余字符,使用methods遍历
        int i = 0;
        for(;i<invokers.length;i++){
            //如果有多余字符，先存多余字符，后存执行结果
            try {
                if(moreStr != null){
                    sb.append(moreStr[i]);
                }
                sb.append(invokers[i].invoke());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //如果多余字符不为空
        //判断多余字符的数量：
        // 如果数量相等，说明在最后的方法后面没有多余参数，
        // 如果数量多1，则说明在最后的方法后面还有多余字符
        //如果尾部有多余字符，添加
        if(moreStr!= null && moreStr.length > invokers.length){
            sb.append(moreStr[i]);
        }

        //重复输出，次数为integerInterval的参数
        //如果没有右参数，重复次数则为左参数
        int times;
        if(integerInterval[1] == null){
            times = integerInterval[0];
        }else{
            int min = integerInterval[0];
            int max = integerInterval[1];
            times = RandomUtil.getNumber$right(min , max);
        }

        String end = sb.toString();
        //有些少数情况，end中拼接后的字符串是可以作为简单JS代码执行的，在此处重复字符串之前，尝试使用eval进行执行
        try {
            end = MethodUtil.eval(end).toString();
        } catch (Exception e) {}


        //重复次数并返回
        return String.join("", Collections.nCopies(times, end));
    }


    /**
     * 构造
     * @param invokers
     * @param moreStr
     */
    public StringFieldValueGetter(Invoker[] invokers, String[] moreStr, Integer[] integerInterval) {
        this.invokers = invokers;
        this.moreStr = moreStr;
        //判断：数组为null || 长度大于2 || 左参数为null || 左右参数都为null
        //如果为true，则使用默认的数组
        boolean isNull = integerInterval == null || integerInterval.length > 2 || integerInterval[0] == null || integerInterval[1] == null;
        if(isNull){
            this.integerInterval = new Integer[]{1,1};
        }else{
            this.integerInterval = integerInterval;
        }
    }

    /**
     * 构造，区间参数默认为[1-1]
     * @param invokers
     * @param moreStr
     */
    public StringFieldValueGetter(Invoker[] invokers, String[] moreStr) {
        this.invokers = invokers;
        this.moreStr = moreStr;
        //区间为默认值
        this.integerInterval = new Integer[]{1,1};
    }

}
