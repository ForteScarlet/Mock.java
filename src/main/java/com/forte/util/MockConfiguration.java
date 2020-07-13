package com.forte.util;
import javax.script.ScriptEngine;

/**
 *
 * 这是一个静态类，你可以随时随地修改他的配置。
 * 他的一些配置会在某些地方用到。
 *
 * @author ForteScarlet <ForteScarlet@163.com> 
 */
public class MockConfiguration {

    /**
     * <p> 是否启用JS脚本执行。
     * <p> 版本1.8之后将脚本执行类{@link ScriptEngine}变更为了复用的单例形式，
     * 效率提升了约2倍多（测试生成1000条数据，优化前：14s左右，优化后：6s左右）
     * <p> 6秒还是太慢了，因此现在修改为默认情况下不开启JS脚本执行，
     * 未开启配置的情况下，任何字符串类型的值都不再会尝试进行JS脚本执行。
     * <p> 关闭脚本的效率比开启脚本的效率高15倍左右（测试生成1000条数据，开启JS：6左右，关闭JS：0.4秒左右）
     * <p>
     * <p>
     *
     */
    private static boolean enableJsScriptEngine = false;

    /**
     * 配置是否开启JS脚本执行
     */
    public static synchronized void setEnableJsScriptEngine(boolean enableJsScriptEngine){
        MockConfiguration.enableJsScriptEngine = enableJsScriptEngine;
    }

    /**
     * 获取是否开启JS脚本执行。
     * @return
     */
    public static boolean isEnableJsScriptEngine(){
        return enableJsScriptEngine;
    }





}
