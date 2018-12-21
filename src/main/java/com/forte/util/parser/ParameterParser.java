package com.forte.util.parser;

import com.forte.util.Mock;
import com.forte.util.fieldvaluegetter.ArrayFieldValueGetter;
import com.forte.util.fieldvaluegetter.FieldValueGetter;
import com.forte.util.fieldvaluegetter.ListFieldValueGetter;
import com.forte.util.invoker.Invoker;
import com.forte.util.mockbean.MockBean;
import com.forte.util.utils.FieldUtils;
import com.forte.util.mockbean.MockField;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 参数解析器，用于解析用户填入的参数语法
 * 解析包({@link com.forte.util.parser})下唯一公共接口，为{@link Mock}解析用户参数
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class ParameterParser {

    /**
     * MockUtil的方法集
     */
    private static final Map<String, Method> MOCK_METHOD = Mock._getMockMethod();

    /* ———— 目前预期类型 ————
     * String / Double / Integer / Map / Object / array&list
     * */

    /* 内部使用的各类型的常量，用于switch，为参数分配解析器 */

    private static final int TYPE_STRING = 0;
    private static final int TYPE_DOUBLE = 1;
    private static final int TYPE_INTEGER = 2;
    private static final int TYPE_MAP = 3;
    private static final int TYPE_OBJECT = 4;
    private static final int TYPE_LIST = 5;
    private static final int TYPE_ARRAY = 6;

    /**
     * 对参数进行解析
     *
     * @param objectClass 需要进行假数据封装的类对象
     * @param paramMap    参数集合
     */
    public static <T> MockBean<T> parser(Class<T> objectClass, Map<String, Object> paramMap) {
        //使用线程安全list集合
        List<MockField> fields = Collections.synchronizedList(new ArrayList<>());
        //遍历并解析-（多线程同步）
        //如果是.entrySet().parallelStream().forEach的话，似乎会出现一个迷之bug
        //如果结果没有任何输出语句打印控制台，会报NullPointer的错
        //已解决，需要使fields这个集合成为线程安全的集合
        paramMap.entrySet().parallelStream().forEach(e -> {
            //解析
            Object value = e.getValue();
            //切割名称，检测是否有区间函数
            String[] split = e.getKey().split("\\|");
            //字段名
            String fieldName = split[0];
            //区间参数字符串
            String intervalStr = split.length > 1 ? split[1] : null;
            //如果对象中不存在此字段，不进行解析
            if (FieldUtils.isFieldExist(objectClass, fieldName)) {
            /*
                判断参数类型
                预期类型：
                String / Double / Integer / Map<Class , Map<String , Object>> / object / array 。。。。。。
             */
                //准备字段解析器
//                FieldParser baseFieldParser;
                //准备假字段对象
                MockField mockField = null;
                int typeNum = typeReferee(value);
                //根据字段参数分配解析器
                switch (typeNum) {
                    case TYPE_STRING:
                        //是字符串，使用指令解析器
                        //获取假字段封装类
                        mockField = stringTypeParse(objectClass, fieldName, intervalStr, value);
                        break;
                    case TYPE_DOUBLE:
                        //是Double的浮点型，使用double浮点解析器
                        //获取假字段封装类
                        mockField = doubleTypeParse(objectClass, fieldName, intervalStr, value);
                        break;
                    case TYPE_INTEGER:
                        //整数解析并获取假字段封装类
                        mockField = integerTypeParse(objectClass, fieldName, intervalStr, value);
                        break;
                    case TYPE_OBJECT:
                        //使用解析器，如果字段类型是集合或数组要重复输出
                        mockField = objectTypeParse(objectClass, fieldName, intervalStr, value);
                        break;
                    case TYPE_MAP:
                        //如果是一个Map集合，说明这个字段映射着另一个假对象
                        //这个Map集合对应的映射类型应该必然是此字段的类型
                        //获取假字段对象
                        mockField = mapTypeParse(objectClass, fieldName, intervalStr, value);
                        break;
                    case TYPE_ARRAY:
                        //如果字段是数组类型，使用数组类型解析器进行解析
                        mockField = arrayTypeParse(objectClass, fieldName, intervalStr, value);
                        break;
                    case TYPE_LIST:
                        //如果字段是list集合类型，使用集合类型解析器解析
                        mockField = listTypeParse(objectClass, fieldName, intervalStr, value);
                        break;
                    default:
                        System.out.println("无法解析");
                        break;
                }

                //添加假字段对象
                fields.add(mockField);
            }
        });


        //解析结束，封装MockObject对象
        return getMockObject(objectClass, fields);

    }

    /* —————————————————————— 各个情况的解析方法 —————————————————— */

    /**
     * 字符串类型参数解析
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     * @param value
     * @return
     */
    private static MockField stringTypeParse(Class objectClass , String fieldName , String intervalStr , Object value){
        //是字符串，使用指令解析器
        FieldParser fieldParser = new InstructionParser(objectClass, fieldName, intervalStr, (String) value);
        //获取假字段封装类
        return fieldParser.getMockField();
    }

    /**
     * double浮点类型解析
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     * @param value
     * @return
     */
    private static MockField doubleTypeParse(Class objectClass , String fieldName , String intervalStr , Object value){
        //是Double的浮点型，使用double浮点解析器
        FieldParser fieldParser = new DoubleParser(objectClass, fieldName, intervalStr, (Double) value);
        //获取假字段封装类
        return fieldParser.getMockField();
    }

    /**
     * 整数类型解析
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     * @param value
     * @return
     */
    private static MockField integerTypeParse(Class objectClass , String fieldName , String intervalStr , Object value){
        //准备字段解析器
        FieldParser fieldParser;
        //如果是整数参数，判断区间参数是否有小数区间
        //如果有区间参数，进行判断
        if(intervalStr != null){
            String[] intervalSplit = intervalStr.split("\\.");
            if(intervalSplit.length > 1){
                //如果切割'.'之后长度大于1，则说明有小数位数，使用浮点数解析器
                fieldParser = new DoubleParser(objectClass, fieldName, intervalStr, ((Integer)value)*1.0);
            }else{
                //如果长度不大于1，则说明没有小数位数，使用整形解析器
                fieldParser = new IntegerParser(objectClass, fieldName, intervalStr, (Integer)value);
            }
        }else{
            //如果没有区间参数，直接使用整数解析器(此处的intervalStr必定为null)
            fieldParser = new IntegerParser(objectClass, fieldName, null, (Integer)value);
        }

        //获取假字段封装类
        return fieldParser.getMockField();
    }


    /**
     * 未知的引用数据类型解析
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     * @param value
     * @return
     */
    private static MockField objectTypeParse(Class objectClass , String fieldName , String intervalStr , Object value){
        ObjectParser objectParser = new ObjectParser(objectClass, fieldName, intervalStr, value);

        //返回假字段对象
        return objectParser.getMockField();
    }


   /**
     * Map集合类型解析
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     * @param value
     * @return
     */
    private static MockField mapTypeParse(Class objectClass , String fieldName , String intervalStr , Object value){
        //如果是一个Map集合，说明这个字段映射着另一个假对象
        //也有可能只是一个普通的Map而不是映射关系
        //需要判断字段的类型，如果字段类型也是Map，则不进行映射解析而是转化为ObjectField
        //这个Map集合对应的映射类型应当必然是此字段的类型
        //获取此字段的class类型
        Class fieldClass = FieldUtils.fieldClassGetter(objectClass, fieldName);
        //判断类型
        if(FieldUtils.isChild(fieldClass , Map.class)){
            //如果字段是Map类型，直接返回此对象作为假字段对象，不做处理
            return getDefaultObjectMockField(fieldName , value);
        }else if(FieldUtils.isChild(fieldClass, List.class) && FieldUtils.getListGeneric(objectClass , fieldName).equals(Map.class) ){
            //如果字段类型是List集合而且集合的泛型是Map类型，使用Object类型解析器
            ObjectParser objectParser = new ObjectParser(objectClass, fieldName, intervalStr, value);
            return objectParser.getMockField();
        }else{
            //如果字段不是Map类型
            //将参数转化为Map<String , Object>类型
            Map<String, Object> fieldMap = (Map<String, Object>)value;
            //判断字段是否为list集合类型或数组类型
            if(FieldUtils.isChild(fieldClass , List.class)){
                //是list集合类型，获取集合的泛型类型
                Class fieldListGenericClass = FieldUtils.getListGeneric(objectClass, fieldName);
                //获取一个假对象
                //同时保存此对象的解析
                MockBean parser = Mock.set(fieldListGenericClass, fieldMap);

                FieldValueGetter fieldValueGetter = objectToListFieldValueGetter(parser, intervalStr);
                return new MockField(fieldName , fieldValueGetter);
            }else if(fieldClass.isArray()){
                //是数组类型，获取数组的类型信息
                Class fieldArrayGeneric = FieldUtils.getArrayGeneric(fieldClass);
                //获取一个假对象
//                MockBean parser = parser(fieldArrayGeneric, fieldMap);
                //同时保存此对象的解析
                MockBean parser = Mock.set(fieldArrayGeneric, fieldMap);

                FieldValueGetter fieldValueGetter = objectToArrayFieldValueGetter(parser, intervalStr);
                return new MockField(fieldName , fieldValueGetter);

            }else{
                //得到一个假对象数据，封装为一个MockField
//                MockBean parser = parser(fieldClass, fieldMap);
                //同时保存此对象的解析
                MockBean parser = Mock.set(fieldClass, fieldMap);

               return objectToField(fieldName , parser);
            }

        }
    }


    /**
     * 数组类型参数解析
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     * @param value
     * @return
     */
    private static MockField arrayTypeParse(Class objectClass , String fieldName , String intervalStr , Object value){
        //准备字段解析器
        FieldParser fieldParser;
        //当参数为一个数组的时候，使用数组解析器
        fieldParser = new ArraysParser(objectClass , fieldName , intervalStr , (Object[])value);

        //获取假字段封装类
        return fieldParser.getMockField();
    }

    /**
     * 集合类型参数解析
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     * @param value
     * @return
     */
    private static MockField listTypeParse(Class objectClass , String fieldName , String intervalStr , Object value){
        //准备字段解析器
        FieldParser fieldParser;
        //如果参数是list集合类型的，使用list参数解析器
        fieldParser = new ListParser(objectClass , fieldName , intervalStr , (List)value);

        //获取假字段封装类
        return fieldParser.getMockField();
    }


    /**
     * 获取一个默认值假字段
     * @param fieldName
     * @param value
     * @return
     */
    private static MockField getDefaultObjectMockField(String fieldName , Object value){
        return new MockField(fieldName , () -> value);
    }

    /**
     * 将一个假类对象封装为一个假字段对象
     * @param object
     * @return
     */
    private static MockField objectToField(String fieldName , MockBean object){
        //使用lambda表达式，创建一个MOckField对象并返回
        return new MockField(fieldName , object::getObject);
    }


    /**
     * 将假字段对象转化为集合字段值获取器
     * @param object
     * @param intervalStr
     * @return
     */
    private static FieldValueGetter objectToListFieldValueGetter(MockBean object , String intervalStr){
        //创建一个方法执行者
        Invoker invoker = object::getObject;

        //获取区间参数
        Integer[] integers = intervalParse(intervalStr);
        //获取集合字段值获取器
        return new ListFieldValueGetter(new Invoker[]{invoker}, integers);
    }

    /**
     * 将假字段对象转化为数组字段值获取器
     * @param object
     * @param intervalStr
     * @return
     */
    private static FieldValueGetter objectToArrayFieldValueGetter(MockBean object , String intervalStr){
        //创建一个方法执行者
        Invoker invoker = object::getObject;

        //获取区间参数
        Integer[] integers = intervalParse(intervalStr);
        //获取集合字段值获取器
        return new ArrayFieldValueGetter(new Invoker[]{invoker}, integers);
    }

    /**
     * 解析区间参数
     * @param intervalStr
     * @return
     */
    private static Integer[] intervalParse(String intervalStr){
        if(intervalStr == null){
            //如果没有区间参数，直接返回[1,1]
            return new Integer[]{1,1};
        }else{
            //有区间参数，解析
            //切割，有可能有小数位的区间
            //期望中，切割后长度最多为2
            String[] split = intervalStr.split("\\.");
            //整数位的区间参数
            String integerInterval = split[0].trim();
            if (integerInterval.length() > 0) {
                //如果不是空的，切割
                String[] splitIntInterval = integerInterval.split("-");
                int intervalMin = Integer.parseInt(splitIntInterval[0]);
                int intervalMax = splitIntInterval.length > 1 ? Integer.parseInt(splitIntInterval[1]) : 1;
                return new Integer[]{intervalMin, intervalMax};
            }else{
                //如果为空，返回[1,1]
                return new Integer[]{1,1};
            }
        }
    }





    /**
     * 获取一个MockObject
     *
     * @param <T>
     * @return
     */
    private static <T> MockBean<T> getMockObject(Class<T> objectObject, MockField[] fields) {
        //返回封装结果
        return new MockBean<>(objectObject, fields);
    }

    /**
     * 获取一个MockObject
     *
     * @param <T>
     * @return
     */
    private static <T> MockBean<T> getMockObject(Class<T> objectObject, List<MockField> fields) {
        //返回封装结果
        return getMockObject(objectObject, fields.toArray(new MockField[0]));
    }

    /**
     * 判断这个类型在预期类型中是哪一个类型的
     *
     * @return
     */
    private static int typeReferee(Object object) {
        //String 类型，属于指令
        if (object instanceof String) {
            return TYPE_STRING;
        }
        //Integer 类型，属于整数
        if (object instanceof Integer) {
            return TYPE_INTEGER;
        }
        //Double 类型，属于浮点数
        if (object instanceof Double) {
            return TYPE_DOUBLE;
        }
        //Map类型，属于一个集合类或者对象类
        if (FieldUtils.isChild(object, Map.class)) {
            return TYPE_MAP;
        }
        //List类型，可考虑将其转化为Array类型，减少工作量
        if (FieldUtils.isChild(object, List.class)) {
            return TYPE_LIST;
        }
        //数组类型，属于数组类
        if (object.getClass().isArray()) {
            return TYPE_ARRAY;
        }
        //其他情况，为一个未知的Object类型
        return TYPE_OBJECT;
    }


}
