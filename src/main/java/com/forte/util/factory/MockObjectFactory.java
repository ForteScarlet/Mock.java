package com.forte.util.factory;

import com.forte.util.mockbean.*;

/**
 * MockObject对象工厂
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2019/2/27 14:38
 */
public class MockObjectFactory {

    /**
     * 创建一个普通mock对象
     * @param mockBean
     * @param <T>
     * @return
     */
    public static <T> MockObject<T> createNormalObj(MockBean<T> mockBean){
        return new MockNormalObject<>(mockBean);
    }


    /**
     * 创建一个map类型的mock对象
     * @param mockMapBean
     * @return
     */
    public static MockMapObject createMapObj(MockMapBean mockMapBean){
        return new MockMapObject(mockMapBean);
    }

}
