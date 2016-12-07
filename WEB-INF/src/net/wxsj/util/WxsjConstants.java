/*
 * Created on 2007-8-7
 *
 */
package net.wxsj.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.cache.CacheAdmin;
import net.wxsj.bean.mall.InfoBean;
import net.wxsj.service.factory.ServiceFactory;
import net.wxsj.service.infc.IMallService;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-7
 * 
 * 说明：
 */
public class WxsjConstants {
    public final static String SPECIAL_RANDOM_MALL_INFO = "%JCSP%SPECIAL_MALL_INFO";

    public static String GROUP = "mall";

    public static int TIME = 3600;

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-8-7
     * 
     * 说明：随机取得n天内的n条精华帖子
     * 
     * %JCSP%SPECIAL_MALL_INFO_天数_条数
     * 
     * 参数及返回值说明：
     * 
     * @param tag
     * @param response
     * @return
     */
    public static String getRandomMallInfo(String tag,
            HttpServletResponse response) {
        if (tag == null) {
            return "";
        }
        if (!tag.startsWith(SPECIAL_RANDOM_MALL_INFO)) {
            return "";
        }

        String[] ss = tag.split("_");
        if (ss.length != 5) {
            return "";
        }
        int days = StringUtil.toInt(ss[3]);
        int counts = StringUtil.toInt(ss[4]);
        if (days <= 0 || counts <= 0) {
            return "";
        }

        String condition = "to_days(now()) - to_days(create_datetime) <= "
                + days + " and is_jinghua = 1";
        ArrayList list = getRandList(getMallInfoList(condition, 0, -1,
                "id desc"), counts);

        if (list == null || list.size() == 0) {
            return "";
        }

        Iterator itr = list.iterator();
        StringBuffer sb = new StringBuffer();
        InfoBean info = null;
        while (itr.hasNext()) {
            info = (InfoBean) itr.next();
            sb.append("<a href=\""
                    + ("mall/info.jsp?id=" + info.getId())
                    + "\">[" + info.getInfoTypeStr1() + "]" + StringUtil.toWml(info.getName()) + "</a><br/>");
        }
        return sb.toString();
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-8-7
     * 
     * 说明：
     * 
     * 参数及返回值说明：
     * 
     * @param condition
     * @param index
     * @param count
     * @param orderBy
     * @return
     */
    public static ArrayList getMallInfoList(String condition, int index,
            int count, String orderBy) {
        String key = "getMallInfo: " + condition + "_" + index + "_" + count
                + "_" + orderBy;
        //从缓存中取
        ArrayList list = (ArrayList) CacheAdmin.getFromCache(key, GROUP, TIME);
        if (list != null) {
            return list;
        }

        IMallService service = ServiceFactory.createMallService();
        list = service.getInfoList(condition, index, count, orderBy);

        CacheAdmin.putInCache(key, list, GROUP, TIME);

        return list;
    }

    public static ArrayList getRandList(ArrayList list, int count) {
        if (list == null) {
            return list;
        }
        if (list.size() <= count) {
            count = list.size();
        }
        if (count <= 0) {
            return new ArrayList();
        }

        Random rand = new Random();
        int max = list.size();
        int i = 0;
        int index = 0;

        ArrayList newList = new ArrayList();
        Hashtable ht = new Hashtable();
        while (true) {
            index = rand.nextInt(max);
            while (ht.get("" + index) != null) {
                index = rand.nextInt(max);
            }
            newList.add(list.get(index));
            i++;
            ht.put("" + index, "");
            if (i == count) {
                return newList;
            }
        }
    }
}
