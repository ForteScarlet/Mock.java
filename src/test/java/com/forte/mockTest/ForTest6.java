package com.forte.mockTest;

import com.forte.User;
import com.forte.mockTest.TestBean.TestBean1;
import com.forte.mockTest.TestBean.TestBean2;
import com.forte.mockTest.TestBean.TestBean3;
import com.forte.util.Mock;
import com.forte.util.utils.MockUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class ForTest6 {

    public static void main(String[] args) {
//        test1();
//        test2();
        test3();


    }



    public static void test3(){

        Map<String , Object> map = new HashMap<>();
        User user = new User();
        user.setName("user的名字");

        map.put("name" , "@cname");
        map.put("user.name" , "@cname");
        map.put("user.age" , "@age");
        map.put("user.friend.name" , "@cname");
        map.put("user.friend.age" , 25);
//        s();
        Mock.set(TestBean3.class , map);
//        e("记录时间");
        s();
        TestBean3 testBean3_1 = Mock.get(TestBean3.class);
        e("获取时间");
        System.out.println(testBean3_1);
//        s();
//        TestBean3 testBean3_2 = Mock.get(TestBean3.class);
//        e("获取时间");
//        System.out.println(testBean3_2);
//        s();
//        TestBean3 testBean3_3 = Mock.get(TestBean3.class);
//        e("获取时间");
//        System.out.println(testBean3_3);
    /*
        执行结果：
        记录时间用时：123ms
        获取时间用时：96ms
        TestBean3{name='邵聍区', user=User{name='逯严保', age=null, friend=null, places=null, nums=null, doubles=null}}
        获取时间用时：1ms
        TestBean3{name='惠俯', user=User{name='项鄙猁', age=null, friend=null, places=null, nums=null, doubles=null}}
        获取时间用时：0ms
        TestBean3{name='晋俣更', user=User{name='督蕈', age=null, friend=null, places=null, nums=null, doubles=null}}
     */


    }

    public static void test2(){
        Map<String , Object> map = new HashMap<>();
        map.put("integer|10-50" , 2);
        map.put("integerArr|2" , 2);
        map.put("integerList|2-5" , 2);

        Mock.set(TestBean2.class , map);

        TestBean2 testBean2 = Mock.get(TestBean2.class);
        System.out.println(testBean2);

    }

    public static void test1(){
        Map<String , Object> map = new HashMap<>();
        map.put("double1|10-50.2-3" , 2);
        map.put("doubleArr|2-5.2" , 2);
        map.put("doubleList|2-5" , "@doubles(10,50,2,3)");

        Mock.set(TestBean1.class , map);

        TestBean1 t = Mock.get(TestBean1.class);

        System.out.println(t);

        Double double1 = t.getDouble1();
        Double aDoubleArr0 = t.getDoubleArr()[0];
        Double aDoubleList0 = t.getDoubleList().get(0);

        System.out.println(double1);
        System.out.println(aDoubleArr0);
        System.out.println(aDoubleList0);

        Double doubles = MockUtil.doubles(10, 50, 2, 3);
        System.out.println(doubles);

    }

    private static long start;
    private static long end;

    private static void s(){
        //开始时间
        start = System.currentTimeMillis();
    }

    private static void e(){
        e("");
    }

    private static void e(String s){
        //结束时间
        end = System.currentTimeMillis();

        System.out.println(s+"用时：" + ((end - start)) + "ms");

    }


}
