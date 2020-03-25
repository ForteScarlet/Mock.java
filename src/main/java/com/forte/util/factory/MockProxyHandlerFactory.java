package com.forte.util.factory;

import com.forte.util.mockbean.MockObject;

import java.lang.reflect.InvocationHandler;
import java.util.function.BiFunction;

/**
 * Mock接口代理对象工厂的接口定义
 * @author <a href="https://github.com/ForteScarlet"> ForteScarlet </a>
 */
public interface MockProxyHandlerFactory {

    /**
     * 获取代理处理接口{@link InvocationHandler}实例
     * @param mockObjectFunction 传入一个类型和一个可能为null的name字符串，获取一个mockObject对象。如果存在name，则会尝试先用name获取
     * @return JDK动态代理所需要的代理处理器示例。
     * @see InvocationHandler
     */
    InvocationHandler getHandler(BiFunction<Class<?>, String, MockObject<?>> mockObjectFunction);

}
