package com.forte.mockTest.TestBean;

import java.util.Arrays;
import java.util.List;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class TestBean2 {

   private Integer integer;
   private Integer[] integerArr;
   private List<Integer> integerList;


    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public Integer[] getIntegerArr() {
        return integerArr;
    }

    public void setIntegerArr(Integer[] integerArr) {
        this.integerArr = integerArr;
    }

    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }

    @Override
    public String toString() {
        return "TestBean2{" +
                "integer=" + integer +
                ", integerArr=" + Arrays.toString(integerArr) +
                ", integerList=" + integerList +
                '}';
    }
}
