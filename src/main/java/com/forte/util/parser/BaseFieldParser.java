package com.forte.util.parser;

import com.forte.util.Mock;
import com.forte.util.fieldvaluegetter.*;
import com.forte.util.invoker.Invoker;
import com.forte.util.mockbean.MockField;
import com.forte.util.utils.FieldUtils;
import com.forte.util.utils.MethodUtil;
import com.forte.util.utils.RegexUtil;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 所有字段解析器的抽象父类<br>
 * <br>
 * <br>
 * <br>
 * 每一个字段都有可能是集合或者数组类型的，但是每种类型的参数解析结果却又不同<br>
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
abstract class BaseFieldParser implements FieldParser {

    /**
     * 类的class对象
     */
    protected final Class objectClass;

    /**
     * 字段名称
     */
    protected final String fieldName;

    /**
     * 区间参数字符串min-max
     */
    protected final Integer intervalMin, intervalMax;

    /**
     * 区间参数字符串-小数位min-max(如果有的话)
     */
    protected final Integer intervalDoubleMin, intervalDoubleMax;

    /**
     * 此字段的class类型
     */
    protected final Class fieldClass;

    /**
     * 获取一个封装好的MockField对象
     * 不可重写
     * @return 封装好的MockField对象
     */
    @Override
    public final MockField getMockField(){
        //解析获取字段值获取器
        FieldValueGetter fieldValueGetter = parserForFieldValueGetter();

        //创建一个MockField对象并返回
        return new MockField(fieldName, fieldValueGetter);
    };

    /**
     * 进行解析，判断需要产生的字段类型
     */
    private FieldValueGetter parserForFieldValueGetter() {
        //字段值获取器
        FieldValueGetter fieldValueGetter;
        //判断这个字段的类型是不是List集合的形式
        //根据字段的class对象判断类型
        boolean isList = FieldUtils.isChild(fieldClass, List.class);
        //如果是list集合类型
        if (isList) {
            //是list集合形式的解析
            fieldValueGetter = parserForListFieldValueGetter();

        } else if (fieldClass.isArray()) {
            //是Array数组的解析
            fieldValueGetter = parserForArrayFieldValueGetter();

        } else {
            //不是list集合形式或Array数组的解析
            fieldValueGetter = parserForNotListOrArrayFieldValueGetter();
        }

        //返回结果
        return fieldValueGetter;
    }



    /* * * —————————————— 为子类服务的辅助方法 —————————————— * * */


    /* —————————————— 正则与方法的解析 ———————————————— */

    /**
     * 看看有没有匹配的方法
     *
     * @param name 方法名
     * @return  是否有
     */
    protected static boolean match(String name) {
        //获取正则
        String regex = ".*" + getMethodRegex() + ".*";
        return name.matches(regex);
    }

    /**
     * 获取匹配的方法
     * @param name 方法名
     * @return  匹配的方法合集
     */
    protected static String[] getMethods(String name) {
        //获取正则
        String regex = getMethodRegex();
        return RegexUtil.getMatcher(name, regex).toArray(new String[0]);
    }

    /**
     * 获取对匹配的方法进行切割之后的结果
     *
     * @return
     */
    protected static String[] getMethodsSplit(String name) {
        //获取正则
        String regex = getMethodRegex();
        return name.split(regex);
    }


    /**
     * 获取一个方法执行者
     * @param methodName
     * @return
     */
    protected static Invoker getOneMethodInvoker(String methodName){
        return getMethodInvoker(new String[]{methodName}).get(0);
    }


    /**
     * 为方法{@link #getMethodInvoker}服务，提供正则来获取方法名
     * @return
     */
    private static String getReplaceForNameRegex(){
        return "@|(\\(\\))|(\\(((\\w+)|('.+')|(\".+\"))(\\,((\\w+)|('.+')|(\".+\")))*\\))";
    }

