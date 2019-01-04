package com.forte.util.loader;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ForteScarlet <[163邮箱地址]ForteScarlet@163.com>
 * @date 2018/12/27 17:10
 */
public class MockMethodLoadReport implements LoadResults {


    private final Set<BranchResult<Method>> RESULTS;

    /**
     * 将成功加载的结果返回
     *
     * @return 成功结果
     */
    @Override
    public Set<Method> loadSuccessResults() {
        //获取成功的方法列表
        return RESULTS.stream().filter(BranchResult::isSuccess).map(BranchResult::getResult).collect(Collectors.toSet());
    }

    /**
     * 将结果作为结果返回，分为成功和失败两种结果
     * @return
     * 成功与否的结果集
     */
    @Override
    public Map<Boolean, Set<Method>> loadResults() {
        return RESULTS.stream().collect(Collectors.groupingBy(
                BranchResult::isSuccess ,
                Collectors.mapping(BranchResult::getResult , Collectors.toSet())
                ));
    }

    /**
     * 成功结果的数量
     * @return
     * 成功结果的数量
     */
    @Override
    public int successNums() {
        return (int) RESULTS.stream().filter(BranchResult::isSuccess).count();
    }

    /**
     * 失败的结果数量
     * @return  失败的结果数量
     */
    @Override
    public int failNums() {
        return (int) RESULTS.stream().filter(r -> !r.isSuccess()).count();
    }

    /**
     * 将加载错误的方法返回，并告知其失败原因
     * @return  错误的方法以及失败原因
     */
    @Override
    public Map<Method, Exception> whyFail() {
        return RESULTS.stream().filter(re -> !re.isSuccess()).collect(Collectors.toMap(BranchResult::getResult, BranchResult::why));
    }



    /**
     * 构造
     * @param results   结果集
     */
    MockMethodLoadReport(Set<BranchResult<Method>> results){
            this.RESULTS = results;
    }

}
