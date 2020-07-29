package com.forte.util.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * <p>
 *      随机数据助手,可能会用到的所有随机方法<br>
 *      此类所有的方法，只要在方法名前加上'@' 即可在Mock中作为映射指令
 *      例如:
 *      <p>
 *        <code>
 *            <p>map.put("name" , "@cname");</p>
 *            <p>map.put("age" , "@age");</p>
 *            <p>map.put("place" , "@ctitle(2,5)");</p>
 *        </code>
 *      </p>
 * </p>
 *
 *  <p>※以下列表仅供参考，一切以方法内实际参数为准，此注释有些情况可能未及时更新。</p>
 *
 * <p><strong>--名称、title等相关</strong></p>
 * <ul>
 * <li>
 * {@link MockUtil#cname()}<em>获取一个中文姓名</em>
 * </li>
 * <li>
 * {@link MockUtil#cnames(Integer, Integer)}<em>获取指定数量区间[min , max]个随机中文名</em>
 * </li>
 * <li>
 * {@link MockUtil#cnames(Integer)}<em>获取指定数量个随机中文名</em>
 * </li>
 * <li>
 * {@link MockUtil#name()}<em>获取一个英文姓名</em>
 * </li>
 * <li>
 * {@link MockUtil#names(Integer, Integer)}<em>获取指定数量区间[min, max]个随机英文姓名</em>
 * </li>
 * <li>
 * {@link MockUtil#names(Integer)}<em>获取指定数量num个随机英文姓名</em>
 * </li>
 * <li>
 * {@link MockUtil#ctitle()}<em>获取一段中文，3-5</em>
 * </li>
 * <li>
 * {@link MockUtil#ctitle(Integer)}<em>获取一段指定长度的中文</em>
 * </li>
 * <li>
 * {@link MockUtil#ctitle(Integer)}<em>获取指定数量区间个随机汉字，区间[min,max]</em>
 * </li>
 * <li>
 * {@link MockUtil#title()}<em>获取一段指定长度的英文5-10</em>
 * </li>
 * <li>
 * {@link MockUtil#title(Integer)}<em>获取一段指定长度的英文</em>
 * </li>
 * <li>
 * {@link MockUtil#title(Integer)}<em>获取指定数量区间个随机英文，区间[min,max]</em>
 * </li>
 * <li>
 * {@link MockUtil#UUID()}<em>获取一个UUID</em>
 * </li>
 * </ul>
 *
 * <p><strong>--date相关</strong></p>
 * <ul>
 * <li>
 * {@link MockUtil#date()}<em>获取随机日期 1990 - 现在</em>
 * </li>
 * <li>
 * {@link MockUtil#toDateStr()}<em>返回一个日随机日期的字符串</em>
 * </li>
 * <li>
 * {@link MockUtil#time(String)}<em>返回一个随机时间的字符串</em>
 * </li>
 * <li>
 * {@link MockUtil#time()}<em>返回一个随机时间的字符串，格式为HH:mm:ss</em>
 * </li>
 * <li>
 * {@link MockUtil#toDateTime(String)}<em>返回一个随机时间日期的字符串</em>
 * </li>
 * <li>
 * {@link MockUtil#toDateTime()}<em>返回一个随机日期时间的字符串，格式为yyyy-dd-MM HH:mm:ss</em>
 * </li>
 * </ul>
 *
 * <p><strong>--number相关</strong></p>
 * <ul>
 * <li>
 * {@link MockUtil#age()}<em>获取一个随机年龄 12-80</em>
 * </li>
 * <li>
 * {@link MockUtil#integer()}<em>获取随机数字 0-9</em>
 * </li>
 * <li>
 * {@link MockUtil#integer(Integer)}<em>获取指定长度的随机数,※不可超过int最大上限</em>
 * </li>
 * <li>
 * {@link MockUtil#integer(Integer, Integer)}<em>获取指定区间[a,b]的随机数,※不可超过int最大上限</em>
 * </li>
 * <li>
 * {@link MockUtil#doubles(Integer, Integer, Integer, Integer)}<em>获取指定区间[a,b]的小数，指定小数位数[endL,endR]，double类型</em>
 * </li>
 * <li>
 * {@link MockUtil#doubles(Integer, Integer, Integer)}<em>获取指定区间[a,b]的小数，指定小数位数[end]，double类型</em>
 * </li>
 * <li>
 * {@link MockUtil#doubles(Integer, Integer)}<em>获取指定区间[a,b]的小数，默认小数位数为0，double类型</em>
 * </li>
 * <li>
 * {@link MockUtil#doubles(Integer)}<em>获取指定数值为a的小数，默认小数位数为0，double类型</em>
 * </li>
 * <li>
 * {@link MockUtil#UUNUM()}<em>获取一个32位的随机数字</em>
 * </li>
 * <li>
 * {@link MockUtil#getNumber(Integer)}<em>获取任意长度的随机整数</em>
 * </li>
 * <li>
 * {@link MockUtil#getDouble(Integer, Integer)}<em>获取指定位的小数</em>
 * </li>
 * <li>
 * {@link MockUtil#getDouble(Integer, Integer, Integer, Integer)}<em>获取指定位的小数</em>
 * </li>
 * <li>
 * {@link MockUtil#UUDOUBLE()}<em>获取32位小数，小数为2位</em>
 * </li>
 * </ul>
 * <p><strong>--String character相关</strong></p>
 * <ul>
 * <li>
 * {@link MockUtil#bool()}<em>返回一个随机布尔值</em>
 * </li>
 * <li>
 * {@link MockUtil#bool(double)}<em>根据概率返回布尔值</em>
 * </li>
 * </ul>
 *
 * <p><strong>--String character相关</strong></p>
 * <ul>
 * <li>
 * {@link MockUtil#character()}<em>获取一个随机字符</em>
 * </li>
 * <li>
 * {@link MockUtil#character(Character[]...)}<em>在提供的字符字典（数组中）随机 返回</em>
 * </li>
 * <li>
 * {@link MockUtil#word(Integer)}<em>返回一个随机的假单词,指定长度区间[min,max]</em>
 * </li>
 * <li>
 * {@link MockUtil#word(Integer, Integer)}<em>返回一个随机的假单词,指定长度</em>
 * </li>
 * <li>
 * {@link MockUtil#word()}<em>返回一个随机的假单词</em>
 * </li>
 * <li>
 * {@link MockUtil#cword(Integer)}<em>返回一个随机的假中文词语,指定长度区间[min,max]</em>
 * </li>
 * <li>
 * {@link MockUtil#cword(Integer)}<em>返回一个随机的假中文词语,指定长度</em>
 * </li>
 * <li>
 * {@link MockUtil#cword()}<em>返回一个随机的假中文词语</em>
 * </li>
 * </ul>
 * <p><strong>--color相关</strong></p>
 * <ul>
 * <li>
 * {@link MockUtil#color()}<em>获取一个随机颜色的16进制代码</em>
 * </li>
 * </ul>
 * <p><strong>--text相关</strong></p>
 * <ul>
 * <li>
 * {@link MockUtil#sentence(Integer, Integer)}<em>随机假英文句子,句子中的单词数量为参数的区间[min,max]</em>
 * </li>
 * <li>
 * {@link MockUtil#sentence(Integer)}<em>返回指定长度的句子</em>
 * </li>
 * <li>
 * {@link MockUtil#sentence()}<em>返回长度为12-18长度的句子</em>
 * </li>
 * <li>
 * {@link MockUtil#csentence(Integer, Integer)}<em>随机假中文句子,句子中的单词数量为参数的区间[min,max]</em>
 * </li>
 * <li>
 * {@link MockUtil#csentence(Integer)}<em>返回指定长度的中文句子</em>
 * </li>
 * <li>
 * {@link MockUtil#csentence()}<em>返回长度为5-10长度的中文句子</em>
 * </li>
 * <li>
 * {@link MockUtil#paragraph(Integer, Integer)}<em>返回一个文本，文中句子数量为参数区间[min,max]</em>
 * </li>
 * <li>
 * {@link MockUtil#paragraph(Integer)}<em>返回指定句子数量的文本</em>
 * </li>
 * <li>
 * {@link MockUtil#paragraph()}<em>返回一个有3-7个句子的文本</em>
 * </li>
 * <li>
 * {@link MockUtil#cparagraph(Integer, Integer)}<em>返回一个文本，文中句子数量为参数区间[min,max]</em>
 * </li>
 * <li>
 * {@link MockUtil#cparagraph(Integer)}<em>返回指定句子数量的文本</em>
 * </li>
 * <li>
 * {@link MockUtil#cparagraph()}<em>返回一个有3-7个句子的文本</em>
 * </li>
 * </ul>
 * <p><strong>--web相关</strong></p>
 * <ul>
 * <li>
 * {@link MockUtil#ip()}<em>获取一个随机IP</em>
 * </li>
 * <li>
 * {@link MockUtil#tId()}<em>获取一个随机的顶级域名</em>
 * </li>
 * <li>
 * {@link MockUtil#email(String, String)}<em>返回一个随机邮箱,可以指定邮箱的名称（@后面的名字）和顶级域名</em>
 * </li>
 * <li>
 * {@link MockUtil#email(String)}<em>返回一个随机邮箱,可以指定邮箱的名称（@后面的名字）</em>
 * </li>
 * <li>
 * {@link MockUtil#email()}<em>返回一个随机邮箱</em>
 * </li>
 * <li>
 * {@link MockUtil#domain(String)}<em>随机生成一个域名，可指定顶级域名</em>
 * </li>
 * <li>
 * {@link MockUtil#domain()}<em>随机生成一个域名</em>
 * </li>
 * <li>
 * {@link MockUtil#url(String)}<em>随机一个url路径，可指定域名</em>
 * </li>
 * <li>
 * {@link MockUtil#url()}<em>随机一个url</em>
 * </li>
 * </ul>
 *
 * 注意：此类在进行方法重载的时候不应出现参数数量相同的重载方法
 *
 * @author ForteScarlet
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class MockUtil {


    /* —————————— 默认参数 ———————————— */
    /**
     * {@link #date()}默认使用的格式化参数
     */
    private static final String DATE_FORMAT;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT;


    /**
     * {@link #time()}默认使用的格式化参数
     */
    private static final String TIME_FORMAT;

    private static final SimpleDateFormat SIMPLE_DATETIME_FORMAT;

    /**
     * {@link #toDateTime()}默认使用的格式化参数
     */
    private static final String DATETIME_FORMAT;

    private static final SimpleDateFormat SIMPLE_TIME_FORMAT;

    /**
     * 顶级域名合集
     */
    private static final String[] DOMAINS;


    //静态代码块加载资源
    static {
        // 加载定义域名合集
        String domainStr = "top,xyz,xin,vip,win,red,net,org,wang,gov,edu,mil,biz,name,info,mobi,pro,travel,club,museum,int,aero,post,rec,asia";
        DOMAINS = domainStr.split(",");

        // 日期格式化
        DATE_FORMAT = "yyyy-dd-MM";
        SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT);

        DATETIME_FORMAT = "yyyy-dd-MM HH:mm:ss";
        SIMPLE_DATETIME_FORMAT = new SimpleDateFormat(DATE_FORMAT);

        TIME_FORMAT = "HH:mm:ss";
        SIMPLE_TIME_FORMAT = new SimpleDateFormat(DATE_FORMAT);

    }


    /* —————————— name/chinese/cname —————————— */

    /**
     * 获取一个随机中文名称
     */
    public static String cname() {
        return ChineseUtil.getName();
    }


    /**
     * 获取指定数量区间[min , max]个随机中文名
     *
     * @param min 最小数量
     * @param max 最大数量
     * @return
     */
    public static String[] cnames(Integer min, Integer max) {
        //获取随机数量
        int num = RandomUtil.getNumber$right(min, max);
        String[] names = new String[num];
        //遍历并获取
        for (int i = 0; i < num; i++) {
            names[i] = cname();
        }
        //返回结果
        return names;
    }

    /**
     * 获取指定数量个随机中文名
     *
     * @return
     */
    public static String[] cnames(Integer num) {
        return cnames(num, num);
    }

    /**
     * 随机获取一个中文姓氏 - 百家姓中获取
     */
    public static String cfirstName() {
        return ChineseUtil.getFamilyName();
    }

    /**
     * 获取一个随机英文姓名-两个开头大写的英文字母(title(2,7)+" "+title(2,7))
     */
    public static String name() {
        int min = 2, max = 7;
        return title(min, max) + " " + title(min, max);
    }

    /**
     * 获取指定数量区间[min, max]个随机英文姓名
     *
     * @param min 最少数量
     * @param max 最大数量
     * @return
     */
    public static String[] names(Integer min, Integer max) {
        //获取随机数量
        int num = RandomUtil.getNumber$right(min, max);
        String[] names = new String[num];
        //遍历并获取
        for (int i = 0; i < num; i++) {
            names[i] = name();
        }
        //返回结果
        return names;
    }

    /**
     * 获取指定数量num个随机英文姓名
     *
     * @param num 获取数量
     * @return
     */
    public static String[] names(Integer num) {
        return names(num, num);
    }

    /**
     * 获取3-5个随机汉字
     */
    public static String ctitle() {
        return ctitle(3, 5);
    }


    /**
     * 获取指定数量个随机汉字
     *
     * @param num
     */
    public static String ctitle(Integer num) {
        return ChineseUtil.getChinese(num);
    }

    /**
     * 获取指定数量区间个随机汉字，区间[min,max]
     *
     * @param min 最少数量
     * @param max 最大数量
     */
    public static String ctitle(Integer min, Integer max) {
        return ChineseUtil.getChinese(RandomUtil.getNumber$right(min, max));
    }


    /**
     * 获取5-10长度的英文字符串，开头大写
     */
    public static String title() {
        return title(5, 10);
    }

    /**
     * 获取指定长度的英文字符串，开头大写
     *
     * @param num
     */
    public static String title(Integer num) {
        return title(num, num);
    }

    /**
     * 获取指定长度的英文字符串，开头大写
     *
     * @param min 最小长度
     * @param max 最大长度
     */
    public static String title(Integer min, Integer max) {
        int num = RandomUtil.getNumber$right(min, max);
        String title = RandomUtil.getRandomString(num, false);
        //全部小写，开头大写
        return FieldUtils.headUpper(title);
    }

    /**
     * 获取一个UUID
     */
    public static String UUID() {
        return RandomUtil.getUUID();
    }



    /* —————————— date —————————— */

    /**
     * 获取随机日期
     * 时间：1990 - 现在
     */
    public static Date date() {
        Calendar calendar = Calendar.getInstance();
        //设置年份等参数
        int nowYear = calendar.get(Calendar.YEAR);
        int nowDay = calendar.get(Calendar.DAY_OF_YEAR);

        //设置随机年份
        calendar.set(Calendar.YEAR, RandomUtil.getNumber$right(1990, nowYear));
        //设置随机日期
        calendar.set(Calendar.DAY_OF_YEAR, RandomUtil.getNumber$right(1, nowDay));
        //设置随机小时
        calendar.set(Calendar.HOUR_OF_DAY, RandomUtil.getNumber$right(1, 24));
        //设置随机分钟
        calendar.set(Calendar.MINUTE, RandomUtil.getNumber$right(1, 60));
        //设置随机秒
        calendar.set(Calendar.SECOND, RandomUtil.getNumber$right(1, 60));

        //返回随机日期
        return calendar.getTime();
    }

    /**
     * 返回一个随机日期的字符串
     *
     * @param format
     */
    public static String toDateStr(String format) {
        return new SimpleDateFormat(format).format(date());
    }

    /**
     * 返回一个随机日期的字符串，格式为yyyy-dd-MM
     */
    public static String toDateStr() {
        return SIMPLE_DATE_FORMAT.format(date());
    }

    /**
     * 返回一个随机时间的字符串
     *
     * @param format
     */
    public static String time(String format) {
        return new SimpleDateFormat(format).format(date());
    }

    /**
     * 返回一个随机时间的字符串，格式为HH:mm:ss
     */
    public static String time() {
        return SIMPLE_TIME_FORMAT.format(date());
    }

    /**
     * 返回一个随机时间日期的字符串
     *
     * @param format
     */
    public static String toDateTime(String format) {
        return new SimpleDateFormat(format).format(date());
    }

    /**
     * 返回一个随机日期时间的字符串，格式为yyyy-dd-MM HH:mm:ss
     */
    public static String toDateTime() {
        return SIMPLE_DATETIME_FORMAT.format(date());
    }

    /* —————————— number age —————————— */

    /**
     * 获取一个随机年龄
     * 12 - 80
     */
    public static Integer age() {
        return RandomUtil.getNumber$right(12, 80);
    }

    /**
     * 获取随机数字
     * 0-9
     */
    public static Integer integer() {
        return RandomUtil.getNumber(1);
    }

    /**
     * 获取指定长度的随机数
     *
     * @param length 长度,长度请不要超过整数型上限。<br> 如果需要获取无限长度的整数请使用{@link MockUtil#getNumber(Integer)}
     */
    public static Integer integer(Integer length) {
        return RandomUtil.getNumber(length);
    }

    /**
     * 获取指定区间[a,b]的随机数
     *
     * @param a 最小值
     * @param b 最大值
     * @return
     */
    public static Integer integer(Integer a, Integer b) {
        return RandomUtil.getNumber$right(a, b);
    }


    /**
     * 获取制定区间[a,b]的小数，指定小数位数[endL,endR]，double类型
     *
     * @param a    整数部分的最小值
     * @param b    整数部分的最大值
     * @param endL 小数部分位数最小值
     * @param endR 小数部分位数最大值
     * @return
     */
    public static Double doubles(Integer a, Integer b, Integer endL, Integer endR) {
        int integer = integer(a, b);
        //获取小数位数值
        int end = RandomUtil.getNumber$right(endL, endR);
        double dou = Double.parseDouble(RandomUtil.toFixed(RandomUtil.getRandom().nextDouble(), end));
        return integer + dou;
    }

    /**
     * 获取指定区间[a,b]的小数，指定小数位数[end]，double类型
     *
     * @param a
     * @param b
     * @param end
     * @return
     */
    public static Double doubles(Integer a, Integer b, Integer end) {
        return doubles(a, b, end, end);
    }

    /**
     * 获取指定区间[a,b]的小数，默认小数位数为0，double类型
     *
     * @param a
     * @param b
     * @return
     */
    public static Double doubles(Integer a, Integer b) {
        return doubles(a, b, 0, 0);
    }

    /**
     * 获取指定数值为a的小数，默认小数位数为0，double类型
     *
     * @param a
     * @return
     */
    public static Double doubles(Integer a) {
        return a * 1.0;
    }


    /**
     * 获取一个32位的随机数字
     */
    public static String UUNUM() {
        int length = 32;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(integer());
        }
        return sb.toString();
    }

    /**
     * 获取任意长度的随机整数
     *
     * @param length
     */
    public static String getNumber(Integer length) {
        return getNumber(length, length);
    }

    /**
     * 获取任意长度的随机整数
     *
     * @param min 最小长度
     * @param max 最大长度
     */
    public static String getNumber(Integer min, Integer max) {
        //获取长度
        int length = RandomUtil.getNumber$right(min, max);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(integer());
        }
        return sb.toString();
    }


    /**
     * 获取指定位的小数
     *
     * @param intLength 整数部分的长度
     * @param douLength 保留小数位数
     */
    public static String getDouble(Integer intLength, Integer douLength) {
        return getDouble(intLength, intLength, douLength, douLength);
    }


    /**
     * 获取指定位的小数的最大区间
     *
     * @param intMinLength 整数部分的长度最小值
     * @param intMaxLength 整数部分的长度最大值
     * @param douMinLength 保留小数位数最小值
     * @param douMaxLength 保留小数位数最大值
     */
    public static String getDouble(Integer intMinLength, Integer intMaxLength, Integer douMinLength, Integer douMaxLength) {
        //先获取整数位
        return getNumber(intMinLength, intMaxLength) +
                "." +
                getNumber(douMinLength, douMaxLength);
    }


    /**
     * 获取32位小数，小数为2位
     */
    public static String UUDOUBLE() {
        return getDouble(32, 2);
    }



    /* ——————————————————————String character code—————————————————————— */

    /**
     * 获取一个随机字符
     */
    public static Character character() {
        return RandomUtil.getRandomChar();
    }

    /**
     * 在提供的字典（数组中）随机 返回
     *
     * @param dic
     */
    public static Character character(Character[]... dic) {
        //合并集合
        Character[] characters = Arrays.stream(dic).flatMap(Arrays::stream).toArray(Character[]::new);
        return characters[RandomUtil.getNumber(characters.length)];
    }

    /**
     * 返回一个随机的假单词
     */
    public static String word() {
        return word(3, 12);
    }

    /**
     * 返回一个随机的假单词,指定长度
     *
     * @param length 指定长度
     */
    public static String word(Integer length) {
        return RandomUtil.getRandomString(length, false);
    }

    /**
     * 返回一个随机的假单词,指定长度区间[min,max]
     *
     * @param min 最小长度
     * @param max 最大长度
     */
    public static String word(Integer min, Integer max) {
        int num = RandomUtil.getNumber$right(min, max);
        return RandomUtil.getRandomString(num, false);
    }

    /**
     * 返回一个随机的假中文词语，指定长度区间[min,max]
     *
     * @param min 最小长度
     * @param max 最大长度
     */
    public static String cword(Integer min, Integer max) {
        return ctitle(min, max);
    }

    /**
     * 返回一个随机的假中文词语，指定长度
     *
     * @param length 单词长度
     */
    public static String cword(Integer length) {
        return ctitle(length);
    }

    /**
     * 返回一个随机的假中文词语,长度2-4
     */
    public static String cword() {
        return ctitle(2, 4);
    }


    /* —————————————————————— color —————————————————————— */

    /**
     * 获取一个随机颜色的16进制代码
     */
    public static String color() {
        return RandomUtil.randomColor$hexString();
    }

    /* —————————————————————— boolean —————————————————————— */

    /**
     * 返回一个随机布尔值
     */
    public static Boolean bool() {
        return RandomUtil.getRandom().nextBoolean();
    }

    /**
     * 根据概率返回布尔值
     *
     * @param prob 返回true的概率，建议取值区间：0-1
     */
    public static Boolean bool(double prob) {
        return RandomUtil.getProbability(prob);
    }

    /* —————————————————————— text —————————————————————— */

    /**
     * 随机假英文句子,句子中的单词数量为参数的区间[min,max]
     *
     * @param min 单词最少数量
     * @param max 单词最多数量
     */
    public static String sentence(Integer min, Integer max) {
        int num = RandomUtil.getNumber$right(min, max);
        StringBuilder sb = new StringBuilder(num);
        for (int i = 1; i <= num; i++) {
            //首句子字母大写
            sb.append(i == 0 ? FieldUtils.headUpper(word()) : word());
            if (i != num) {
                sb.append(' ');
            } else {
                //30%概率为！结尾
                if (RandomUtil.getProbability(0.3)) {
                    sb.append("! ");
                    //否则30%概率？结尾
                } else if (RandomUtil.getProbability(0.3)) {
                    sb.append("? ");
                    //否则。结尾
                } else {
                    sb.append(". ");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 返回指定长度的句子
     *
     * @param length
     */
    public static String sentence(Integer length) {
        return sentence(length, length);
    }

    /**
     * 返回长度为12-18长度的句子
     */
    public static String sentence() {
        return sentence(12, 18);
    }

    /**
     * 随机假中文句子,句子中的单词数量为参数的区间[min,max]
     *
     * @param min 单词最少数量
     * @param max 单词最多数量
     */
    public static String csentence(Integer min, Integer max) {
        StringBuilder sb = new StringBuilder();
        int num = RandomUtil.getNumber$right(min, max);
        for (int i = 1; i <= num; i++) {
            //首句子字母大写
            sb.append(cword());
            if (i == num) {
                //30%概率为！结尾
                if (RandomUtil.getProbability(0.3)) {
                    sb.append("！");
                    //否则30%概率？结尾
                } else if (RandomUtil.getProbability(0.3)) {
                    sb.append("？");
                    //否则。结尾
                } else {
                    sb.append("。");
                }
            }
        }
        return sb.toString();
    }


    /**
     * 返回指定长度的中文句子
     *
     * @param length
     */
    public static String csentence(Integer length) {
        return csentence(length, length);
    }

    /**
     * 返回长度为5-10长度的中文句子
     */
    public static String csentence() {
        return csentence(5, 10);
    }

    /**
     * 返回一个文本，文中句子数量为参数区间[min,max]
     *
     * @param min
     * @param max
     */
    public static String paragraph(Integer min, Integer max) {
        int num = RandomUtil.getNumber$right(min, max);
        StringBuilder sb = new StringBuilder(num);
        for (int i = 1; i <= num; i++) {
            sb.append(sentence());
        }
        return sb.toString();
    }

    /**
     * 返回指定句子数量的文本
     *
     * @param length
     */
    public static String paragraph(Integer length) {
        return paragraph(length, length);
    }

    /**
     * 返回一个有3-7个句子的文本
     */
    public static String paragraph() {
        return paragraph(3, 7);
    }

    /**
     * 返回一个文本，文中句子数量为参数区间[min,max]
     *
     * @param min 最小数量
     * @param max 最大数量
     */
    public static String cparagraph(Integer min, Integer max) {
        int num = RandomUtil.getNumber$right(min, max);
        StringBuilder sb = new StringBuilder(num);
        for (int i = 1; i <= num; i++) {
            sb.append(csentence());
        }
        return sb.toString();
    }

    /**
     * 返回指定句子数量的文本
     *
     * @param length
     */
    public static String cparagraph(Integer length) {
        return cparagraph(length, length);
    }

    /**
     * 返回一个有3-7个句子的文本
     */
    public static String cparagraph() {
        return cparagraph(3, 7);
    }


    /* —————————————————————— web —————————————————————— */

    /**
     * 获取一个随机IP
     */
    public static String ip() {
        Random random = RandomUtil.getRandom();
        return (random.nextInt(255) + 1) +
                "." +
                (random.nextInt(255) + 1) +
                '.' +
                (random.nextInt(255) + 1) +
                '.' +
                (random.nextInt(255) + 1);
    }

    /**
     * 获取一个随机的顶级域名
     */
    public static String tId() {
        return RandomUtil.getRandomElement(DOMAINS);
    }

    /**
     * 返回一个随机邮箱,可以指定邮箱的名称（@后面的名字）和顶级域名
     */
    public static String email(String emailName, String tid) {

        return word() +
                '@' +
                emailName +
                '.' +
                tid;
    }

    /**
     * 返回一个随机邮箱,可以指定邮箱的名称（@后面的名字）
     */
    public static String email(String emailName) {
        return email(emailName, tId());
    }

    /**
     * 返回一个随机邮箱
     */
    public static String email() {
        return email(word(), tId());
    }

    /**
     * 随机生成一个域名，可指定顶级域名
     *
     * @param tid 指定顶级域名
     */
    public static String domain(String tid) {
        if (RandomUtil.getRandom().nextBoolean()) {
            return "www." + word() + "." + tid;
        }
        return word() + '.' + tid;
    }

    /**
     * 随机生成一个域名
     */
    public static String domain() {
        return domain(tId());
    }

    /**
     * 随机一个url路径，可指定域名
     *
     * @param domainName 指定域名
     */
    public static String url(String domainName) {
        StringBuilder sb = new StringBuilder(32);
        //url前半部分
        sb.append("http://").append(domainName).append('/').append(word());
        //每次有0.2的概率再追加一层路径
        while (RandomUtil.getProbability(0.2)) {
            sb.append('/').append(word());
        }
        return sb.toString();
    }

    /**
     * 随机一个url
     */
    public static String url() {
        return url(domain());
    }


    /* —————————————————————— 构造 —————————————————————— */

    /**
     * 构造私有化
     */
    private MockUtil() { }


}
