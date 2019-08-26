package com.forte.util.mockbean;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2019/2/27 14:33
 */
public interface MockObject<T> {


    /**
     * 返回获取结果的Optional封装类
     * @return
     */
    Optional<T> get();


    /**
     * 获取一个实例对象
     */
    default T getOne(){
        return get().orElse(null);
    }

    /**
     * 获取一个无限流
     */
    default Stream<T> getStream(){
        return Stream.iterate(getOne(), i -> getOne());
    }

    /**
     * 获取一个指定长度的流，等同于 <code>getStream().limit(limit)</code>
     * @param limit
     * @return
     */
    default Stream<T> getStream(int limit){
        return getStream().limit(limit);
    }

    /**
     * 获取一个并行无限流
     */
    default Stream<T> getParallelStream(){
        return getStream().parallel();
    }

    /**
     * 获取一个并行流
     */
    default Stream<T> getParallelStream(int limit){
        return getParallelStream().limit(limit);
    }

    /**
     * 获取多个实例对象，作为list集合返回
     */
    default List<T> getList(int num){
        return collect(num, Collectors.toList());
    }


    /**
     * 并行线程创建多个实例对象，作为list集合返回
     */
    default List<T> getListParallel(int num){
        return collectParallel(num, Collectors.toList());
    }


    /**
     * 获取多个实例对象，根据转化规则转化后作为list集合返回
     * @param num
     * @param mapper
     * @param <R>
     * @return
     */
    default <R> List<R> getList(int num , Function<T, R> mapper){
        return collect(num, mapper, Collectors.toList());
    }


    /**
     * 获取多个实例对象，使用并行线程获取并根据指定规则进行转化，作为List返回
     * @param num
     * @param mapper
     * @param <R>
     * @return
     */
    default <R> List<R> getListParallel(int num, Function<T, R> mapper){
        return collectParallel(num, mapper, Collectors.toList());
    }


    /**
     * 获取多个实例对象，作为Set返回
     * @param num
     * @return
     */
    default Set<T> getSet(int num){
        return collect(num, Collectors.toSet());
    }


    /**
     * 获取多个实例对象，使用并行流操作，作为Set返回
     * @param num
     * @return
     */
    default Set<T> getSetParallel(int num){
        return collectParallel(num, Collectors.toSet());
    }


    /**
     * 获取多个实例对象，根据转化规则转化后作为Set返回
     */
    default <R> Set<R> getSet(int num , Function<T, R> mapper){
        return collectParallel(num, mapper, Collectors.toSet());
    }


    /**
     * 获取多个实例对象，使用并行流根据转化规则进行转化后作为Set返回
     */
    default <R> Set<R> getSetParallel(int num, Function<T, R> mapper){
        return collectParallel(num, mapper, Collectors.toSet());
    }


    /**
     * 获取多个实例对象，作为Map返回，需要指定Map的转化方式
     */
    default <K, V> Map<K, V> getMap(int num , Function<T,K> keyMapper , Function <T,V> valueMapper){
        return collectToMap(num, keyMapper, valueMapper);
    }

    /**
     * 获取多个实例对象，作为Map返回，需要指定Map的转化方式
     */
    default <K, V, R> Map<K, V> getMap(int num, Collector<T, R, Map<K, V>> collector){
        return collectToMap(num, collector);
    }


    /**
     * 获取多个实例对象，作为Map返回，需要指定Map的转化方式
     */
    default <K, V> Map<K, V> getMapParallel(int num , Function<T,K> keyMapper , Function <T,V> valueMapper){
        return collectToMapParallel(num, keyMapper, valueMapper);
    }

    /**
     * 获取多个实例对象，作为Map返回，需要指定Map的转化方式
     */
    default <K, V, R> Map<K, V> getMapParallel(int num , Collector<T, R, Map<K, V>> collector){
        return collectToMapParallel(num, collector);
    }


    /**
     * 串行collect
     */
    default <R, A> R collect(int num, Collector<? super T, A, R> collector){
        return getStream(num).collect(collector);
    }

    /**
     * 带转化的串行collect
     */
    default <R, A, N> N collect(int num, Function<? super T, ? extends R> mapper, Collector<? super R, A, N> collector){
        return getStream(num).map(mapper).collect(collector);
    }

    /**
     * 串行collect toMap
     */
    default <A, K, V> Map<K, V> collectToMap(int num, Collector<? super T, A, Map<K, V>> collector){
        return getStream(num).collect(collector);
    }

    /**
     * 串行collect toMap
     */
    default <A, K, V> Map<K, V> collectToMap(int num, Function<T, K> keyFunction, Function<T, V> valueFunction){
        return getStream(num).collect(Collectors.toMap(keyFunction, valueFunction));
    }

    /**
     * 带转化的串行collect toMap
     */
    default <A, R, K, V> Map<K, V> collectToMap(int num, Function<T, R> mapper, Collector<? super R, A, Map<K, V>> collector){
        return getStream(num).map(mapper).collect(collector);
    }

    /**
     * 带转化的串行collect toMap
     */
    default <A, R, K, V> Map<K, V> collectToMap(int num, Function<T, R> mapper, Function<R, K> keyFunction, Function<R, V> valueFunction){
        return getStream(num).map(mapper).collect(Collectors.toMap(keyFunction, valueFunction));
    }


    /**
     * 并行collect
     */
    default <R, A> R collectParallel(int num, Collector<? super T, A, R> collector){
        return IntStream.range(0, num).parallel().mapToObj(i -> getOne()).collect(collector);
    }

    /**
     * 带转化的并行collect
     */
    default <R, A, N> N collectParallel(int num, Function<? super T, ? extends R> mapper, Collector<? super R, A, N> collector){
        return IntStream.range(0, num).mapToObj(i -> mapper.apply(getOne())).collect(collector);
    }

    /**
     * 并行collect
     */
    default <A, K, V> Map<K, V> collectToMapParallel(int num, Collector<? super T, A, Map<K, V>> collector){
        return IntStream.range(0, num).parallel().mapToObj(i -> getOne()).collect(collector);
    }

    /**
     * 并行collect
     */
    default <A, K, V> Map<K, V> collectToMapParallel(int num, Function<T, K> keyFunction, Function<T, V> valueFunction){
        return IntStream.range(0, num).parallel().mapToObj(i -> getOne()).collect(Collectors.toMap(keyFunction, valueFunction));
    }

    /**
     * 带转化的并行collect
     */
    default <A, R, K, V> Map<K, V> collectToMapParallel(int num, Function<? super T, ? extends R> mapper, Collector<? super R, A, Map<K, V>> collector){
        return IntStream.range(0, num).mapToObj(i -> mapper.apply(getOne())).collect(collector);
    }

    /**
     * 带转化的并行collect
     */
    default <A, R, K, V> Map<K, V> collectToMapParallel(int num, Function<? super T, ? extends R> mapper, Function<R, K> keyFunction, Function<R, V> valueFunction){
        return IntStream.range(0, num).mapToObj(i -> mapper.apply(getOne())).collect(Collectors.toMap(keyFunction, valueFunction));
    }


}
