package com.forte.util.mockbean;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ForteScarlet <ForteScarlet@163.com>
 * @date 2020/7/29
 */
public class ParallelMockMapBean extends MockMapBean {

    /**
     * 重写MockBean的方法，返回Map封装对象
     * @return
     */
    @Override
    public Map<String, Object> getObject(){
        //假字段集
        MockField[] fields = this.getFields();

        return Arrays.stream(fields).parallel()
                .map(f -> new AbstractMap.SimpleEntry<>(f.getFieldName(), f.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        throwingMerger() , LinkedHashMap::new
                ));
    }


    public MockMapBean parallel(){
        return this;
    }


    public MockMapBean sequential(){
        return new MockMapBean(Arrays.copyOf(fields, fields.length));
    }

    /**
     * 构造方法
     *
     * @param fields
     */
    public ParallelMockMapBean(MockField[] fields) {
        super(fields);
    }
}
