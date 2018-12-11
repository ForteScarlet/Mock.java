package com.forte.mockTest.testbean;

import com.forte.User;
import org.junit.Test;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class TestUser {

    private String name;

    private Integer age;

    private Double money;

    private TestUser friend;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public TestUser getFriend() {
        return friend;
    }

    public void setFriend(TestUser friend) {
        this.friend = friend;
    }

    @Override
    public String toString() {
        return "TestUser{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", money=" + money +
                ", friend=" + friend +
                '}';
    }

}
