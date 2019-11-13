package com.forte.util.mockbean;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date Created in 2019/2/14 16:52
  @since JDK1.8
 **/
public class MockMapBean extends MockBean<Map> {


    /**
     * 重写MockBean的方法，返回Map封装对象
     * @return
     */
    @Override
    public Map<String, Object> getObject(){
        //假字段集
        MockField[] fields = this.getFields();

        //使用多线程执行
        /*
            2019/10/18
            还是那句话，不是什么都用多线程就是好的，考虑优化，不在使用多线程
            下次啥时候再看见这段注释就删了
         */
//        return Arrays.stream(fields).parallel().flatMap(f -> {
        //保存字段映射
//            HashMap<String, Object> map = new HashMap<>(1);
//            map.put(f.getFieldName(), f.getValue());
//            return map.entrySet().stream();

        /*
            考虑在后续修改为LinkedMap以适应Json对象的映射转化
            不考虑了，就现在改吧
         */
        return Arrays.stream(fields)
                .map(f -> new AbstractMap.SimpleEntry<>(f.getFieldName(), f.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        throwingMerger() , LinkedHashMap::new
                ));



    }


    /**
     * 构造方法
     *
     * @param fields
     */
    public MockMapBean(MockField[] fields) {
        super(Map.class, fields);
    }

    /**
     * 此方法来自 {@link Collectors#throwingMerger()}
     * @param <T>
     * @return
     */
    private static <T> BinaryOperator<T> throwingMerger() {
        return (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
    }

}
