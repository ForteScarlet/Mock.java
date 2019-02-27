package com.forte.util.mockbean;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2019/2/27 14:39
 */
public class MockMapObject implements MockObject<Map<String, Object>> {

    private final MockMapBean mockMapBean;

    /**
     * 获取一个实例对象
     *
     * @return
     */
    @Override
    public Map<String, Object> getOne() {
        return mockMapBean.getObject();
    }

    /**
     * 获取多个实例对象，作为list集合返回
     *
     * @param num
     * @return
     */
    @Override
    public List<Map<String, Object>> getList(int num) {
        return IntStream.range(0, num).parallel().mapToObj(i -> getOne()).collect(Collectors.toList());
    }

    /**
     * 获取多个实例对象，根据转化规则转化后作为list集合返回
     *
     * @param num
     * @param mapper
     * @return
     */
    @Override
    public <R> List<R> getList(int num, Function<Map<String, Object>, R> mapper) {
        return IntStream.range(0, num).parallel().mapToObj(i -> mapper.apply(getOne())).collect(Collectors.toList());
    }

    /**
     * 获取多个实例对象，作为Set返回
     *
     * @param num
     * @return
     */
    @Override
    public Set<Map<String, Object>> getSet(int num) {
        return IntStream.range(0, num).parallel().mapToObj(i -> getOne()).collect(Collectors.toSet());
    }

    /**
     * 获取多个实例对象，根据转化规则转化后作为Set返回
     *
     * @param num
     * @param mapper
     * @return
     */
    @Override
    public <R> Set<R> getSet(int num, Function<Map<String, Object>, R> mapper) {
        return IntStream.range(0, num).parallel().mapToObj(i -> mapper.apply(getOne())).collect(Collectors.toSet());
    }

    /**
     * 获取多个实例对象，作为Map返回，需要指定Map的转化方式
     *
     * @param num
     * @param keyMapper
     * @param valueMapper
     * @return
     */
    @Override
    public <K, V> Map<K, V> getMap(int num, Function<Map<String, Object>, K> keyMapper, Function<Map<String, Object>, V> valueMapper) {
        return IntStream.range(0, num).parallel().mapToObj(i -> getOne()).collect(Collectors.toMap(keyMapper, valueMapper));
    }

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
