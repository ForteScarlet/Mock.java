package com.forte.mockTest.TestBean;

import com.forte.User;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class TestBean3 {

    private String name = "初始名字";

    private User user;

    @Override
    public String toString() {
        return "TestBean3{" +
                "name='" + name + '\'' +
                ", user=" + user +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
