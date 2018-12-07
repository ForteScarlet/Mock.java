package com.forte.mockTest;

import com.forte.User;
import com.forte.util.Mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class ForTest4 {

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "@cname");
        map.put("age", "@age");
        map.put("places|1-10", "@cname");
        map.put("nums|10-20", "@integer");
        map.put("doubles|5-30" , 2.0);

        Mock.set(User.class, map);

        User user = Mock.get(User.class);
        System.out.println(user);
        System.out.println(user.getName());
        System.out.println(user.getAge());
        System.out.println(user.getPlaces()+"-"+user.getPlaces().get(0));
        System.out.println(Arrays.toString(user.getNums()) + "-" + user.getNums()[0]);
        System.out.println(Arrays.toString(user.getDoubles()) + "-" + user.getDoubles()[0]);


//        String integer1 = MockUtil.getNumber(55);
//        System.out.print(integer1+"+");
//        int integer2 = MockUtil.integer();
//        System.out.print(integer2+"=");
//
//        String str = integer1 + "+" + integer2;
//        Object eval = MethodUtil.eval(str);
//        System.out.println(eval);

//        List<Integer> list;
//        ArrayList arrayList = new ArrayList();
//
//        arrayList.add(1);
//        arrayList.add("2");
//
//        list = arrayList;
//
//        Integer s1 = list.get(0);
//        System.out.println(s1);

    }

}
