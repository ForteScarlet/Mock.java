package com.forte;

import com.forte.util.utils.MockUtil;
import com.forte.util.utils.RandomUtil;

/**
 * @author ForteScarlet
 * <div>
 * <ul>
 * <li> ---><a href='https://gitee.com/ForteScarlet' target='_block'>码云个人地址</a></li>
 * </ul>
 * </div>
 */
public class ForTest2 {
    public static void main(String[] args) {

        //随机日期测试
        for (int i = 0; i < 20; i++) {
            System.out.println(MockUtil.dateStr("yyyy-dd-MM HH:ss:mm"));
        }

        System.out.println("——————————————————————————————");

        System.out.println("title:" + MockUtil.title());
        System.out.println("ctitle:" + MockUtil.ctitle());
        System.out.println("cname:" + MockUtil.cname());
        System.out.println("character:" + MockUtil.character());

        System.out.println("——————————————————————————————");

        System.out.println("color:" + MockUtil.color());

        System.out.println("——————————————————————————————");

        int all = 1000000;
        int trueNum = 0;
        int falseNum = 0;
        double prob = 0.8;
        for (int i = 0; i < all; i++) {
            Boolean b = RandomUtil.getProbability(prob);
            if (b) trueNum++;
            else falseNum++;
        }

        System.out.println("trueprob:" + (prob * 100) + "%");
        System.out.println("all:" + all);
        System.out.println("trueNum:" + trueNum + " - " + ((trueNum * 1.0 / all) * 100) + "%");
        System.out.println("falseNum:" + falseNum + " - " + ((falseNum * 1.0 / all) * 100) + "%");

        System.out.println("——————————————————————————————");

//        System.out.println("sentence():"+MockUtil.sentence());
//        System.out.println("================================================================");
//        System.out.println("sentence(int=20):"+MockUtil.sentence(20));
//        System.out.println("================================================================");
//        System.out.println("sentence(20,30):"+MockUtil.sentence(20,30));
//
//        System.out.println("——————————————————————————————");
//
//        System.out.println("sentence():"+MockUtil.csentence());
//        System.out.println("================================================================");
//        System.out.println("sentence(int=20):"+MockUtil.csentence(20));
//        System.out.println("================================================================");
//        System.out.println("sentence(20,30):"+MockUtil.csentence(20,30));
//
//        System.out.println("——————————————————————————————");
//
//        System.out.println("paragraph():"+MockUtil.paragraph());
//        System.out.println("================================================================");
//        System.out.println("paragraph(int=4):"+MockUtil.paragraph(4));
//        System.out.println("================================================================");
//        System.out.println("paragraph(2,5):"+MockUtil.paragraph(2,5));
//
//        System.out.println("——————————————————————————————");
//
//        System.out.println("cparagraph():"+MockUtil.cparagraph());
//        System.out.println("================================================================");
//        System.out.println("cparagraph(int=4):"+MockUtil.cparagraph(4));
//        System.out.println("================================================================");
//        System.out.println("cparagraph(2,5):"+MockUtil.cparagraph(2,5));
//
//        System.out.println("——————————————————————————————");

        System.out.println("ip:" + MockUtil.ip());
        System.out.println("email(163,com):" + MockUtil.email("163", "com"));
        System.out.println("email(163):" + MockUtil.email("163"));
        System.out.println("email():" + MockUtil.email());
        System.out.println("domain(net)" + MockUtil.domain("net"));
        System.out.println("domain()" + MockUtil.domain());
        System.out.println("url(baidu.com)" + MockUtil.url("baidu.com"));
        System.out.println("url()" + MockUtil.url());

    }
}
