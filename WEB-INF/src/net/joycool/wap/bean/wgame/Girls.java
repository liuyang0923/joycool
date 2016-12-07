/*
 * Created on 2006-1-23
 *
 */
package net.joycool.wap.bean.wgame;

import java.util.Vector;

/**
 * @author lbj
 *  
 */
public class Girls {
    public static Vector girls;

    public static Vector getGirls() {
        if (girls == null) {
            String prefix = "http://wap.joycool.net/wgame/girlsImg";
            girls = new Vector();
            //1
            GirlBean girl = new GirlBean();
            String[] picList = new String[9];
            int i;
            girl.setId(1);
            girl.setName("混血娇娃丝丝");
            for (i = 0; i < 9; i++) {
                picList[i] = prefix + "/1/" + (i + 1) + ".gif";
            }
            girl.setPicList(picList);
            girls.add(girl);
            //2
            girl = new GirlBean();
            picList = new String[7];
            girl.setId(2);
            girl.setName("清纯宝贝佳佳");
            for (i = 0; i < 7; i++) {
                picList[i] = prefix + "/2/" + (i + 1) + ".gif";
            }
            girl.setPicList(picList);
            girls.add(girl);
            //3
            girl = new GirlBean();
            picList = new String[9];
            girl.setId(3);
            girl.setName("奔放美女露露");
            for (i = 0; i < 9; i++) {
                picList[i] = prefix + "/3/" + (i + 1) + ".gif";
            }
            girl.setPicList(picList);
            girls.add(girl);
            //4
            girl = new GirlBean();
            picList = new String[7];
            girl.setId(4);
            girl.setName("成熟少妇盈盈");
            for (i = 0; i < 7; i++) {
                picList[i] = prefix + "/4/" + (i + 1) + ".gif";
            }
            girl.setPicList(picList);
            girls.add(girl);
            //5
            girl = new GirlBean();
            picList = new String[7];
            girl.setId(5);
            girl.setName("痴情女生晴晴");
            for (i = 0; i < 7; i++) {
                picList[i] = prefix + "/5/" + (i + 1) + ".gif";
            }
            girl.setPicList(picList);
            girls.add(girl);
            //6
            girl = new GirlBean();
            picList = new String[8];
            girl.setId(6);
            girl.setName("幽怨美女琳琳");
            for (i = 0; i < 8; i++) {
                picList[i] = prefix + "/6/" + (i + 1) + ".gif";
            }
            girl.setPicList(picList);
            girls.add(girl);
            //7
            girl = new GirlBean();
            picList = new String[7];
            girl.setId(7);
            girl.setName("快乐天使珠珠");
            for (i = 0; i < 7; i++) {
                picList[i] = prefix + "/7/" + (i + 1) + ".gif";
            }
            girl.setPicList(picList);
            girls.add(girl);
            //8
            girl = new GirlBean();
            picList = new String[10];
            girl.setId(8);
            girl.setName("寂寞女孩佳佳");
            for (i = 0; i < 10; i++) {
                picList[i] = prefix + "/8/" + (i + 1) + ".gif";
            }
            girl.setPicList(picList);
            girls.add(girl);
        }

        return girls;
    }

    public static GirlBean getGirl(int id) {
        return (GirlBean) getGirls().get(id - 1);
    }
}
