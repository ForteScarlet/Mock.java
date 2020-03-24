package com.forte.util.factory;

import com.forte.util.function.ExProxyHandler;
import com.forte.util.mapper.MockProxy;
import com.forte.util.mapper.MockProxyType;
import com.forte.util.mockbean.MockObject;
import com.forte.util.utils.FieldUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * {@link MockProxyHandlerFactory}的默认实现
 *
 * @author <a href="https://github.com/ForteScarlet"> ForteScarlet </a>
 */
public class MockProxyHandlerFactoryImpl implements MockProxyHandlerFactory {
    /**
     * 获取接口代理处理器实例
     * 首先，只扫描所有的抽象方法，default方法不会代理，而是执行它自己。
     *
     * @param mockObjectFunction 传入一个类型，获取一个mockObject对象。如果是Map类型，则第二参数为map的名称，否则忽视第二参数。
     * @return 接口代理处理器实例
     */
    @Override
    public InvocationHandler getHandler(BiFunction<Class<?>, String, MockObject<?>> mockObjectFunction) {
        return new DefaultMockProxyHandler(mockObjectFunction);
    }


    /**
     * 默认的mock接口代理处理器实现
     */
    public static class DefaultMockProxyHandler implements InvocationHandler {


        /**
         * mockObject获取器
         */
        private BiFunction<Class<?>, String, MockObject<?>> mockObjectFunction;

        /**
         * 方法返回值缓存map。
         */
        private Map<Method, SimpleBean<InvocationHandler>> methodReturnCacheMap;

        /**
         * 返回值永远为null的缓存值
         */
        private final SimpleBean<InvocationHandler> nullValueCache = new SimpleBean<>((p, m, o) -> null);

        /**
         * 构造需要一个mockObject获取器
         */
        public DefaultMockProxyHandler(BiFunction<Class<?>, String, MockObject<?>> mockObjectFunction) {
            this.mockObjectFunction = mockObjectFunction;
            this.methodReturnCacheMap = new ConcurrentHashMap<>(2);
        }

        /**
         * 记录缓存
         */
        private void saveCache(Method m, InvocationHandler handler) {
            methodReturnCacheMap.put(m, new SimpleBean<>(handler));
        }


        /**
         * 记录缓存
         */
        private void saveNullCache(Method m) {
            methodReturnCacheMap.put(m, nullValueCache);
        }


        private InvocationHandler getCache(Method m) {
            final SimpleBean<InvocationHandler> supplierAtomicReference = methodReturnCacheMap.get(m);
            return supplierAtomicReference == null ? null : supplierAtomicReference.get();
        }

        /**
         * 函数接口
         *
         * @param method 第一参数
         * @param args   第二参数
         * @return 返回值
         * @throws Throwable 任意异常
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 先尝试获取缓存
            final InvocationHandler cache = getCache(method);
            if (cache != null) {
                return cache.invoke(proxy, method, args);
            }

            //如果是接口中的默认方法，使用特殊方法执行
            //代码源于网络: http://www.it1352.com/988865.html
            if (method.isDefault()) {
                Class<?> declaringClass = method.getDeclaringClass();
                Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                constructor.setAccessible(true);
                InvocationHandler defaultInvocationHandler = (p, m, o) -> constructor.
                        newInstance(declaringClass, MethodHandles.Lookup.PRIVATE).
                        unreflectSpecial(method, declaringClass).
                        bindTo(proxy).
                        invokeWithArguments(args);

                // 记录缓存
                saveCache(method, defaultInvocationHandler);
                return defaultInvocationHandler.invoke(proxy, method, args);
            }

            // 尝试获取@MockProxy注解
            final MockProxy mockProxyAnnotation = method.getAnnotation(MockProxy.class);
            boolean ignore = mockProxyAnnotation != null && mockProxyAnnotation.ignore();
            // 返回值类型
            final Class<?> returnType = method.getReturnType();

            // 如果忽略此函数，则跳过
            if (ignore) {
                return getDefaultResultAndCache(returnType, method);
            }

            // 没有忽略，进行解析

            // 准备参数
            MockProxyType proxyType = mockProxyAnnotation == null ? MockProxyType.OBJECT : mockProxyAnnotation.proxyType();
            if(proxyType == MockProxyType.OBJECT){
                // 判断一下返回值的类型，如果是数组，转化为数组类型，如果是list，转化为list类型
                if(returnType.isArray()){
                    proxyType = MockProxyType.ARRAY;
                }
                if(FieldUtils.isChild(returnType, List.class)){
                    proxyType = MockProxyType.LIST;
                }
            }


            Class<?> genericType = mockProxyAnnotation == null ? Object.class : mockProxyAnnotation.genericType();
            String name = mockProxyAnnotation == null ? null : mockProxyAnnotation.name().trim().length() == 0 ? null : mockProxyAnnotation.name().trim();
            int[] array = mockProxyAnnotation == null ? new int[]{1, 1} : mockProxyAnnotation.size();

            if (array.length == 0) {
                array = new int[]{1, 1};
            }
            if (array.length == 1) {
                array = new int[]{array[0], array[0]};
            }
            if (array.length > 2) {
                array = new int[]{array[0], array[1]};
            }


            // 要获取的mock类型
            Class<?> mockGetType = proxyType.selectTypeUse(returnType, genericType);

            final MockObject<?> mockObject = mockObjectFunction.apply(mockGetType, name);

            if (mockObject == null) {
                // 获取不到mockObject, 获取默认返回值
                return getDefaultResultAndCache(returnType, method);
            }

            // mockObject不为null，构建返回值

            final int[] finalArr = array;

            final MockProxyType finalProxyType = proxyType;

            // 构建缓存函数
            InvocationHandler proxyHandler = (p, m, o) -> finalProxyType.buildReturnType(num -> {
                if (num == 1) {
                    return new Object[]{mockObject.getOne()};
                } else if (num == 0) {
                    return new Object[0];
                } else if (num < 0) {
                    throw new IllegalArgumentException("size cannot be zero.");
                } else {
                    return mockObject.getStream(num).toArray(Object[]::new);
                }
            }, mockGetType, finalArr[0], finalArr[1]);

            saveCache(method, proxyHandler);
            return proxyHandler.invoke(proxy, method, args);
        }

        /**
         * 获取默认返回值并缓存
         *
         * @param returnType 返回值类型
         * @param method     方法
         */
        private Object getDefaultResultAndCache(Class<?> returnType, Method method) {
            final Object defaultResult = getDefaultResult(returnType);
            // 记录缓存
            if (defaultResult == null) {
                saveNullCache(method);
            } else {
                InvocationHandler ignoreHandler = (p, m, o) -> defaultResult;
                saveCache(method, ignoreHandler);
            }
            return defaultResult;
        }


        /**
         * 获取默认返回值
         *
         * @param returnType 返回值类型
         */
        private Object getDefaultResult(Class<?> returnType) {
            // char
            if (returnType.equals(char.class)) {
                return ' ';
            }
            // boolean
            if (returnType.equals(boolean.class)) {
                return false;
            }

            // 浮点型
            Class<?>[] basicFloatTypes = new Class[]{double.class, float.class};
            for (Class<?> basicFloatType : basicFloatTypes) {
                if (returnType.equals(basicFloatType)) {
                    return 0.0;
                }
            }

            // 整型
            Class<?>[] basicNumberTypes = new Class[]{byte.class, short.class, int.class, long.class};
            for (Class<?> basicNumberType : basicNumberTypes) {
                if (returnType.equals(basicNumberType)) {
                    return 0;
                }
            }

            // 其他的返回null
            return null;
        }


    }


}
