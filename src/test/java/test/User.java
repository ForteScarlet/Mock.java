package test;

import com.forte.util.mapper.ArrayMapperType;
import com.forte.util.mapper.MockArray;
import com.forte.util.mapper.MockValue;

/**
 * @author ForteScarlet <[email]ForteScarlet@163.com>
 * @since JDK1.8
 **/
public class User {
    @MockValue("@cname")
    private String name;

    @MockArray(value = {"1", "2", "3"}, mapper = ArrayMapperType.ToInt.class)
    private int age;

    @MockArray(value = {"22", "22.5", "10"}, mapper = ArrayMapperType.ToDouble.class)
    private double doub;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getDoub() {
        return doub;
    }

    public void setDoub(double doub) {
        this.doub = doub;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", doub=" + doub +
                '}';
    }
}
