package com.forte.util.factory;

import com.forte.util.mockbean.MockBean;
import com.forte.util.mockbean.MockField;
import com.forte.util.mockbean.MockMapBean;

/**
 * MockBean的工厂
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2019/2/27 14:57
 */
public class MockBeanFactory {

    /**
     * 创建一个MockBean
     * @param objectClass
     * @param fields
     * @param <T>
     * @return
     */
    public static <T> MockBean<T> createMockBean(Class<T> objectClass, MockField[] fields){
        return new MockBean<>(objectClass, fields);
    }

    /**
     * 创建一个MockMapBean
     * @param fields
     * @return
     */
    public static MockMapBean createMockMapBean(MockField[] fields){
        return new MockMapBean(fields);
    }

}
