package com.forte.util.utils;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 字段操作工具，提供丰富的方法，以反射的方式从对象中获取字段的值。<br>
 * 其中:<br>
 * - objectGetter方法可以允许使用多级字段，例如"user.child.name"<br>
 * - getExcelNum方法可以获取Excel中列的数字坐标，例如:"AA" => 27
 *
 * @author ForteScarlet
 */
public class FieldUtils {


    //静态代码块加载字母顺序
    static {

        Map<String, Integer> wordNum = new HashMap<>();

        for (int i = 1; i <= 26; i++) {
            char c = (char) (97 + (i - 1));
            wordNum.put(c + "", i);
        }

        //获取字母顺序表
        Map<String, Integer> map = wordNum;

        //保存
        WORD_NUMBER = map;

    }

    /**
     * 字母顺序表
     */
    public static final Map<String, Integer> WORD_NUMBER;

    /**
     * 获取Excel中列的数字坐标<br>
     * 例如:"AA" => 27
     *
     * @param colStr Excel中的列坐标
     * @return 此列坐标对应的数字
     * @author ForteScarlet
     */
    public static long getExcelNum(String colStr) {
        //获取数组
        char[] array = colStr.toCharArray();
        //长度
        int length = array.length;
        //初始数
        long end = 1;

        //倒序遍历,从小位开始遍历
        for (int i = array.length - 1; i >= 0; i--) {
            //字母序号
            int num = WORD_NUMBER.get((array[i] + "").toLowerCase());
            //加成
            int addBuffer = length - i - 1;
            //结果加成
            end += num * (int) (Math.pow(26, addBuffer));
        }

        //返回结果
        return end;
    }

    /**
     * 获取对象的所有字段(包括没有get方法的字段) - root
     *
     * @param tClass 对象
     * @return
     * @author ForteScarlet
     */
    public static List<Field> getFieldsWithoutGetter(Class<?> tClass) {
        Field[] fs = tClass.getDeclaredFields();
        return Arrays.asList(fs);
    }


    /**
     * 获取对象的所有字段(包括没有get方法的字段)
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<Field> getFieldsWithoutGetter(T t) {
        return getFieldsWithoutGetter(t.getClass());
    }


    /**
     * 获取对象的所有字段名(包括没有get方法的字段)
     *
     * @param t 对象
     * @return
     * @author ForteScarlet
     */
    public static <T> List<String> getFieldsNameWithoutGetter(T t) {
        return getFieldsWithoutGetter(t).stream().map(f -> f.getName()).collect(Collectors.toList());
    }

    /**
     * 获取对象的所有字段 - root
     *
     * @param tClass 对象
     * @return
     * @author ForteScarlet
     */
    public static List<Field> getFieldsWithGetter(Class<?> tClass) {
        Field[] fs = tClass.getDeclaredFields();
        //返回时过滤掉没有get方法的字段
        return Arrays.asList(fs).stream().filter(f -> Arrays.stream(tClass.getMethods()).anyMatch(m -> m.getName().equals("get" + headUpper(f.getName())))).collect(Collectors.toList());
    }

    /**
     * 获取对象的所有字段
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<Field> getFieldsWithGetter(T t) {
        return getFieldsWithGetter(t.getClass());
    }

    /**
     * 获取对象的所有字段名
     *
     * @param t 对象
     * @return
     * @author ForteScarlet
     */
    public static <T> List<String> getFieldsNameWithGetter(T t) {
        return getFieldsWithGetter(t).stream().map(f -> f.getName()).collect(Collectors.toList());
    }


    /**
     * 获取类指定的字段对象，支持多层级获取
     * @param c
     * @param fieldName
     * @return
     */
    public static Field fieldGetter(Class c , String fieldName){
        //可以获取多级字段的字段对象
        //使用'.'切割字段名
        String[] split = fieldName.split("\\.");
        if(split.length == 1){
            //如果长度为1，则说明只有一级字段，直接获取字段对象
            return getField(c , fieldName);
        }else{
            //如果不是1，则说明不止1层字段值
            //当前字段名
            String thisFieldName = split[0];
            //剩下的字段名称拼接
            //移除第一个字段
            List<String> list = Arrays.stream(split).collect(Collectors.toList());
            list.remove(0);
            //拼接剩余
            String otherFieldName = list.stream().collect(Collectors.joining("."));
            //递归获取
            return fieldGetter(getFieldClass(c , thisFieldName) , otherFieldName);

        }
    }

    /**
     * 获取字段的class类型，支持多层级获取
     * @param c
     * @param fieldName
     * @return
     */
    public static Class fieldClassGetter(Class c , String fieldName){
        return fieldGetter(c , fieldName).getType();
    }


