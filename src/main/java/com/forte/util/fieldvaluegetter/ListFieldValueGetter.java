package com.forte.util.fieldvaluegetter;

import com.forte.util.invoker.Invoker;
import com.forte.util.utils.MethodUtil;
import com.forte.util.utils.RandomUtil;



import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * List集合的字段值获取器
 * 有较大的可能出现一些类型异常，请注意一定按照规范填写
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class ListFieldValueGetter implements FieldValueGetter<List> {


    /**
     * 方法执行者们
     * 期望中 只有一个执行者
     * 不排除多个执行者的情况，
     * 但是如果是多个执行者，List集合很大可能是String、Integer之类的基础数据类型
     */
    private final Invoker[] invokers;

    /**
     * 区间参数,重复最终输出,参数期望中长度为2，0索引为最小值，1为最大值
     * 默认值为[1,1],即为不重复
     */
    private final Integer[] integerInterval;

    /**
     * 多余字符
     * 集合字段的指令类参数中可能出现多余字符，当有多于字符的时候，list集合的类型有很大的概率是Sting、Integer之类的基础数据类型
     */
    private final String[] moreStrs;


    /**
     * 获取字段值
     *
     * @return
     */
    @Override
    public List value() {
        //创建一个Object类型的List集合，用于保存数据
        List list = new ArrayList();
        //获取执行次数
        Integer min = integerInterval[0];
        Integer max = integerInterval[1];
        int num = (max == null ? min : RandomUtil.getNumber$right(min, max));
        //判断执行者的数量
        if (invokers.length > 1) {

            //当执行者数量大于1，执行方法
            getValueWhenInvokersMoreThan1(num, list);

        } else if (invokers.length <= 0) {
            /*
             * 没有执行者的情况，创建并返回一个空的字符串集合
             * 一般情况下不会出现空执行者的情况,就算是没有可执行方法也会有空值执行者
             */
            return new ArrayList<String>();
        } else {

            //当执行者的数量不大于1的时候，执行方法
            getValueWhenInvokerIs1(num, list);
        }
        //返回结果
        return list;
    }

    /**
     * 当执行者的数量超过1的时候
     *
     * @param num
     * @param list
     */
    private void getValueWhenInvokersMoreThan1(int num, List list) {
        //执行者数量大于1的情况下，只能将全部执行结果的toString拼接，并尝试使用eval进行执行
        //如果eval可以执行，则保存eval中得到的结果，如果无法执行则返回拼接字符串
        StringBuilder sb = new StringBuilder();
        //开始遍历并执行
        for (int i = 0; i < num; i++) {
            //执行全部执行者
            for (int j = 0; j < invokers.length; j++) {
                try {
                    //如果有多余字符，先拼接多余字符
                    if (moreStrs != null) {
                        sb.append(moreStrs[j]);
                    }
                    //拼接执行结果
                    sb.append(invokers[j].invoke());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //如果有多余字符且多余字符的数量比执行者多1
            //只要多余字符比执行者数量大，则说明多余字符的数量为执行者的数量+1
            if (moreStrs != null && moreStrs.length > invokers.length) {
                //拼接多余字符的最后值
                sb.append(moreStrs[moreStrs.length - 1]);
            }
            String invokeStr = sb.toString();
            try {
                //尝试使用eval进行执行
                Object eval = MethodUtil.eval(invokeStr);
                //如果能执行成功，保存这个执行结果到集合
                list.add(eval);
            } catch (ScriptException e) {
                //如果执行失败，保存执行前的字符串
                list.add(invokeStr);
            }
        }
    }


    /**
     * 当执行者数量不大于1的时候
     *
     * @param num
     * @param list
     */
    private void getValueWhenInvokerIs1(int num, List list) {

        //执行者数量不大于1，即只有一个
        Invoker invoker = invokers[0];
        //尽管只有一个方法执行者，但是仍然可能存在多余字符
        //所以分两种情况
        //在有多余字符的情况下，处理方式类似于上面的多执行者
        if (moreStrs != null) {
            //如果存在多余字符
            //准备拼接结果
            StringBuilder sb = new StringBuilder();
            //遍历num次数
            for (int i = 0; i < num; i++) {
                //先拼接多余字符，再拼接方法执行结果
                sb.append(moreStrs[i]);
                try {
                    //方法的执行结果
                    sb.append(invoker.invoke());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //如果多余字符有结尾，拼接
                //由于只有一个执行者，所以如果多余字符数量大于1就说明有尾部多余
                if (moreStrs.length > 1) {
                    //这里的元素索引不出意外的话，必定是2
                    sb.append(moreStrs[moreStrs.length - 1]);
                }
                String invokeData = sb.toString();
                //尝试对结果进行eval
                try {
                    //如果执行成功，保存执行结果
                    Object eval = MethodUtil.eval(invokeData);
                    list.add(eval);
                } catch (ScriptException e) {
                    //如果执行失败，保存执行前的字符串
                    list.add(invokeData);
                }
            }

        } else {
            //没有多余字符
            //只有一个方法执行者、且没有多余字符的情况是最稳定的情况。
            //指令参数类型情况下，这种类型只有及低的可能会出现类型异常（只要你的list类型没有填写错误）
            //遍历num次数，执行执行者并将结果保存至list集合
            for (int i = 0; i < num; i++) {
                Object invoke = null;
                try {
                    //执行结果
                    invoke = invoker.invoke();
                    list.add(invoke);
                } catch (Exception e) {
                    //如果执行出现错误，保存一个空值 null
                    list.add(null);
                }
            }
        }
    }


    /**
     * 构造方法
     *
     * @param invokers        方法执行者
     * @param integerInterval 区间参数
     * @param moreStrs        多余字符
     */
    public ListFieldValueGetter(Invoker[] invokers,  Integer[] integerInterval,  String[] moreStrs) {
        this.invokers = invokers;
        //如果多余字符长度为0，则赋值为null
        this.moreStrs = moreStrs.length == 0 ? null : moreStrs;
        //如果为true，则使用默认的数组
        boolean isNull = integerInterval == null || integerInterval.length > 2 || integerInterval[0] == null || integerInterval[1] == null;
        if (isNull) {
            this.integerInterval = new Integer[]{1, 1};
        } else {
            this.integerInterval = integerInterval;
        }
    }

    /**
     * 构造方法，区间参数默认为[1,1]
     *
     * @param invokers 方法执行者
     * @param moreStrs 多余字符
     */
    public ListFieldValueGetter(Invoker[] invokers,  String[] moreStrs) {
        this.invokers = invokers;
        this.integerInterval = new Integer[]{1, 1};
        //如果多余字符长度为0，则赋值为null
        this.moreStrs = moreStrs.length == 0 ? null : moreStrs;
    }


    /**
     * 构造方法，没有多余字符
     *
     * @param invokers        方法执行者
     * @param integerInterval 区间参数
     */
    public ListFieldValueGetter(Invoker[] invokers,  Integer[] integerInterval) {
        this.invokers = invokers;
        //判断：数组为null || 长度大于2 || 左参数为null || 左右参数都为null
        //如果为true，则使用默认的数组
        boolean isNull = integerInterval == null || integerInterval.length > 2 || integerInterval[0] == null || integerInterval[1] == null;
        if (isNull) {
            this.integerInterval = new Integer[]{1, 1};
        } else {
            this.integerInterval = integerInterval;
        }
        //多余字符赋值为null
        this.moreStrs = null;
    }

    /**
     * 构造方法，没有多余字符，区间参数默认为[1,1]
     *
     * @param invokers 方法执行者
     */
    public ListFieldValueGetter(Invoker[] invokers) {
        this.invokers = invokers;
        this.integerInterval = new Integer[]{1, 1};
        //多余字符赋值为null
        this.moreStrs = null;
    }


}
