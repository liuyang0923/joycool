/*
 * Created on 2007-7-30
 *
 */
package net.wxsj.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-7-30
 * 
 * 说明：
 */
public class IntUtil {
    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：根据字符串解析成数组
     * 
     * 参数及返回值说明：
     * 
     * @param str
     * @param seperator
     * @return
     */
    public static ArrayList getIntList(String str, String seperator) {
        ArrayList intList = new ArrayList();
        if (str == null) {
            return intList;
        }

        String[] ss = str.split(seperator);
        int ii;
        for (int i = 0; i < ss.length; i++) {
            ii = StringUtil.toInt(ss[i]);
            if (ii > 0) {
                intList.add(new Integer(ii));
            }
        }

        return intList;
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：根据数组得到字符串
     * 
     * 参数及返回值说明：
     * 
     * @param intList
     * @param seperator
     * @return
     */
    public static String getIntStr(ArrayList intList, String seperator) {
        Iterator itr = intList.iterator();
        StringBuffer sb = new StringBuffer();
        int i = 0;
        Integer ii;
        while (itr.hasNext()) {
            ii = (Integer) itr.next();
            if (i > 0) {
                sb.append(seperator);
            }
            sb.append(ii.intValue());
            i++;
        }

        return sb.toString();
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：增加一个数
     * 
     * 参数及返回值说明：
     * 
     * @param intList
     * @param i
     * @return
     */
    public static ArrayList addInt(ArrayList intList, int i) {
        Iterator itr = intList.iterator();
        StringBuffer sb = new StringBuffer();
        Integer ii;
        while (itr.hasNext()) {
            ii = (Integer) itr.next();
            if (ii.intValue() == i) {
                return intList;
            }
        }
        intList.add(new Integer(i));

        return intList;
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：删除一个数
     * 
     * 参数及返回值说明：
     * 
     * @param intList
     * @param i
     * @return
     */
    public static ArrayList removeInt(ArrayList intList, int i) {
        Iterator itr = intList.iterator();
        StringBuffer sb = new StringBuffer();
        Integer ii;
        while (itr.hasNext()) {
            ii = (Integer) itr.next();
            if (ii.intValue() == i) {
                intList.remove(ii);
                return intList;
            }
        }

        return intList;
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：判断是否在列表里
     * 
     * 参数及返回值说明：
     * 
     * @param intList
     * @param i
     * @return
     */
    public static boolean intInList(ArrayList intList, int i) {
        Iterator itr = intList.iterator();
        StringBuffer sb = new StringBuffer();
        Integer ii;
        while (itr.hasNext()) {
            ii = (Integer) itr.next();
            if (ii.intValue() == i) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList getIntList(String[] ss) {
        ArrayList intList = new ArrayList();
        if (ss == null) {
            return intList;
        }
       
        int ii;
        for (int i = 0; i < ss.length; i++) {
            ii = StringUtil.toInt(ss[i]);
            if (ii > 0) {
                intList.add(new Integer(ii));
            }
        }

        return intList;
    }
}
