package com.forte.util.parser;

import com.forte.util.fieldvaluegetter.FieldValueGetter;
import com.forte.util.invoker.Invoker;
import com.forte.util.utils.MethodUtil;

import java.util.List;

/**
 * 指令字段解析器
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
class InstructionParser extends BaseFieldParser {

    /**
     * 指令的字符串
     */
    private final String instructionStr;


    /**
     * 当字段是数组的时候，进行解析
     * @return
     */
    @Override
    public FieldValueGetter parserForArrayFieldValueGetter() {
        //字段是数组类型时的解析方法
        //字段值获取器
        FieldValueGetter fieldValueGetter;

        //解析指令,查找指令中的@方法
        //先判断是否有匹配的方法
        boolean match = match(instructionStr);
        if (match) {
            //如果指令中有方法
            //解析出方法名
            String[] methods = getMethods(instructionStr);
            //解析方法并获取方法执行者
            List<Invoker> invoker = getMethodInvoker(methods);
            //获取多余字符
            String[] methodsSplit = getMethodsSplit(instructionStr);
            //获取list类型字段值获取器
            fieldValueGetter = getArrayFieldValueGetter(invoker, methodsSplit);

        } else {
            //如果没有@方法，则说明list集合是String集合或者是可以使用eval函数执行的js代码
            //则创建一个空执行者，将参数作为其输出值
            Invoker nullInvoker = MethodUtil.createNullMethodInvoker(instructionStr);

            //创建字段值获取器
            fieldValueGetter = getArrayFieldValueGetter(new Invoker[]{nullInvoker});
        }

        //返回字段值获取器
        return fieldValueGetter;
    }

    /**
     * 当字段是list集合的时候进行解析
     * @return
     */
    @Override
    public FieldValueGetter parserForListFieldValueGetter() {
        //字段是数组类型时的解析方法
        //字段值获取器
        FieldValueGetter fieldValueGetter;
        //解析指令,查找指令中的@方法
        //先判断是否有匹配的方法
        boolean match = match(instructionStr);
        if (match) {
            //如果指令中有方法
            //解析出方法名
            String[] methods = getMethods(instructionStr);
            //解析方法并获取方法执行者
            List<Invoker> invoker = getMethodInvoker(methods);
            //获取多余字符
            String[] methodsSplit = getMethodsSplit(instructionStr);
            //获取list类型字段值获取器
            fieldValueGetter = getListFieldValueGetter(invoker, methodsSplit);

        } else {
            //如果没有@方法，则说明list集合是String集合或者是可以使用eval函数执行的js代码
            //则创建一个空执行者，将参数作为其输出值
            Invoker nullInvoker = MethodUtil.createNullMethodInvoker(instructionStr);

            //创建字段值获取器
            fieldValueGetter = getListFieldValueGetter(new Invoker[]{nullInvoker});
        }


        //返回字符值获取器
        return fieldValueGetter;
    }


    /**
     * 当字段既不是数组又不是集合的时候，进行解析
     *
     * @return 字段值获取器
     */
    @Override
    public FieldValueGetter parserForNotListOrArrayFieldValueGetter() {
        /*
            假如字段类型为Object类型且存在区间参数，则认为这是一个需要转化为数组的类型，即认为字段类型为List类型，直接使用List字段值生成器
            区间参数只要存在左参数即为存在
         */
        boolean isObjectToList = this.fieldClass.equals(Object.class) && (intervalMin != null);
        if(isObjectToList){
            return this.parserForListFieldValueGetter();
        }


        //字段值获取器
        FieldValueGetter fieldValueGetter;
        //解析指令,查找指令中的@方法
        //先判断是否有匹配的方法
        boolean match = match(instructionStr);

        if (match) {
            //如果存在指令方法
            //解析出方法名
            String[] methods = getMethods(instructionStr);
            //如果有方法，解析方法，解析参数-由于做过判断，所以此处必然有方法

            //解析方法并获取方法执行者
            List<Invoker> invoker = getMethodInvoker(methods);

            //获取多余字符
            String[] methodsSplit = getMethodsSplit(instructionStr);
            //如果参数多余字符不为0，则参数类型必定为String且methodsSplit[]的长度必定为methods[]的长度+1或与methods[]的长度相等
            //则必定为字符串字段
            //有指令方法的时候，如果有区间参数，对字符串的最终输出进行重复
            if (methodsSplit.length > 0) {
                //获取 StringFieldValueGetter字段值获取器，如果有整数位的区间参数，添加参数
                //使用三元运算判断
                fieldValueGetter = getStringFieldValueGetter(invoker, methodsSplit);
            } else {
                //如果没有多余字符，则字段可能不是字符串类型，
                //判断字段的数据类型
                if (fieldClass.equals(String.class)) {
                    //如果是String类型的，使用StringFieldValueGetter字段值获取器
                    fieldValueGetter = getStringFieldValueGetter(invoker);
                } else {
                    //如果字段类型不是String，则不能用StringFieldValueGetter字段值获取器了
                    //使用ObjectFieldValueGetter，不指定参数获取值
                    fieldValueGetter = getObjectFieldValueGetter(invoker);
                }
            }
        } else {
            //如果没有能够匹配的@方法，则说明指令部分就是普通的字符串，创建一个方法执行者为空值的未知类型字段值获取器: ObjectFieldValueGetter
            Integer[] intervalData = getIntervalData();
            //判断区间参数是否存在
            if(intervalData == null){
                //因为是没有@方法的普通字符串，没有多余字符，使用Object类型的字段值获取器即可
                fieldValueGetter = getObjectFieldValueGetter(MethodUtil.createNullMethodInvoker(instructionStr));
            }else{
                //有区间参数，获取一个对字符串重复输出的Invoker
                fieldValueGetter = getStringFieldValueGetter(new Invoker[]{MethodUtil.createNullMethodInvoker(instructionStr)});
            }


        }
        //返回结果
        return fieldValueGetter;
    }


    /**
     * 获取区间参数区间，如果没有区间参数则返回null
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
                return null;
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
     * @param objectClass    类的class对象
     * @param fieldName      字段名称
     * @param intervalStr    区间参数字符串
     * @param instructionStr 指令字符串
     */
    public InstructionParser(Class objectClass, String fieldName, String intervalStr, String instructionStr) {
        super(objectClass, fieldName, intervalStr);
        this.instructionStr = instructionStr;
    }
}
