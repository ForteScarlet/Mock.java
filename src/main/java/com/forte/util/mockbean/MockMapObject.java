package com.forte.util.mockbean;

import java.util.Map;

/**
 *
 * Map类型的结果集合
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2019/2/27 14:39
 */
public class MockMapObject implements MockObject<Map> {

    private final MockMapBean mockMapBean;

    @Override
    public MockBean<Map> getMockBean() {
        return mockMapBean;
    }

//    /**
//     * 返回获取结果的Optional封装类
//     *
//     * @return
//     */
//    @Override
//    public Optional<Map> get() {
//        return Optional.ofNullable(mockMapBean.getObject());
//    }

    /**
     *
     * @return
     */
    @Override
    public Map getOne() {
        return mockMapBean.getObject();
    }

    /**
     * 唯一构造
     * @param mockMapBean
     */
    public MockMapObject(MockMapBean mockMapBean){
        this.mockMapBean = mockMapBean;
    }

}
