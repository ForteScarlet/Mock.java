package com.forte.util.mockbean;

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

        Map<String, Object> map = new LinkedHashMap<>();

        for (MockField field : fields) {
            map.merge(field.getFieldName(), field.getValue(), (old, val) -> throwingMerger());
        }

        return map;

//        return Arrays.stream(fields)
//                .map(f -> new AbstractMap.SimpleEntry<>(f.getFieldName(), f.getValue()))
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey, Map.Entry::getValue,
//                        throwingMerger() , LinkedHashMap::new
//                ));
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
    @SuppressWarnings("JavadocReference")
    protected static <T> BinaryOperator<T> throwingMerger() {
        return (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
    }

}
