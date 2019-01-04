package com.forte.util.loader;

import java.lang.reflect.Method;

/**
 * 方法的加载结果
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date Created in 2018/12/26 17:45
 * @since JDK1.8
 **/
class MockMethodLoadResult implements BranchResult<Method> {

    /** 结果 */
    private final Method result;

    /** 如果结果为成功的结果，则为true */
    private final Boolean success;

    /** 如果success为false，则此字段记录的结果的失败原因（异常） */
    private final Exception why;


    @Override
    public Boolean isSuccess() {
        return this.success;
    }

    @Override
    public Exception why() {
        return this.why;
    }

    @Override
    public Method getResult() {
        return this.result;
    }

    /**
     * 工厂方法。获得一个成功的返回值
     * @param result    结果
     * @return  返回一个实例
     */
    public static MockMethodLoadResult success(Method result){
        return new MockMethodLoadResult(result , true , null);
    }

    /**
     * 工厂方法。获得一个失败的返回值
     * @param result    结果
     * @param why       为何失败
     * @return  返回一个实例
     */
    public static MockMethodLoadResult fail(Method result , Exception why){
        return new MockMethodLoadResult(result , false, why);
    }

    /**
     * 唯一私有构造
     * @param result    结果
     * @param success   是否成功
     * @param why       为何失败
     */
    private MockMethodLoadResult(Method result, Boolean success, Exception why){
        this.result = result;
        this.success = success;
        this.why = why;
    }

}