    /**
     * 解析方法字符串并获取方法执行者
     * @param methods
     * 方法名列表
     * @return
     */
    protected static List<Invoker> getMethodInvoker(String[] methods) {
        //移除@符号、空括号、一个参数的括号、两个参数的括号
//        String replaceForNameRegex = "@|(\\(\\))|(\\(\\w+(\\,\\w+)*\\))";
        String replaceForNameRegex = getReplaceForNameRegex();
//        String replaceForParamRegex = "[(@" + getMethodNameRegexs() + ")\\(\\)]";
//        String regex = "@" + getMethodNameRegexs() + "(((\\((\\w+|\\w+\\,\\w+)\\))|(\\(\\))|())?)";

        List<Invoker> invokerList = new ArrayList<>();

        //遍历方法，保证顺序
        for (String methodStr : methods) {
            //获取方法名称
            String methodName = methodStr.replaceAll(replaceForNameRegex, "");

            //获取方法的参数
            String[] params = getMethodParams(methodStr);
            //获取方法对象
            Method method = getMethodFromName(methodName, params.length);
            //返回一个方法执行者，如果为没有对应的方法则创建一个空执行者，返回原本的字符串
            if (method == null) {
                invokerList.add(MethodUtil.createNullMethodInvoker(methodStr));
            } else {
                invokerList.add(MethodUtil.createMethodInvoker(null, params, method));
            }
        }

        //返回方法执行者的集合
        return invokerList;
    }


    /**
     * 获取匹配MockUtil中的方法的正则
     *
     * @return
     */
    protected static String getMethodRegex() {
        String collect = getMethodNameRegexs();
        //全数量参数匹配、参数间空格匹配（参数为任意字符匹配）- 有bug
//        String regex = "(@"+ collect +"+((\\((.+(\\,.+)*)\\))|(\\(\\))|()|))";
        //全数量参数匹配、参数\\w匹配
//        String regex = "(@" + collect + "+((\\((\\w+(\\,\\w+)*)\\))|(\\(\\))|())?)";

        //尝试支持中文字符串
        String regex = "(@" + collect + "{1}((\\((((\\w)+|('.+')|(\".+\"))(\\,((\\w)+|('.+')|(\".+\")))*)\\))|(\\(\\))|())?)";

        //值匹配0-1-2个参数
//        String regex = "(@"+ collect +"+((\\((\\w+|\\w+\\,\\w+)\\))|(\\(\\))|())?)";
        return regex;
    }


    /**
     * 从方法字符串中获取参数
     * @param methodStr 方法字符串
     * @return
     */
    public static String[] getMethodParams(String methodStr) {
        String replaceForParamRegex = "(@" + getMethodNameRegexs() + ")|\\(|\\)";
        String[] split = methodStr.replaceAll(replaceForParamRegex, "").split("( *)\\,( *)");
        return Arrays.stream(split).map(s -> s.trim().replaceAll("['\"]", "")).filter(s -> s.length() > 0).toArray(String[]::new);
    }

    /**
     * 通过名称获取方法
     * @param methodName   纯方法名称
     * @param paramsLength 参数数量
     * @return MockUtil中对应方法名与参数数量的方法对象，如果没有获取到则返回null
     */
    public static Method getMethodFromName(String methodName, int paramsLength) {
        //过滤出方法名匹配,参数长度也匹配的方法
        Method value = null;

        try {
            //取值，理论上来说最后的过滤结果应该只有一个结果
            value = Mock._getMockMethod().entrySet().stream()
                    .filter(e -> e.getKey().replaceAll("\\([\\w\\.\\,]*\\)","").equals(methodName) && e.getValue().getParameters().length == paramsLength).findFirst().get().getValue();
        } catch (NoSuchElementException e) {
        }

        //返回结果
        return value;
    }


    /**
     * 获取全部方法的正则匹配字符串
     *
     * @return
     */
    protected static String getMethodNameRegexs() {
        return Mock._getMockMethod().entrySet().stream().map(e -> e.getKey().replaceAll("(\\(.*\\))", "")).distinct().collect(Collectors.joining("|", "(", ")"));
    }



    /* —————————————————— 获取各种参数获取器的方法 —————————————————————— */

