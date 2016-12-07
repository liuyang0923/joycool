/*
 * Created on 2006-12-9
 *
 */
package net.wxsj.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import net.joycool.wap.util.StringUtil;
import net.wxsj.bean.KnifeBean;
import net.wxsj.bean.KnifeQuestionBean;

/**
 * 作者：李北金
 * 
 * 创建日期：2006-12-9
 * 
 * 说明：
 */
public class KnifeFrk {
    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-9
     * 
     * 说明：根目录
     */
    public static String resourcePath = "/home/joycool/joycool-wap/wxsj/config";

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-9
     * 
     * 说明：问题列表
     */
    public static ArrayList questionList = null;

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-9
     * 
     * 说明：取得问题列表
     * 
     * 参数及返回值说明：
     * 
     * @return
     */
    public static ArrayList getQuestionList() {
        if (questionList != null) {
            return questionList;
        }

        //载入问题
        questionList = new ArrayList();
        try {
            File questionFile = new File(resourcePath
                    + "/knifeQuestionList.txt");
            BufferedReader br = new BufferedReader(new FileReader(questionFile));
            String str = br.readLine();
            String[] ss = null;
            KnifeQuestionBean question = null;
            ArrayList answerList = null;
            while (str != null) {
                ss = str.split("\t");
                if (ss.length != 6) {
                    str = br.readLine();
                    continue;
                }

                question = new KnifeQuestionBean();
                question.setId(StringUtil.toInt(ss[0]));
                question.setQuestion(ss[1]);
                answerList = new ArrayList();
                answerList.add(ss[2]);
                answerList.add(ss[3]);
                answerList.add(ss[4]);
                question.setAnswerId(ss[5].charAt(0) - 'A');
                question.setAnswerList(answerList);

                questionList.add(question);
                str = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questionList;
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-9
     * 
     * 说明：取得一个问题
     * 
     * 参数及返回值说明：
     * 
     * @param id
     * @return
     */
    public static KnifeQuestionBean getQuestionById(int id) {
        ArrayList ql = getQuestionList();
        if (id <= 0 || id > ql.size()) {
            return null;
        }

        return (KnifeQuestionBean) ql.get(id - 1);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-9
     * 
     * 说明：取得一个军刀
     * 
     * 参数及返回值说明：
     * 
     * @param id
     * @return
     */
    public static KnifeBean getKnifeById(int id) {
        ArrayList ql = getKnifeList();
        if (id <= 0 || id > ql.size()) {
            return null;
        }

        return (KnifeBean) ql.get(id - 1);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-9
     * 
     * 说明：取得军刀列表
     * 
     * 参数及返回值说明：
     * 
     * @return
     */
    public static ArrayList getKnifeList() {
        if (knifeList != null) {
            return knifeList;
        }

        //载入问题
        knifeList = new ArrayList();
        try {
            File knifeFile = new File(resourcePath + "/knifeList.txt");
            BufferedReader br = new BufferedReader(new FileReader(knifeFile));
            String str = br.readLine();
            String[] ss = null;
            KnifeBean knife = null;
            while (str != null) {
                ss = str.split("\t");
                if (ss.length != 5) {
                    str = br.readLine();
                    continue;
                }

                knife = new KnifeBean();
                knife.setId(StringUtil.toInt(ss[0]));
                knife.setName(ss[1]);
                knife.setImage(ss[2]);
                knife.setIntroduction(ss[3]);
                knife.setPrice(ss[4]);

                knifeList.add(knife);

                str = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return knifeList;
    }

    /**
     * 作者：李北金 创建日期：2006-12-9 说明：军刀列表
     */
    public static ArrayList knifeList = null;

    public static void main(String[] args) {
        ArrayList questionList = getQuestionList();
    }
}
