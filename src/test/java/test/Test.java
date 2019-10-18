package test;

import com.forte.util.Mock;
import com.forte.util.mockbean.MockObject;

/**
 * @author ForteScarlet <[email]ForteScarlet@163.com>
 * @since JDK1.8
 **/
public class Test {
    public static void main(String[] args) throws InterruptedException {
        Mock.set(User.class);
        MockObject<User> userMockObject = Mock.get(User.class);
        userMockObject.getStream(20).forEach(System.out::println);



    }

}