    /* —————————————————— Integer参数获取器的方法 —————————————————————— */
    /**
     * 获取随机整数的方法名，有三种参数：
     * {@link com.forte.util.utils.MockUtil#integer()}<em>获取随机数字 0-9</em>
     * {@link com.forte.util.utils.MockUtil#integer(Integer)}<em>获取指定长度的随机数,※不可超过int最大上限</em>
     * {@link com.forte.util.utils.MockUtil#integer(Integer, Integer)}<em>获取指定区间[a,b]的随机数,※不可超过int最大上限</em>
     */
    private static final String INTEGER_METHOD_NAME = "@integer";

    /**
     * 获取随机整数方法执行者，由于整数三种参数中方法的执行类型不同，则需要分为三个方法
     * @return
     */
    private Invoker getIntegerMethodInvoker(Integer intIntervalMin , Integer intIntervalMax){
        //拼接出方法
        StringBuilder methodBuilder = new StringBuilder();
        methodBuilder.append(INTEGER_METHOD_NAME)
                     .append("(")
                     .append(intIntervalMin)
                     .append(",")
                     .append(intIntervalMax)
                     .append(")");

        //获取方法字符串
        String methodStr = methodBuilder.toString();

        //返回方法执行者
        return getOneMethodInvoker(methodStr);
    }
    /**
     * 获取随机整数方法执行者，由于整数三种参数中方法的执行类型不同，则需要分为三个方法
     * @return
     */
    private Invoker getIntegerMethodInvoker(Integer intIntervalLength){
        //拼接出方法
        StringBuilder methodBuilder = new StringBuilder();
        methodBuilder.append(INTEGER_METHOD_NAME);
        methodBuilder.append("(");
        methodBuilder.append(intIntervalLength);
        methodBuilder.append(")");

        //获取方法字符串
        String methodStr = methodBuilder.toString();

        //返回方法执行者
        return getOneMethodInvoker(methodStr);
    }
    /**
     * 获取随机整数方法执行者，由于整数三种参数中方法的执行类型不同，则需要分为三个方法
     * @return
     */
    private Invoker getIntegerMethodInvoker(){
        //拼接出方法
        //获取方法字符串
        String methodStr = INTEGER_METHOD_NAME + "()";

        //返回方法执行者
        return getOneMethodInvoker(methodStr);
    }

    /**
     * 获取一个整数类型字段值获取器
     * @param intIntervalMin
     * @param intIntervalMax
     * @return
     */
    protected IntegerFieldValueGetter getIntegerFieldValueGetter(Integer intIntervalMin , Integer intIntervalMax){
        //获取一个整数类型字段值获取器
        //获取随机整数方法执行者
        Invoker integerMethodInvoker = getIntegerMethodInvoker(intIntervalMin, intIntervalMax);

        //创建整数类型字段值获取器
        return new IntegerFieldValueGetter(integerMethodInvoker);
    }

    /**
     * 获取一个整数类型字段值获取器
     * @param intIntervalLength
     * 整数的长度
     * @return
     */
    protected IntegerFieldValueGetter getIntegerFieldValueGetter(Integer intIntervalLength){
        //获取一个整数类型字段值获取器
        //获取随机整数方法执行者
        Invoker integerMethodInvoker = getIntegerMethodInvoker(intIntervalLength);

        //创建整数类型字段值获取器
        return new IntegerFieldValueGetter(integerMethodInvoker);
    }

    /**
     * 获取一个整数类型字段值获取器
     * @return
     */
    protected IntegerFieldValueGetter getIntegerFieldValueGetter(){
        //获取一个整数类型字段值获取器
        //获取随机整数方法执行者
        Invoker integerMethodInvoker = this::getIntegerMethodInvoker;

        //创建整数类型字段值获取器
        return new IntegerFieldValueGetter(integerMethodInvoker);
    }


    /* —————————————————— Double参数获取器的方法 —————————————————————— */
    /**
     * 获取随机小数的方法名，有4种参数：
     * {@link com.forte.util.utils.MockUtil#doubles(Integer, Integer, Integer, Integer)}<em>获取制定区间[a,b]的小数，指定小数位数[endL,endR]，double类型</em>
     * {@link com.forte.util.utils.MockUtil#doubles(Integer, Integer, Integer)}<em>获取制定区间[a,b]的小数，指定小数位数[end]，double类型</em>
     * {@link com.forte.util.utils.MockUtil#doubles(Integer, Integer)}<em>获取指定区间[a,b]的小数，默认小数位数为0，double类型</em>
     * {@link com.forte.util.utils.MockUtil#doubles(Integer)}<em>获取指定数值为a的小数，默认小数位数为0，double类型</em>
     *
     */
    private final String DOUBLE_METHOD_NAME = "@doubles";



