package com.forte.util.mapper;

import com.forte.util.exception.MockException;
import com.forte.util.function.ExFunction;
import com.forte.util.utils.RandomUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 代理接口对象的时候可能存在的三种类型：list类型，Array类型，和Object类型
 *
 * @author <a href="https://github.com/ForteScarlet"> ForteScarlet </a>
 */
public enum MockProxyType {

    /**
     * 未知类型。直接使用此类型将会抛出异常。
     * 但是一般来讲，默认的代理工厂内部会进行判断。
     */
    UNKNOWN(
            (mrt, grt) -> {
                throw new MockException("unknown proxy type.");
            },
            (f, num, t) -> {
                throw new MockException("unknown proxy type.");
            }
    ),

    /**
     * 其他任意Object
     * 返回值选择会选方法返回值
     * 构建返回值的时候，无视区间参数，只会传入1且只取第一个返回值。
     */
    OBJECT(
            (mrt, grt) -> mrt,
            (f, num, t) -> f.apply(1)[0]
    ) {
        @Override
        protected int getRandomNum(int min, int max) {
            return 1;
        }
    },
    /**
     * 数组类型
     */
    ARRAY(
            (mrt, grt) -> grt,
            (f, num, t) -> {
                final Object[] array = f.apply(num);
                final Object newArrayInstance = Array.newInstance(t, array.length);
                for (int i = 0; i < array.length; i++) {
                    Array.set(newArrayInstance, i, array[i]);
                }
                return newArrayInstance;
            }
    ),
    /**
     * list类型
     */
    LIST(
            (mrt, grt) -> grt,
            (f, num, t) -> {
                final Object[] array = f.apply(num);
                return new ArrayList<>(Arrays.asList(array));
            }
    );

    /**
     * 函数参数1代表方法的返回值类型，参数2代表注解上的泛型类型（如果存在）
     * 返回值代表使用哪个类型来获取泛型
     */
    private final BiFunction<Class<?>, Class<?>, Class<?>> selectUseTypeFunction;
    /**
     * 参数1代表一个函数，这个函数接收一个数量参数，返回一定数量的结果对象
     * 参数2代表具体数量
     * 参数3代表bean的类型
     * 参数4代表最终的返回结果
     */
    private final ExFunction<Function<Integer, Object[]>, Integer, Class<?>, Object> resultObjectFunction;

    MockProxyType(
            BiFunction<Class<?>, Class<?>, Class<?>> selectUseTypeFunction,
            ExFunction<Function<Integer, Object[]>, Integer, Class<?>, Object> resultObjectFunction
    ) {
        this.selectUseTypeFunction = selectUseTypeFunction;
        this.resultObjectFunction = resultObjectFunction;
    }

    /**
     * 选择使用的类型
     *
     * @param methodReturnType 方法类型
     * @param genericType      泛型类型
     * @return
     */
    public Class<?> selectTypeUse(Class<?> methodReturnType, Class<?> genericType) {
        return selectUseTypeFunction.apply(methodReturnType, genericType);
    }

    /**
     * 根据函数构建最终结果
     * @param beanGetter bean获取器
     * @param beanType   bean的类型，即mock所得类型
     * @param min
     * @param max
     * @return
     */
    public Object buildReturnType(Function<Integer, Object[]> beanGetter, Class<?> beanType, int min, int max) {
        final int randomNum = getRandomNum(min, max);
        return resultObjectFunction.apply(beanGetter, randomNum, beanType);
    }

    protected int getRandomNum(int min, int max) {
        return RandomUtil.getNumber$right(min, max);
    }

}
