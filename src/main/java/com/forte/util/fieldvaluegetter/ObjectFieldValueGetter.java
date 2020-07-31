package com.forte.util.fieldvaluegetter;

import com.forte.util.exception.MockException;
import com.forte.util.invoker.Invoker;
import com.forte.util.utils.MethodUtil;

import javax.script.ScriptException;

/**
 * 字段类型为任意未知类型的时候
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class ObjectFieldValueGetter implements FieldValueGetter<Object> {

    /**
     * 方法执行者，期望值是只有一个，但是不能保证，假如有多个参数的话，尝试使用加法运算进行相加
     */
    private final Invoker[] invokers;

    /**
     * 获取值
     *
     * @return
     */
    @Override
    public Object value() {
        try {
            //如果只有一个执行者
            if (invokers.length > 1) {
                //不止一个，拼接结果为类js代码并执行eval()
                //用于执行eval的拼接字符串
                StringBuilder evalString = new StringBuilder();
                //用于防止执行出现错误的直接返回用的字符串
                StringBuilder returnString = new StringBuilder();
                //遍历执行并拼接
                for (int i = 0; i < invokers.length; i++) {
                    if (i != 0) {
                        evalString.append("+");
                    }
                    //执行结果
                    Object invoke = invokers[i].invoke();
                    evalString.append(invoke);
                    returnString.append(invoke);
                }
                //遍历结束，执行加法运算并返回结果
                String forEval = evalString.toString();
                try {
                    return MethodUtil.eval(forEval);
                } catch (ScriptException e) {
                    //如果出现异常，则直接返回结果的拼接字符串
                    return returnString.toString();
                }
            }else {
                //直接返回执行结果
                return invokers[0].invoke();
            }
        } catch (Exception e) {
            //出现异常，抛出
            throw new MockException(e);
        }
    }


    /**
     * 构造方法
     *
     * @param invokers 方法执行者
     */
    public ObjectFieldValueGetter(Invoker[] invokers) {
        this.invokers = invokers;
    }

}
