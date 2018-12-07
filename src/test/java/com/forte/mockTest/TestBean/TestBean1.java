package com.forte.mockTest.TestBean;

import java.util.Arrays;
import java.util.List;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class TestBean1 {

    private Double double1;
    private Double[] doubleArr;
    private List<Double> doubleList;


    @Override
    public String toString() {
        return "TestBean1{" +
                "double1=" + double1 +
                ", doubleArr=" + Arrays.toString(doubleArr) +
                ", doubleList=" + doubleList +
                '}';
    }

    public Double getDouble1() {
        return double1;
    }

    public void setDouble1(Double double1) {
        this.double1 = double1;
    }

    public Double[] getDoubleArr() {
        return doubleArr;
    }

    public void setDoubleArr(Double[] doubleArr) {
        this.doubleArr = doubleArr;
    }

    public List<Double> getDoubleList() {
        return doubleList;
    }

    public void setDoubleList(List<Double> doubleList) {
        this.doubleList = doubleList;
    }
}
