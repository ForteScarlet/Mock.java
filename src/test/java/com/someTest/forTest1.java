package com.someTest;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class forTest1 {

    public static void main(String[] args) {
        //default方法
        InstanceTest instanceTest = new InstanceTest();
        instanceTest.run1();

        //静态方法
        InterTest.run2();

        String a1 = "哈哈";
        String a2 = "嘻嘻嘻嘻嘻嘻嘻嘻";
        String a3 = "啊实打实大苏打";
        String a4 = "同业公会发给各个如果";
        String a5 = "窗前明月光，疑是地上霜";
        String a6 = "Java";
        String a7 = "吖3";
        String a8 = "EUREKA-集群+负载均衡";
        String a9 = "ElasticSearch";

        String[] arr = {a1,a2,a3,a4,a5,a6,a7,a8,a9};

        Integer maxLength = Stream.of(arr).max(Comparator.comparingInt(String::length)).map(String::length).get();

        for (String s:arr) {
            String format = String.format("FORMAT：[%-"+ maxLength +"s]", s);
            System.out.println(format);
        }


    }


}
