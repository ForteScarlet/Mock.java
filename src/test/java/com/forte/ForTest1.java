package com.forte;

import com.forte.util.utils.FieldUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * FieldUtils的测试类
 *
 * @author ForteScarlet
 */
public class ForTest1 {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        User user1 = new User();
        user1.setAge(12);
        user1.setName("张三");

        User user2 = new User();
        user2.setName("李四");
        user2.setAge(18);
        user2.setFriend(user1);

        System.out.println("——————————————get测试————————————");

        Object name = FieldUtils.objectGetter(user2, "name");
        Object age = FieldUtils.objectGetter(user2, "age");
        Object friend = FieldUtils.objectGetter(user2, "friend");
        Object friendName = FieldUtils.objectGetter(user2, "friend.name");

        System.out.println("name:" + name);
        System.out.println("age:" + age);
        System.out.println("friend:" + friend);
        System.out.println("friendName:" + friendName);

        System.out.println("——————————————set测试————————————");

        User user3 = new User();
        user3.setName("user3的名字就是user3啦！");
        user3.setAge(99);

        System.out.println("user3:" + user3);

        int a = 25;

        FieldUtils.objectSetter(user2, "name", "王五");
        FieldUtils.objectSetter(user2, "age", a);
//        FieldUtils.objectSetter(user2 , "friend" , user3);
        FieldUtils.objectSetter(user2, "friend.name", "nope");

        System.out.println("user2:" + user2);

        System.out.println("user3:" + user3);

    }

    @Test
    public void test1() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        User user1 = new User();
        user1.setName("user3的名字就是user3啦！");
        user1.setAge(99);

        FieldUtils.objectSetter(user1, "name", "王五");
        FieldUtils.objectSetter(user1, "age", 123);
        FieldUtils.objectSetter(user1, "friend.name", "nope");

        System.out.println(user1);

    }

}
