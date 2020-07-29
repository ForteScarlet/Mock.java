package com.forte.util.mockbean;

/**
 * 将{@link MockBean}封装并返回
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2018/12/11 16:11
 * @since JDK1.8
 **/
public class MockNormalObject<T> implements MockObject<T> {

    private final MockBean<T> mockBean;

    @Override
    public MockBean<T> getMockBean() {
        return mockBean;
    }

//    /**
//     * 返回获取结果的Optional封装类
//     * @return
//     */
//    @Override
//    public Optional<T> get(){
//       return Optional.ofNullable(mockBean.getObject());
//    }

    @Override
    public T getOne() {
        return mockBean.getObject();
    }

    /**
     * 唯一构造
     * @param mockBean
     */
    public MockNormalObject(MockBean<T> mockBean){
        this.mockBean = mockBean;
    }
}
