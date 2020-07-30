package test; /**
 * @author ForteScarlet(ForteScarlet @ 163.com)
 * @since JDK1.8
 **/

import com.forte.util.Mock;
import com.forte.util.MockConfiguration;
import com.forte.util.mockbean.MockObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ForteScarlet <ForteScarlet@163.com> 
 */
public class Test3 {
    public static void main(String[] args) {

        MockConfiguration.setEnableJsScriptEngine(false);

        Map<String, Object> map = getMap();

        Mock.set(SchemeImprovePointVo.class, map);
        MockObject<SchemeImprovePointVo> mockObject = Mock.get(SchemeImprovePointVo.class);
        mockObject.getOne();

        // 异步流获取列表
//        long s = System.currentTimeMillis();
//        mockObject.getListParallel(10_0000);
//        long e = System.currentTimeMillis();
//        System.out.println(e - s);
        /*
           2690 2877 2805
         */

        // 同步流获取列表
//        long s2 = System.currentTimeMillis();
//        mockObject.getList(10_0000);
//        long e2 = System.currentTimeMillis();
//        System.out.println(e2 - s2);
        /*
            3429 3793 3787 4120 4133
         */

//        // 同步流获取大量数据
//        long s3 = System.currentTimeMillis();
//        mockObject.getList(100_0000);
//        long e3 = System.currentTimeMillis();
//        System.out.println(e3 - s3);
//        /*
//            45349 39445
//         */

//        // 异步流获取大量数据
//        long s4 = System.currentTimeMillis();
//        mockObject.getListParallel(100_0000);
//        long e4 = System.currentTimeMillis();
//        System.out.println(e4 - s4);
//        /*
//            24543 24339
//         */


//        // 异步流获取大量数据
//        int size5 = 10_0000;
//        List<SchemeImprovePointVo> list = new ArrayList<>(size5);
//        MockBean<SchemeImprovePointVo> parallelMockBean = mockObject.getMockBean().parallel();
//        long s5 = System.currentTimeMillis();
//        for (int i = 0; i < size5; i++) {
//            list.add(parallelMockBean.getObject());
//        }
//        long e5 = System.currentTimeMillis();
//        System.out.println(e5 - s5);
//        /*
//            4641 4760 4742
//         */


//        // v1.9.0 优化后
//        long s6 = System.currentTimeMillis();
//        mockObject.getList(10_0000);
//        long e6 = System.currentTimeMillis();
//        System.out.println(e6 - s6);
//        /*
//            2088 1775 1818 1748 1898
//         */



        // v1.9.0 优化后
        // 同步流大量数据（100w）
        // 再大一些的数据，例如1kw，推荐使用流而不是list。
//        long s7 = System.currentTimeMillis();
//        mockObject.getList(100_0000);
//        long e7 = System.currentTimeMillis();
//        System.out.println(e7 - s7);
        /*
            15534 16139 15522
            15736 16138 15481
            15782 15995 16403
            avg: 15858.89ms | ~16s
         */

//        // v1.9.0 优化后
        // 异步流大量数据（100w）
        long s8 = System.currentTimeMillis();
        mockObject.getListParallel(100_0000);
        long e8 = System.currentTimeMillis();
        System.out.println(e8 - s8);
//        /*
//            6378 7313 7235
//            7143 6654 6634
//            7080 7203 6777
//            avg: 6935.22 | ~7s
//         */




    }


    public static Map<String, Object> getMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("weaknessId", "@integer(1, 999999)");
        map.put("schemeCode", "@string");
        map.put("schemeName", "@ctitle(4,16)");
        map.put("schemeCategoryFrequency", "@string");
        map.put("schemeCategoryContent", "@string");
        map.put("schemeCategoryPromote", "@string");
        map.put("improvePointName", "@ctitle(8,20)");
        map.put("improvePointCategoryFrequency", "@string");
        map.put("principal", "@ctitle(2,8)");
        map.put("executionSteps", "@ctitle(10,100)");
        map.put("reportingPeriod", "@ctitle(2,4)");
        map.put("reportingMaterials", "@ctitle(8,40)");
        return map;

    }

}
