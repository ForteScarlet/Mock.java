package com.forte.util.parser;

import com.forte.util.fieldvaluegetter.FieldValueGetter;
import com.forte.util.invoker.Invoker;
import com.forte.util.mockbean.MockObject;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Mock数据类型字段解析器
 *
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
class MockObjectParser extends BaseFieldParser {


    /**
     * 参数中的默认值
     */
    private Supplier<MockObject<?>> defaultValueSupplier;

    private AtomicReference<Supplier<Object>> fieldValueGetter = new AtomicReference<>(null);

    /**
     * 当字段类型既不是集合也不是数组的时候
     */
    @Override
    public FieldValueGetter parserForNotListOrArrayFieldValueGetter() {
        // 如果字段类型为Object且存在左区间参数，则认为这是一个List类型
        boolean isList = this.fieldClass.equals(Object.class) && (intervalMin != null);

        if (isList) {
            return parserForListFieldValueGetter();
        }
        //直接获取一个默认值参数获取器
        return () -> getObjectValue().get();
    }

    /**
     * 如果字段类型是集合类型
     *
     * @return
     */
    @Override
    public FieldValueGetter parserForListFieldValueGetter() {
        //使用集合字段类型获取器
        return getListFieldValueGetter(new Invoker[]{() -> getObjectValue().get()}, getIntervalData(), getIntervalDoubleData());
    }

    /**
     * 如果字段类型是数组类型
     *
     * @return
     */
    @Override
    public FieldValueGetter parserForArrayFieldValueGetter() {
        //使用集合字段类型获取器
        return getArrayFieldValueGetter(new Invoker[]{() -> getObjectValue().get()}, getIntervalData());
    }

    /**
     * 得到获取objectValue的函数。
     * 初始化后，会决定最终是通过Object获取还是MockObject获取。
     */
    private Supplier<Object> getObjectValue(){
        return fieldValueGetter.updateAndGet(old -> {
            if (old != null) {
                return old;
            }
            final MockObject<?> mockObject = defaultValueSupplier.get();
            if (mockObject == null) {
                return () -> defaultValueSupplier;
            } else {
                return mockObject::getOne;
            }
        });
    }

    /**
     * 获取区间参数区间，如果没有区间参数则返回区间[1,1]
     */
    private Integer[] getIntervalData() {
        //获取参数
        Integer min = intervalMin;
        Integer max = intervalMax;

        //判断区间参数
        if (min == null) {
            //如果没左参数
            if (max == null) {
                //如果右参数也没有，直接返回一个[1,1]的区间
                return new Integer[]{1, 1};
            } else {
                //如果有右参数，参数同化
                min = max;
            }
        } else {
            //有左参数，判断右参数
            if (max == null) {
                //没有右参数，同化
                max = min;
            }
            //否则都有，不变
        }
        //返回结果
        return new Integer[]{min, max};
    }

    /**
     * 获取小数区间参数区间，如果没有区间参数则返回区间[1,1]
     */
    private Integer[] getIntervalDoubleData() {
        //获取参数
        Integer min = intervalDoubleMin;
        Integer max = intervalDoubleMax;

        //判断区间参数
        if (min == null) {
            //如果没左参数
            if (max == null) {
                //如果右参数也没有，直接返回一个[1,1]的区间
                return new Integer[]{1, 1};
            } else {
                //如果有右参数，参数同化
                min = max;
            }
        } else {
            //有左参数，判断右参数
            if (max == null) {
                //没有右参数，同化
                max = min;
            }
            //否则都有，不变
        }
        //返回结果
        return new Integer[]{min, max};
    }


    /**
     * 构造
     *
     * @param objectClass 最终的mock对象类型
     * @param fieldName   字段名称
     * @param intervalStr 区间参数列表
     */
    public MockObjectParser(Class objectClass, String fieldName, String intervalStr, Supplier<MockObject<?>> mockObjectSupplier) {
        super(objectClass, fieldName, intervalStr);
        //赋值
        this.defaultValueSupplier = mockObjectSupplier;
    }

}