    /**
     * 获取随机小数的方法执行者
     * @param intIntervalMin
     * @param intIntervalMax
     * @param doubleIntervalMin
     * @param doubleIntervalMax
     * @return
     */
    private Invoker getDoublesMethodInvoker(Integer intIntervalMin, Integer intIntervalMax, Integer doubleIntervalMin, Integer doubleIntervalMax){
        StringBuilder strForMethod = new StringBuilder();
        strForMethod.append(DOUBLE_METHOD_NAME);
        strForMethod.append("(");
        strForMethod.append(intIntervalMin);
        strForMethod.append(",");
        strForMethod.append(intIntervalMax);
        strForMethod.append(",");
        strForMethod.append(doubleIntervalMin);
        strForMethod.append(",");
        strForMethod.append(doubleIntervalMax);
        strForMethod.append(")");

        String methodStr = strForMethod.toString();

        //获取并返回方法执行者
        return getOneMethodInvoker(methodStr);

    }

    /**
     * 获取小数字段值获取器
     * @param intInterval
     * 整数部分数值
     * @return
     */
    protected DoubleFieldValueGetter getDoubleFieldValueGetter(Integer intInterval){
        //获取方法执行者
        String methodName = DOUBLE_METHOD_NAME + "("+ intInterval +")";
        //获取方法执行者
        Invoker oneMethodInvoker = getOneMethodInvoker(methodName);

        //创建并返回double字段值获取器
        return new DoubleFieldValueGetter(oneMethodInvoker);
    }

    /**
     * 获取小数字段值获取器
     * @param intIntervalMin
     * 整数部分区间最小值
     * @param intIntervalMax
     * 整数部分区间最大值
     * @return
     */
    protected DoubleFieldValueGetter getDoubleFieldValueGetter(Integer intIntervalMin , Integer intIntervalMax){
        return getDoubleFieldValueGetter(intIntervalMin , intIntervalMax , 0 , 0);
    }

    /**
     * 获取小数字段值获取器
     *  @param intIntervalMin
     *  整数部分最小数区间
     *  @param intIntervalMax
     *  整数部分最大数区间
     *  @param doubleInterval
     *  小数部分位数
     * @return
     */
    protected DoubleFieldValueGetter getDoubleFieldValueGetter(Integer intIntervalMin , Integer intIntervalMax , Integer doubleInterval){
        return getDoubleFieldValueGetter(intIntervalMin , intIntervalMax , doubleInterval , doubleInterval);
    }

    /**
     * 获取小数字段值获取器
     * @param intIntervalMin
     * 整数部分最小数区间
     * @param intIntervalMax
     * 整数部分最大数区间
     * @param doubleIntervalMin
     * 小数部分最小位数区间
     * @param doubleIntervalMax
     * 小数部分最大位数区间
     * @return
     */
    protected DoubleFieldValueGetter getDoubleFieldValueGetter(Integer intIntervalMin , Integer intIntervalMax , Integer doubleIntervalMin , Integer doubleIntervalMax){
        //有一下4种情况种可能，
        //1 - 4个参数都有
        //2 - 整数部分没有右参数
        //3 - 小数部分没有右参数
        //4 - 整数和小数都没有右边参数

        //先判断两部分的右参数，如果没有则赋值与左参数相同
        intIntervalMax = Optional.ofNullable(intIntervalMax).orElse(intIntervalMin);
        doubleIntervalMax = Optional.ofNullable(doubleIntervalMax).orElse(intIntervalMin);


        //现在只有一种情况：4个参数都有，拼接参数
        //获取方法执行者
        Invoker oneMethodInvoker =getDoublesMethodInvoker(intIntervalMin , intIntervalMax , doubleIntervalMin , doubleIntervalMax);

        //创建并返回double字段值获取器
        return new DoubleFieldValueGetter(oneMethodInvoker);
    }


