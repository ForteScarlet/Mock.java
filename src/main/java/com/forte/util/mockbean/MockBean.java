package com.forte.util.mockbean;

import com.forte.util.exception.MockException;

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
    protected Class<T> objectClass;

    /**
     * 假对象的全部字段
     */
    protected MockField[] fields;

    /**
     * 获取对象一个对象
     * @return
     */
    public T getObject() {
        //先创建一个实例
        T instance;
        try {
            instance = getObjectClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
        for (MockField field : fields) {
            try {
                field.setValue(instance);
            } catch (Exception e) {
                // ignored ?
                // no
                throw new MockException(e);
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


    public MockBean<T> parallel(){
        return new ParallelMockBean<>(objectClass, Arrays.copyOf(fields, fields.length));
    }


    public MockBean<T> sequential(){
        return this;
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
