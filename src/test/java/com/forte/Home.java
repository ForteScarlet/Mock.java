package com.forte;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 */
public class Home {

    private String name;

    private List<User> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Home{" +
                "name='" + name + '\'' +
                ", users=[\r\n" + users.stream().map(u -> u.toString()).collect(Collectors.joining("\r\n\t", "\t", "")) +
                "\r\n]}";
    }
}