    /* —————————————————— Array参数获取器的方法 —————————————————————— */


    /**
     * 获取一个List类型字段值获取器
     *
     * @param invokers        方法执行者
     * @param integerInterval 区间参数
     * @param moreStrs        多余字符
     */
    protected FieldValueGetter getArrayFieldValueGetter(Invoker[] invokers, Integer[] integerInterval, String[] moreStrs) {
        if (integerInterval == null) {
            //创建对象并返回
            return new ArrayFieldValueGetter(invokers, moreStrs);
        } else {
            //创建对象并返回
            return new ArrayFieldValueGetter(invokers, integerInterval, moreStrs);
        }
    }

    /**
     * 获取一个List类型字段值获取器
     * @param invokers
     * 方法执行者
     * @param integerInterval
     * 区间参数
     * @return
     */
    protected FieldValueGetter getArrayFieldValueGetter(Invoker[] invokers, Integer[] integerInterval) {
        if (integerInterval == null) {
            //创建对象并返回
            return new ArrayFieldValueGetter(invokers);
        } else {
            //创建对象并返回
            return new ArrayFieldValueGetter(invokers, integerInterval);
        }
    }


    /**
     * 获取一个数组类字段值获取器
     *
     * @param invokers        方法执行者
     * @param integerInterval 区间参数
     * @param moreStrs        多余字符
     */
    protected FieldValueGetter getArrayFieldValueGetter(List<Invoker> invokers, Integer[] integerInterval, String[] moreStrs) {
        Invoker[] invokersArr = invokers.toArray(new Invoker[0]);
        if (integerInterval == null) {
            //创建对象并返回
            return new ArrayFieldValueGetter(invokersArr, moreStrs);
        } else {
            //创建对象并返回
            return new ArrayFieldValueGetter(invokersArr, integerInterval, moreStrs);
        }
    }

    /**
     * 获取一个数组类字段值获取器
     *
     * @param invokers 方法执行者
     * @param moreStrs 多余字符
     */
    protected FieldValueGetter getArrayFieldValueGetter(Invoker[] invokers, String[] moreStrs) {
        //创建对象并返回
        return getArrayFieldValueGetter(invokers, new Integer[]{intervalMin, intervalMax}, moreStrs);
    }

    /**
     * 获取一个数组类型字段值获取器
     *
     * @param invokers 方法执行者
     * @param moreStrs 多余字符
     */
    protected FieldValueGetter getArrayFieldValueGetter(List<Invoker> invokers, String[] moreStrs) {
        //创建对象并返回
        return getArrayFieldValueGetter(invokers.toArray(new Invoker[0]), new Integer[]{intervalMin, intervalMax}, moreStrs);
    }


    /**
     * 获取一个数组类型字段值获取器
     *
     * @param invokers 方法执行者
     */
    protected FieldValueGetter getArrayFieldValueGetter(Invoker[] invokers) {
        //创建对象并返回
        return new ArrayFieldValueGetter(invokers, new Integer[]{intervalMin, intervalMax});
    }

    /**
     * 获取一个数组类型字段值获取器
     *
     * @param invokers 方法执行者
     */
    protected FieldValueGetter getArrayFieldValueGetter(List<Invoker> invokers) {
        //创建对象并返回
        return new ArrayFieldValueGetter(invokers.toArray(new Invoker[0]), new Integer[]{intervalMin, intervalMax});
    }



    /* —————————————————— List参数获取器的方法 —————————————————————— */


    /**
     * 获取一个List类型字段值获取器
     *
     * @param invokers        方法执行者
     * @param integerInterval 区间参数
     * @param moreStrs        多余字符
     */
    protected FieldValueGetter getListFieldValueGetter(Invoker[] invokers, Integer[] integerInterval, String[] moreStrs) {
        if (integerInterval == null) {
            //创建对象并返回
            return new ListFieldValueGetter(invokers, moreStrs);
        } else {
            //创建对象并返回
            return new ListFieldValueGetter(invokers, integerInterval, moreStrs);
        }
    }

