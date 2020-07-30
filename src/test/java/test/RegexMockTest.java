package test;

/**
 * @author ForteScarlet <ForteScarlet@163.com>
 * @date 2020/7/30
 */
public class RegexMockTest {

    /**
     * 根据正则规则生成随机字符串
     * 如：[a-z][A-Z][0-9]{32} 生成32位包含大写小写数字的字符串
     * like @regex('[a-z][A-Z][0-9]{32}')
     *
     * TODO 目前存在的缺陷：
     *  1 导入了额外的依赖
     *  2 无法试别例如\\d、\\w等特殊字符
     * @param regex 正则规则
     */
    @Deprecated
    public static String regex(String regex) {
        Xeger xeger = Xeger.getInstance(regex);
        return xeger.generate();
    }

    /**
     * 获取指定数量num个随机字符串
     * TODO 目前存在的缺陷：
     *  1 导入了额外的依赖
     *  2 无法试别例如\\d、\\w等特殊字符
     * @param regex z正则规则
     * @param num 获取数量
     * @return
     */
    @Deprecated
    public static String[] regexs(String regex, Integer num) {
        String[] regexs = new String[num];
        for (int i = 0; i < num; i++) {
            regexs[i] = regex(regex);
        }
        return regexs;
    }
}
