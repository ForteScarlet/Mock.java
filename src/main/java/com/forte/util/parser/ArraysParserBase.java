package com.forte.util.parser;

import com.forte.util.fieldvaluegetter.FieldValueGetter;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
class ArraysParserBase extends BaseFieldParser {

    @Override
    public FieldValueGetter parserForNotListOrArrayFieldValueGetter() {
        return null;
    }

    @Override
    public FieldValueGetter parserForListFieldValueGetter() {
        return null;
    }

    @Override
    public FieldValueGetter parserForArrayFieldValueGetter() {
        return null;
    }


    /**
     * 构造
     *
     * @param objectClass
     * @param fieldName
     * @param intervalStr
     */
    public ArraysParserBase(Class objectClass, String fieldName, String intervalStr) {
        super(objectClass, fieldName, intervalStr);
    }
}