    /**
     * 获取一个List类型字段值获取器
     *
     * @param invokers        方法执行者
     * @param integerInterval 区间参数
     */
    protected FieldValueGetter getListFieldValueGetter(Invoker[] invokers, Integer[] integerInterval) {
        if (integerInterval == null) {
            //创建对象并返回
            return new ListFieldValueGetter(invokers);
        } else {
            //创建对象并返回
            return new ListFieldValueGetter(invokers, integerInterval);
        }
    }

    /**
     * 获取一个List类型字段值获取器
     *
     * @param invokers        方法执行者
     * @param integerInterval 区间参数
     * @param moreStrs        多余字符
     */
    protected FieldValueGetter getListFieldValueGetter(List<Invoker> invokers, Integer[] integerInterval, String[] moreStrs) {
        Invoker[] invokersArr = invokers.toArray(new Invoker[0]);
        if (integerInterval == null) {
            //创建对象并返回
            return new ListFieldValueGetter(invokersArr, moreStrs);
        } else {
            //创建对象并返回
            return new ListFieldValueGetter(invokersArr, integerInterval, moreStrs);
        }
    }

    /**
     * 获取一个List类型字段值获取器
     *
     * @param invokers        方法执行者
     * @param integerInterval 区间参数
     */
    protected FieldValueGetter getListFieldValueGetter(List<Invoker> invokers, Integer[] integerInterval) {
        Invoker[] invokersArr = invokers.toArray(new Invoker[0]);
        if (integerInterval == null) {
            //创建对象并返回
            return new ListFieldValueGetter(invokersArr);
        } else {
            //创建对象并返回
            return new ListFieldValueGetter(invokersArr, integerInterval);
        }
    }

    /**
     * 获取一个List类型字段值获取器
     *
     * @param invokers 方法执行者
     * @param moreStrs 多余字符
     */
    protected FieldValueGetter getListFieldValueGetter(Invoker[] invokers, String[] moreStrs) {
        //创建对象并返回
        return getListFieldValueGetter(invokers, new Integer[]{intervalMin, intervalMax}, moreStrs);
    }


    /**
     * 获取一个List类型字段值获取器
     *
     * @param invokers 方法执行者
     * @param moreStrs 多余字符
     */
    protected FieldValueGetter getListFieldValueGetter(List<Invoker> invokers, String[] moreStrs) {
        //创建对象并返回
        return getListFieldValueGetter(invokers.toArray(new Invoker[0]), new Integer[]{intervalMin, intervalMax}, moreStrs);
    }


    /**
     * 获取一个List类型字段值获取器
     *
     * @param invokers 方法执行者
     */
    protected FieldValueGetter getListFieldValueGetter(Invoker[] invokers) {
        //创建对象并返回
        return new ListFieldValueGetter(invokers, new Integer[]{intervalMin, intervalMax});
    }

    /**
     * 获取一个List类型字段值获取器
     *
     * @param invokers 方法执行者
     */
    protected FieldValueGetter getListFieldValueGetter(List<Invoker> invokers) {
        //创建对象并返回
        return new ListFieldValueGetter(invokers.toArray(new Invoker[0]), new Integer[]{intervalMin, intervalMax});
    }




    /* —————————————————— Object参数获取器的方法 —————————————————————— */


    /**
     * 获取一个未知引用类型参数获取器
     *
     * @return
     */
    protected FieldValueGetter getObjectFieldValueGetter(Invoker... invokers) {
        return new ObjectFieldValueGetter(invokers);
    }

    /**
     * 获取一个未知引用类型参数获取器
     *
     * @return
     */
    protected FieldValueGetter getObjectFieldValueGetter(List<Invoker> invokers) {
        return new ObjectFieldValueGetter(invokers.toArray(new Invoker[0]));
    }


    /* —————————————————— String参数获取器的方法 —————————————————————— */


    /**
     * 获取一个字符串参数获取器
     *
     * @param invokers      方法执行者数组
     * @param methodsSplit  多余字符数组
     * @param intervalArray 区间参数
     * @return
     */
    protected FieldValueGetter getStringFieldValueGetter(Invoker[] invokers, String[] methodsSplit, Integer[] intervalArray) {
        //获取 StringFieldValueGetter参数获取器，如果有整数位的区间参数，添加参数
        //使用三元运算判断
        //如果为空||长度小于1||长度大于2||索引0为空
        boolean intervalArrayIsRight = !(intervalArray == null || intervalArray.length < 1 || intervalArray.length >= 2 || intervalArray[0] == null);
        return intervalArrayIsRight ?
                new StringFieldValueGetter(invokers, methodsSplit, intervalArray)
                :
                new StringFieldValueGetter(invokers, methodsSplit);
    }