    /**
     * 通过对象的getter获取字段数值
     * 支持类似“user.child”这种多层级的获取方式
     * 获取的字段必须有其对应的公共get方法
     * @param t
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @author ForteScarlet
     */
    public static Object objectGetter(Object t, String fieldName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //判断是否有用“.”分割
        String[] split = fieldName.split("\\.");
        //如果分割后只有一个字段值，直接返回
        if (split.length == 1) {
            //获取其get方法,返回执行结果
            Method method = t.getClass().getMethod("get" + FieldUtils.headUpper(fieldName));
            return method.invoke(t);
        } else {
            //否则为多层级字段,获取第一个字段名，拼接其余字段名并进行递归处理
            String field = split[0];
            //移除第一个字段
            List<String> list = Arrays.stream(split).collect(Collectors.toList());
            list.remove(0);
            //拼接剩余
            fieldName = list.stream().collect(Collectors.joining("."));

            //field必定为单层字段，获取field对应的对象，然后使用此对象进行递归
            return objectGetter(objectGetter(t, field), fieldName);
        }
    }


    /**
     * 通过对象的setter为字段赋值
     * 支持类似“user.child”这种多层级的赋值方式
     * 赋值的字段必须有其对应的公共set方法
     * 如果多层级对象中有非底层级字段为null，将会为其创建一个新的实例
     *
     * @param t         对象
     * @param fieldName 需要赋值的字段
     * @param value     需要赋的值
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void objectSetter(Object t, String fieldName, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        //判断是否有用“.”分割
        String[] split = fieldName.split("\\.");
        //如果分割后只有一个字段值，直接进行赋值
        if (split.length == 1) {
            //获取其set方法,返回执行结果
            String setterName = "set" + FieldUtils.headUpper(fieldName);
            //获取全部可能为setter的方法
            Method[] setters = Arrays.stream(t.getClass().getMethods()).filter(m -> m.getName().equals(setterName)).toArray(Method[]::new);

            //遍历
            for(Method m : setters){
                //赋值
                try{
                    MethodUtil.invoke(t , new Object[]{value} , m);
                    return;
                }catch (Exception e){}
            }
            //TODO 红字警告,可以考虑成为一个异常
            System.err.println("没有找到["+ t.getClass() +"]中的字段["+ fieldName +"]的setter方法，无法进行赋值");

        } else {
            //否则为多层级字段,获取第一个字段名，拼接其余字段名并进行递归处理
            String field = split[0];
            //移除第一个字段
            List<String> list = Arrays.stream(split).collect(Collectors.toList());
            list.remove(0);
            //拼接剩余
            fieldName = list.stream().collect(Collectors.joining("."));

            //获取下一层的对象
            Object fieldObject = objectGetter(t, field);
            if (fieldObject == null) {
                //如果为null，创建一个此类型的实例
                fieldObject = getFieldClass(t, field).newInstance();
                //并为此对象赋值
                objectSetter(t, field, fieldObject);
            }
            //寻找下一层字段
            objectSetter(fieldObject, fieldName, value);
        }
    }

    /**
     * 获取对象指定字段对象
     *
     * @param object    对象的class对象
     * @param fieldName 字段名称
     */
    public static Field getField(Object object, String fieldName) {
        return getField(object.getClass(), fieldName);
    }


    /**
     * 获取类指定字段对象
     *
     * @param objectClass 类的class对象
     * @param fieldName   字段名称
     */
    public static Field getField(Class objectClass, String fieldName) {
        //反射获取全部字段
        Field[] declaredFields = objectClass.getDeclaredFields();
        //遍历寻找此字段
        Field field = null;
        for (Field f : declaredFields) {
            //如果找到了，赋值并跳出循环
            if (f.getName().equals(fieldName)) {
                field = f;
                break;
            }
        }
        return field;
    }


    /**
     * 获取指定类字段的类型class对象
     *
     * @param objectClass 类class对象
     * @param fieldName   字段名称
     */
    public static Class getFieldClass(Class objectClass, String fieldName) {
        return getField(objectClass, fieldName).getType();
    }

    /**
     * 获取类指定字段的class对象
     *
     * @param object    类实例
     * @param fieldName 字段名称
     * @return
     */
    public static Class getFieldClass(Object object, String fieldName) {
        return getFieldClass(object.getClass(), fieldName);
    }


