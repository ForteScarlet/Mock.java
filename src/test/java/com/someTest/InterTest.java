package com.someTest;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public interface InterTest {

    default void run1(){
        System.out.println("run1");
    }

    public static void run2(){
        System.out.println("run2");
    }

    


}