    /**
     * 获取一个字符串参数获取器
     *
     * @param invokers     方法执行者数组
     * @param methodsSplit 多余字符数组
     * @return
     */
    protected FieldValueGetter getStringFieldValueGetter(Invoker[] invokers, String[] methodsSplit) {
        //获取 StringFieldValueGetter参数获取器，如果有整数位的区间参数，添加参数
        //使用三元运算判断
        return intervalMax == null ?
                new StringFieldValueGetter(invokers, methodsSplit)
                :
                new StringFieldValueGetter(invokers, methodsSplit, new Integer[]{intervalMin, intervalMax});
    }

    /**
     * 获取一个字符串参数获取器
     *
     * @param invokers     方法执行者集合
     * @param methodsSplit 多余字符数组
     * @return
     */
    protected FieldValueGetter getStringFieldValueGetter(List<Invoker> invokers, String[] methodsSplit) {
        return getStringFieldValueGetter(invokers.toArray(new Invoker[0]), methodsSplit);
    }

    /**
     * 获取一个没有多余字符的字符串字段值获取器
     *
     * @param invokers 方法执行者数组
     * @return
     */
    protected FieldValueGetter getStringFieldValueGetter(Invoker[] invokers) {
        return getStringFieldValueGetter(invokers, null);
    }

    /**
     * 获取一个没有多余字符的字符串字段值获取器
     *
     * @param invokers 方法执行者集合
     * @return
     */
    protected FieldValueGetter getStringFieldValueGetter(List<Invoker> invokers) {
        return getStringFieldValueGetter(invokers, null);
    }




    /* ————————————————————— 构造 ————————————————————— */


    /**
     * 构造
     *
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     */
    public BaseFieldParser(Class objectClass, String fieldName,  String intervalStr) {
        //保存数据
        this.objectClass = objectClass;
        this.fieldName = fieldName;
        //获取此字段的数据类型
        if(objectClass != null)
            this.fieldClass = FieldUtils.fieldClassGetter(objectClass, fieldName);
        else
            this.fieldClass = Object.class;
        //解析区间参数,如果有的话
        if (intervalStr != null) {
            //切割，看看有没有小数位的区间
            //期望中，切割后长度最多为2
            String[] split = intervalStr.split("\\.");
            //整数位的区间参数
            String integerInterval = split[0].trim();
            if (integerInterval.length() > 0) {
                //如果不是空的，切割并记录
                String[] splitIntInterval = integerInterval.split("-");
                this.intervalMin = Integer.parseInt(splitIntInterval[0]);
                this.intervalMax = splitIntInterval.length > 1 ? Integer.parseInt(splitIntInterval[1]) : null;
            } else {
                //否则赋值为空
                this.intervalMin = null;
                this.intervalMax = null;
            }

            //如果切割后的长度不只一个，说明还有小数位数
            if (split.length > 1) {
                String doubleInterval = split[1].trim();
                //如果小数位上有值
                if (doubleInterval.length() > 0) {
                    //如果不是空的，切割并记录
                    String[] splitDouInterval = doubleInterval.split("\\-");
                    this.intervalDoubleMin = Integer.parseInt(splitDouInterval[0]);
                    this.intervalDoubleMax = splitDouInterval.length > 1 ? Integer.parseInt(splitDouInterval[1]) : null;
                } else {
                    //否则赋空值
                    this.intervalDoubleMax = null;
                    this.intervalDoubleMin = null;
                }

            } else {
                //如果没有，赋值为空
                this.intervalDoubleMax = null;
                this.intervalDoubleMin = null;
            }
        } else {
            //如果没有，赋值为null
            this.intervalMin = null;
            this.intervalMax = null;
            this.intervalDoubleMax = null;
            this.intervalDoubleMin = null;

        }
    }
}
