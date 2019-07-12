package com.forte.util.mockbean;

import java.util.Map;
import java.util.Optional;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2019/2/27 14:39
 */
public class MockMapObject implements MockObject<Map<String, Object>> {

    private final MockMapBean mockMapBean;

    /**
     * 返回获取结果的Optional封装类
     *
     * @return
     */
    @Override
    public Optional<Map<String, Object>> get() {
        return Optional.ofNullable(mockMapBean.getObject());
    }


    /**
     * 唯一构造
     * @param mockMapBean
     */
    public MockMapObject(MockMapBean mockMapBean){
        this.mockMapBean = mockMapBean;
    }

}
