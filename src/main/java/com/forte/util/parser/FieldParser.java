package com.forte.util.parser;

import com.forte.util.fieldvaluegetter.FieldValueGetter;
import com.forte.util.mockbean.MockField;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public interface FieldParser {

    /**
     * 若字段不是List集合时的解析方法
     *
     * @return 字段值获取器
     */
    FieldValueGetter parserForNotListOrArrayFieldValueGetter();


    /**
     * 若字段是集合时的解析方法
     *
     * @return 字段值获取器
     */
    FieldValueGetter parserForListFieldValueGetter();


    /**
     * 若字段是数组时的解析方法
     *
     * @return 字段值获取器
     */
    FieldValueGetter parserForArrayFieldValueGetter();


    /**
     * 获取一个假字段对象，为参数解析器准备
     * @return
     */
    MockField getMockField();


}
