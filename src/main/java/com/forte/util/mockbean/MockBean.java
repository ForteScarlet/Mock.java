package com.forte.util.mockbean;

import java.util.Arrays;

/**
 * 假对象的封装类，利用此类的getObject来获取一个对象
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class MockBean<T> {

    /**
     * 需要封装假数据的对象
     */
    private Class<T> objectClass;

    /**
     * 假对象的全部字段
     */
    private MockField[] fields;

    /**
     * 获取对象一个对象
     * @return
     */
    public T getObject() {
        //先创建一个实例
        T instance;
        try {
            instance = objectClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
        //抓取错误，若实例创建错误，直接返回null
        //使用多线程执行
        // 2020/7/13 多线程个p
        for (MockField field : fields) {
            try {
                field.setValue(instance);
            } catch (Exception e) {
                // ignored ?
                throw new RuntimeException(e);
            }
        }
        //返回这个实例
        return instance;
    }

    /**
     * 获取假字段集
     */
    public MockField[] getFields(){
        return Arrays.copyOf(fields, fields.length);
    }

    public Class<T> getObjectClass(){
        return objectClass;
    }

    /**
     * 构造方法
     *
     * @param objectClass
     * @param fields
     */
    public MockBean(Class<T> objectClass, MockField[] fields) {
        this.objectClass = objectClass;
        this.fields = fields;
    }
}
