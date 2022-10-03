package test;

import com.forte.util.utils.RandomUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author ForteScarlet
 */
public class RandomUtilTest {

    @Test
    public void RandomUtilToFixedTest() {
        Assert.assertEquals(RandomUtil.toFixed(0.111111111, 4), "0.1111");
        Assert.assertEquals(RandomUtil.toFixed(0.111, 4), "0.1110");
        Assert.assertEquals(RandomUtil.toFixed(114514, 4), "114514.0000");
    }

}
