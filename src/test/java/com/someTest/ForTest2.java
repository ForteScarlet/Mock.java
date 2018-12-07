package com.someTest;

import com.forte.User;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class ForTest2 {

    public static void main(String[] args) {
        //创建一个User对象
        User user = new User();
        user.setName("name");


        Optional<User> ouser = Optional.of(user);

        Optional<User> empty = Optional.empty();




        /*
        orElse方法，如果为null则返回后面的获取值，如果不为null则返回正常的user对象
        但是，如果orElse中填写的是一个方法，则无论此对象是否为null，此方法必执行
        因为参数是作为一个对象传入的。
         */
        User orElse1 = ouser.orElse(getUser());
        System.out.println(orElse1.getName());
        User orElse2 = empty.orElse(getUser());
        System.out.println(orElse2.getName());
        /*
            执行结果：
            getUser()
            name
            getUser()
            getUser
         */


        /*
            orElseGet方法的参数是Supplier函数接口，要求返回一个User实例对象
            此方法与上一个方法不同的是，只有当Optional中的实例为null的时候才会调用Supplier中的方法
         */
        User orElse3 = ouser.orElseGet(ForTest2::getUser);
        System.out.println(orElse3.getName());
        User orElse4 = empty.orElseGet(ForTest2::getUser);
        System.out.println(orElse4.getName());
        /*
            执行结果：
            name
            getUser()
            getUser
         */
        //.orElseThrow(RuntimeException::new)
        //.ifPresent(System.out::println)
//        ouser;


        Optional<String> a = ouser.filter(u -> u.getName().equals("a")).map(User::getName);
        System.out.println(a);

    }


    /**
     * 获取一个用户
     * @return
     */
    public static User getUser(){
        System.out.println("getUser()");

        User user = new User();
        user.setName("getUser");

        return user;
    }

}