    /**
     * 通过Class对象判断是否存在此字段
     *
     * @param tClass
     * @param field
     * @return
     */
    public static boolean isFieldExist(Class<?> tClass, String field) {
        //判断是否为多层级字段
        String[] split = field.split("\\.");
        if (split.length == 1) {
            //如果只有一个，直接获取
            Field getField = null;
            try {
                getField = tClass.getDeclaredField(field);
            } catch (NoSuchFieldException e) {
            }
            //如果存在，返回true，否则返回false
            if (getField == null) {
                return false;
            } else {
                return true;
            }
        } else {
            //否则，存在多层级字段，先获取第一个字段并获得其类型，然后通过此类型的Class对象进一步判断
            String firstField = split[0];
            //获取字段对象
            Field getField = null;
            try {
                getField = tClass.getDeclaredField(firstField);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            //如果获取失败，直接返回false
            if (getField == null) {
                return false;
            }

            Class<?> firstFieldType = getField.getType();
            //拼接剩余
            //移除第一个字段
            List<String> list = Arrays.stream(split).collect(Collectors.toList());
            list.remove(0);
            //拼接剩余
            String fieldName = list.stream().collect(Collectors.joining("."));
            //获取当前字段的Class对象
            Class fieldClass = getFieldClass(tClass, firstField);
            //递归
            return isFieldExist(fieldClass, fieldName);
        }
    }

    /**
     * 通过对象实例判断字段是否存在
     *
     * @param obj   实例对象
     * @param field 查询字段
     * @return
     * @throws NoSuchFieldException
     */
    public static boolean isFieldExist(Object obj, String field) {
        return isFieldExist(obj.getClass(), field);
    }


    /**
     * 获取一个list字段的泛型类型<br>
     * 这个字段必须是一个list类型的字段！
     *
     * @param field 字段
     * @return
     * @throws ClassNotFoundException
     */
    public static Class getListGeneric(Field field) {

        ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
        Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
        if (listActualTypeArguments.length == 0) {
            //如果没有数据
            return null;
        } else if (listActualTypeArguments.length == 1) {
            //如果只有一种类型
            String typeName = listActualTypeArguments[0].getTypeName();
            //如果此类型存在泛型，移除泛型
            typeName = typeName.replaceAll("<[\\w\\.\\, ]+>" , "");
            try {
                return Class.forName(typeName);
            } catch (ClassNotFoundException e) {
                //将异常转化为运行时
                throw new RuntimeException(e);
            }
        } else {
            //如果多个类型，直接返回Object类型
            return Object.class;
        }
    }

    /**
     * 获取一个list字段的泛型类型<br>
     * 这个字段必须是一个list类型的字段！
     * @param c
     * @param fieldName
     * @return
     * @throws ClassNotFoundException
     */
    public static Class getListGeneric(Class c , String fieldName) {
        return getListGeneric(fieldGetter(c , fieldName));
    }

    /**
     * 获取一个list字段的泛型类型<br>
     * 这个字段必须是一个list类型的字段！
     * @param obj
     * @param fieldName
     * @return
     * @throws ClassNotFoundException
     */
    public static Class getListGeneric(Object obj , String fieldName) {
        return getListGeneric(obj.getClass() , fieldName);
    }

    /**
     * 判断一个Class对象是否为另一个对象的实现类
     *
     * @param child      进行寻找的子类
     * @param findFather 被寻找的父类
     * @return
     */
    public static boolean isChild(Class child, Class findFather) {
        //如果自身就是这个类，直接返回true
        if (child.equals(findFather)) {
            return true;
        }
        /*
            两个方向，一个是向父继承类递归，一个是向接口递归
         */
        //子类继承的父类
        Class superClass = child.getSuperclass();
        //子类实现的接口
        Class[] interfaces = child.getInterfaces();
        //如果全部为null，直接返回false
        if (superClass == null && interfaces.length == 0) {
            return false;
        }
        //进行判断-先对当前存在的两类型进行判断
        if (superClass != null && superClass.equals(findFather)) {
            //如果发现了，返回true
            return true;
        }

        //遍历接口并判断
        for (Class interClass : interfaces) {
            if (interClass.equals(findFather)) {
                return true;
            }
        }

        //如果当前的没有发现，递归查询
        //如果没有发现，递归父类寻找
        if (superClass != null && isChild(superClass, findFather)) {
            return true;
        }

        //如果父类递归没有找到，进行接口递归查询
        //遍历
        for (Class interClass : interfaces) {
            if (isChild(interClass, findFather)) {
                return true;
            }
        }

        //未查询到
        return false;
    }

    /**
     * 判断一个Class对象是否为另一个对象的实现类
     *
     * @param child      进行寻找的子类的实现类
     * @param findFather 被寻找的父类
     * @return
     */
    public static boolean isChild(Object child, Class findFather) {
        return isChild(child.getClass(), findFather);
    }



    /**
     * 单词开头大写
     *
     * @param str
     * @return
     * @author ForteScarlet
     */
    public static String headUpper(String str) {
        //拿到第一个字母
        String head = (str.charAt(0) + "").toUpperCase();
        //拿到其他
        String body = str.substring(1);
        return head + body;
    }

    /**
     * 获取类名
     *
     * @param c
     * @return
     * @author ForteScarlet
     */
    public static String getClassName(Class<?> c) {
        String name = c.getName();
        String[] split = name.split("\\.");
        return split[split.length - 1];
    }

    /**
     * 通过对象获取类名
     *
     * @param o
     * @return
     * @author ForteScarlet
     */
    public static String getClassName(Object o) {
        return getClassName(o.getClass());
    }


    /**
     * 内部类，实现字段缓存，优化此工具类的效率
     */
    class CacheField{
        // TODO 实现此内部类，实现工具类效率优化
    }


    /**
     * 构造私有化
     */
    private FieldUtils(){}

}
