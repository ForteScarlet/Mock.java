package com.forte.util.mockbean;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2019/2/27 14:33
 */
public interface MockObject<T> {

    /**
     * 获取一个实例对象
     * @return
     */
    public T getOne();

    /**
     * 获取多个实例对象，作为list集合返回
     * @param num
     * @return
     */
    public List<T> getList(int num);

    /**
     * 获取多个实例对象，根据转化规则转化后作为list集合返回
     * @param num
     * @param mapper
     * @param <R>
     * @return
     */
    public <R> List<R> getList(int num , Function<T, R> mapper);

    /**
     * 获取多个实例对象，作为Set返回
     * @param num
     * @return
     */
    public Set<T> getSet(int num);

    /**
     * 获取多个实例对象，根据转化规则转化后作为Set返回
     * @param num
     * @param mapper
     * @param <R>
     * @return
     */
    public <R> Set<R> getSet(int num , Function<T, R> mapper);


    /**
     * 获取多个实例对象，作为Map返回，需要指定Map的转化方式
     * @param num
     * @param keyMapper
     * @param valueMapper
     * @param <K>
     * @param <V>
     * @return
     */
    public <K,V> Map<K,V> getMap(int num , Function<T,K> keyMapper , Function <T,V> valueMapper);


    /**
     * 返回获取结果的Optional封装类
     * @return
     */
    public Optional<T> get();

}
