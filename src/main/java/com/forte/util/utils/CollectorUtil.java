package com.forte.util.utils;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 *
 * 针对{@link java.util.stream.Collector}的操作工具类
 *
 * @author ForteScarlet <ForteScarlet@163.com>
 * @date 2020/8/1
 */
public class CollectorUtil {

    /**
     * 在非stream环境下使用{@link Collector}
     * @param num           获取数量
     * @param getter        单值获取器
     * @param collector     收集器
     */
    public static <T, A, R> R collector(int num, Supplier<T> getter, Collector<? super T, A, R> collector){
        // 获取容器
        A container = collector.supplier().get();
        // 获取累加器
        BiConsumer<A, ? super T> accumulator = collector.accumulator();
        for (int i = 0; i < num; i++) {
            accumulator.accept(container, getter.get());
        }
        // 获取结果
        return collector.finisher().apply(container);
    }

    /**
     * 在非stream环境下使用{@link Collector}
     * @param num           获取数量
     * @param getter        单值获取器
     * @param mapper        转化器
     * @param collector     收集器
     */
    public static <T, A, N, R> N collector(int num, Supplier<T> getter, Function<? super T, ? extends R> mapper, Collector<? super R, A, N> collector){
        // 获取容器
        A container = collector.supplier().get();
        // 获取累加器
        BiConsumer<A, ? super R> accumulator = collector.accumulator();
        for (int i = 0; i < num; i++) {
            accumulator.accept(container, mapper.apply(getter.get()));
        }
        // 获取结果
        return collector.finisher().apply(container);
    }

}
