package com.forte;

import com.forte.util.utils.FieldUtils;
import com.forte.util.utils.MethodUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class ForTest5 {


    public static void main(String[] args) throws Exception {

        Object[] arr = {1, 2, 3, 4, 5};

        User user = new User();


        MethodUtil.invoke(user, new Object[]{arr}, "setNums");

        System.out.println(Arrays.toString(user.getNums()));


    }


}



