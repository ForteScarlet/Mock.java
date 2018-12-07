package com.forte;

import java.util.Arrays;
import java.util.List;

/**
 * @author ForteScarlet
 * <div>
 * <ul>
 * <li> ---><a href='https://gitee.com/ForteScarlet' target='_block'>码云个人地址</a></li>
 * </ul>
 * </div>
 */
public class User {

    private String name;

    private Integer age;

    private User friend;

    private List<String> places;

    private Integer[] nums;

    private Double[] doubles;

    public Double[] getDoubles() {
        return doubles;
    }

    public void setDoubles(Double[] doubles) {
        this.doubles = doubles;
    }

    public Integer[] getNums() {
        return nums;
    }

    public void setNums(Integer[] nums) {
        this.nums = nums;
    }

    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }


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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", friend=" + friend +
                ", places=" + places +
                ", nums=" + Arrays.toString(nums) +
                ", doubles=" + Arrays.toString(doubles) +
                '}';
    }
}
