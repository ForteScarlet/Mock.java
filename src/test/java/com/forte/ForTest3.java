package com.forte;

import com.forte.util.Mock;
import com.forte.util.utils.RegexUtil;
import com.forte.util.utils.FieldUtils;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author ForteScarlet
 * <div>
 * <ul>
 * <li> ---><a href='https://gitee.com/ForteScarlet' target='_block'>码云个人地址</a></li>
 * </ul>
 * </div>
 */
public class ForTest3 {

    public static void main(String[] args) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine se = manager.getEngineByName("js");
        String str = "1+2*(3+6)-5/2";
        Object result = se.eval(str);
        System.out.println(result);

        String a = "hhhhhhhh@title(2,4)asdasdasd@title(2,5)asdas";
        RegexUtil regexUtil = new RegexUtil();
        String regex = "\\@(\\w)+\\(\\d*(\\,)?\\d*\\)";

//        System.out.println(a.matches(regex));
        String[] subUtil = RegexUtil.getMatcher(a, regex).stream().toArray(String[]::new);
        System.out.println("subUtil:"+Arrays.toString(subUtil));


        String[] split = a.split(regex);
        String s = Arrays.toString(split);
        System.out.println("split:"+s);

    }

    @Test
    public void test1() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        User user1 = new User();
        user1.setName("张三");
        user1.setAge(20);

        User user2 = new User();
        user2.setName("李四");
        user2.setAge(24);
        user2.setFriend(user1);
        user1.setFriend(user2);

        Home home = new Home();
        home.setName("张三李四的家");
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        home.setUsers(list);

//        System.out.println(home);

        Field usersField = FieldUtils.getField(home, "users");

        Class c = getListGeneric(usersField);

        ArrayList newList = new ArrayList();
        newList.add(c.newInstance());
        newList.add(c.newInstance());
        newList.add(c.newInstance());
        newList.add(c.newInstance());
        newList.add(c.newInstance());

        home.setUsers(newList);

        System.out.println(home);


    }

    @Test
    public void test2() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
//        boolean child = FieldUtils.isChild(ArrayList.class, Map.class);
//        System.out.println(child);

        User user1 = new User();
        FieldUtils.objectSetter(user1 , "name" , "张三");
        FieldUtils.objectSetter(user1 , "age" , 18);
        FieldUtils.objectSetter(user1 , "friend.name" , "李四");
        FieldUtils.objectSetter(user1 , "friend.age" , 14);
        FieldUtils.objectSetter(user1 , "friend.friend" , user1);

        System.out.println(user1);
        System.out.println(user1.getFriend());

    }

    @Test
    public void test3(){
        Class<Mock> mockClass = Mock.class;
        Mock.set(User.class , new HashMap<>());
    }

    @Test
    public void test4(){
//        String a = "aaaaaa@title(1,9)aaaaaa@UUIDaaaaaa@title(3)bbb@csentence()asdasd";
//        String a = "aaaaaa";
//        boolean match = Mock.match(a);
//        System.out.println(match);

//        String[] method = Mock.getMethods(a);
//        System.out.println(Arrays.toString(method));


//        System.out.println(a.replaceAll("(\\(.*\\))" , ""));
    }


    /**
     * 获取一个list字段的泛型类型<br>
     * 这个字段必须是一个list类型的字段！
     * @param field
     * 字段
     * @return
     * @throws ClassNotFoundException
     */
    public Class getListGeneric(Field field) throws ClassNotFoundException {

        ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
        Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
        if(listActualTypeArguments.length == 0){
            //如果没有数据
            return null;
        }else if(listActualTypeArguments.length == 1){
            //如果只有一种类型
            String typeName = listActualTypeArguments[0].getTypeName();
            return Class.forName(typeName);
        }else{
            //如果多个类型，直接返回Object类型
            return Object.class;
        }
    }

}
