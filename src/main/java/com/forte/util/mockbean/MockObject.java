package com.forte.util.mockbean;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 假对象的封装类，利用此类的getObject来获取一个对象
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class MockObject<T> {

    /**
     * 需要封装假数据的对象
     */
    private Class<T> objectClass;

    /**
     * 假对象的全部字段
     */
    private MockField[] fields;

    /**
     * 获取对象
     *
     * @return
     */
    public T getObject() {
        //先创建一个实例
        T instance;
        //使用try抓取错误，若实例创建错误，直接返回null
        try {
            instance = objectClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //如果没有出现异常，遍历字段并赋值
        for (MockField field : fields) {
            //捕获异常，如果出现异常，不做处理，即为使其值为null
            try {
                field.setValue(instance);
            } catch (/*NullPointerException |*/ NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            }
        }

        //返回这个实例
        return instance;

    }


    /**
     * 构造方法
     *
     * @param objectClass
     * @param fields
     */
    public MockObject(Class<T> objectClass, MockField[] fields) {
        this.objectClass = objectClass;
        this.fields = fields;
    }
}
