package com.forte.util.mockbean;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 将{@link MockBean}封装并返回
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2018/12/11 16:11
 * @since JDK1.8
 **/
public class MockNormalObject<T> implements MockObject<T> {

    private final MockBean<T> mockBean;

    /**
     * 获取一个实例对象
     * @return
     */
    @Override
    public T getOne(){
        return get().orElse(null);
    }

    /**
     * 获取多个实例对象，作为list集合返回
     * @return
     */
    @Override
    public List<T> getList(int num){
        //使用多线程创建list集合
        return IntStream.range(0, num).parallel().mapToObj(i -> getOne()).collect(Collectors.toList());
    }

    /**
     * 获取多个实例对象，根据转化规则转化后作为list集合返回
     * @param num
     * @param mapper
     * @param <R>
     * @return
     */
    @Override
    public <R> List<R> getList(int num , Function<T, R> mapper){
        return IntStream.range(0, num).parallel().mapToObj(i -> mapper.apply(getOne())).collect(Collectors.toList());
    }

    /**
     * 获取多个实例对象，作为Set返回
     * @param num
     * @return
     */
    @Override
    public Set<T> getSet(int num){
        return IntStream.range(0, num).parallel().mapToObj(i -> getOne()).collect(Collectors.toSet());
    }

    /**
     * 获取多个实例对象，根据转化规则转化后作为Set返回
     * @param num
     * @param mapper
     * @param <R>
     * @return
     */
    @Override
    public <R> Set<R> getSet(int num , Function<T, R> mapper){
        return IntStream.range(0, num).parallel().mapToObj(i -> mapper.apply(getOne())).collect(Collectors.toSet());
    }


    /**
     * 获取多个实例对象，作为Map返回，需要指定Map的转化方式
     * @param keyMapper
     * @param valueMapper
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K,V> Map<K,V> getMap(int num , Function<T,K> keyMapper , Function <T,V> valueMapper){
        return IntStream.range(0, num).parallel().mapToObj(i -> getOne()).collect(Collectors.toMap(keyMapper, valueMapper));
    }


    /**
     * 返回获取结果的Optional封装类
     * @return
     */
    @Override
    public Optional<T> get(){
        return Optional.ofNullable(mockBean.getObject());
    }


    /**
     * 唯一构造
     * @param mockBean
     */
    public MockNormalObject(MockBean<T> mockBean){
        this.mockBean = mockBean;
    }
}
