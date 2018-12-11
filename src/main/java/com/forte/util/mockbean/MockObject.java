package com.forte.util.mockbean;

import java.util.ArrayList;
import java.util.List;

/**
 * 将{@link MockBean}封装并返回
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2018/12/11 16:11
 * @since JDK1.8
 **/
public class MockObject<T> {
    private final MockBean<T> mockBean;

    /**
     * 获取一个实例对象
     * @return
     */
    public T getOne(){
        return mockBean.getObject();
    }

    /**
     * 获取多个实例对象，作为list集合返回
     * @return
     */
    public List<T> getList(int num){
        List<T> list = new ArrayList<>();

        //获取多个
        for(int i = 0;i<num;i++){
            list.add(mockBean.getObject());
        }

        return list;
    }


    /**
     * 唯一构造
     * @param mockBean
     */
    public MockObject(MockBean<T> mockBean){
        this.mockBean = mockBean;
    }
}
