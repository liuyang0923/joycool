/*
 * Created on 2007-7-27
 *
 */
package net.wxsj.framework.mall;

import java.util.ArrayList;
import java.util.Iterator;

import net.joycool.wap.cache.CacheAdmin;
import net.wxsj.bean.mall.AreaTagBean;
import net.wxsj.bean.mall.TagBean;
import net.wxsj.service.factory.ServiceFactory;
import net.wxsj.service.infc.IMallService;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-7-27
 * 
 * 说明：
 */
public class MallFrk {
    public static String SEPERATOR = ",";

    public static int OTHER_TAG = 999999;

    //分类标签
    public static String TAG_GROUP = "mall";

    public static int TAG_TIME = 3600 * 24;

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：取得所有分类标签
     * 
     * 参数及返回值说明：
     * 
     * @return
     */
    public static ArrayList getTags() {
        String key = "getTags";
        //从内存中取
        ArrayList tags = (ArrayList) CacheAdmin.getFromCache(key, TAG_GROUP,
                TAG_TIME);
        if (tags != null) {
            return tags;
        }

        //从数据库中取
        IMallService service = ServiceFactory.createMallService();
        tags = service.getTagList("id > 0", 0, -1, "display_order, id");

        //放入缓存
        CacheAdmin.putInCache(key, tags, TAG_GROUP, TAG_TIME);

        return tags;
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：取得热门标签
     * 
     * 参数及返回值说明：
     * 
     * @return
     */
    public static ArrayList getHotTags() {
        String key = "getHotTags";
        //从内存中取
        ArrayList tags = (ArrayList) CacheAdmin.getFromCache(key, TAG_GROUP,
                TAG_TIME);
        if (tags != null) {
            return tags;
        }

        //从数据库中取
        IMallService service = ServiceFactory.createMallService();
        tags = service.getTagList("mark = " + TagBean.HOT, 0, -1,
                "display_order, id");

        //放入缓存
        CacheAdmin.putInCache(key, tags, TAG_GROUP, TAG_TIME);

        return tags;
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：取得单个标签
     * 
     * 参数及返回值说明：
     * 
     * @param id
     * @return
     */
    public static TagBean getTag(int id) {
        String key = "getTag: " + id;
        //从内存中取
        TagBean tag = (TagBean) CacheAdmin.getFromCache(key, TAG_GROUP,
                TAG_TIME);
        if (tag != null) {
            return tag;
        }

        //从数据库中取
        IMallService service = ServiceFactory.createMallService();
        tag = service.getTag("id = " + id);

        //放入缓存
        if (tag != null) {
            CacheAdmin.putInCache(key, tag, TAG_GROUP, TAG_TIME);
        }
        return tag;
    }

    //地区标签
    public static String AREA_TAG_GROUP = "mall";

    public static int AREA_TAG_TIME = 3600 * 24;

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：取得所有地区标签
     * 
     * 参数及返回值说明：
     * 
     * @return
     */
    public static ArrayList getAreaTags() {
        String key = "getAreaTags";
        //从内存中取
        ArrayList tags = (ArrayList) CacheAdmin.getFromCache(key,
                AREA_TAG_GROUP, AREA_TAG_TIME);
        if (tags != null) {
            return tags;
        }

        //从数据库中取
        IMallService service = ServiceFactory.createMallService();
        tags = service.getAreaTagList("id > 0", 0, -1, "display_order, id");

        //放入缓存
        CacheAdmin.putInCache(key, tags, AREA_TAG_GROUP, AREA_TAG_TIME);

        return tags;
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：取得热门标签
     * 
     * 参数及返回值说明：
     * 
     * @return
     */
    public static ArrayList getHotAreaTags() {
        String key = "getHotAreaTags";
        //从内存中取
        ArrayList tags = (ArrayList) CacheAdmin.getFromCache(key,
                AREA_TAG_GROUP, AREA_TAG_TIME);
        if (tags != null) {
            return tags;
        }

        //从数据库中取
        IMallService service = ServiceFactory.createMallService();
        tags = service.getAreaTagList("mark = " + AreaTagBean.HOT, 0, -1,
                "display_order, id");

        //放入缓存
        CacheAdmin.putInCache(key, tags, AREA_TAG_GROUP, AREA_TAG_TIME);

        return tags;
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-27
     * 
     * 说明：取得单个标签
     * 
     * 参数及返回值说明：
     * 
     * @param id
     * @return
     */
    public static AreaTagBean getAreaTag(int id) {
        String key = "getAreaTag: " + id;
        //从内存中取
        AreaTagBean tag = (AreaTagBean) CacheAdmin.getFromCache(key,
                AREA_TAG_GROUP, AREA_TAG_TIME);
        if (tag != null) {
            return tag;
        }

        //从数据库中取
        IMallService service = ServiceFactory.createMallService();
        tag = service.getAreaTag("id = " + id);

        //放入缓存
        if (tag != null) {
            CacheAdmin.putInCache(key, tag, AREA_TAG_GROUP, AREA_TAG_TIME);
        }
        return tag;
    }

    public static void reloadTag() {
        CacheAdmin.flushGroup(TAG_GROUP);
    }

    public static void reloadAreaTag() {
        CacheAdmin.flushGroup(AREA_TAG_GROUP);
    }

    public static ArrayList getTags(ArrayList intList) {
        ArrayList tagList = new ArrayList();
        Iterator itr = intList.iterator();
        Integer i = null;
        TagBean tag = null;
        while (itr.hasNext()) {
            i = (Integer) itr.next();
            tag = getTag(i.intValue());
            if (tag != null) {
                tagList.add(tag);
            }
        }

        return tagList;
    }

    public static ArrayList getAreaTags(ArrayList intList) {
        ArrayList tagList = new ArrayList();
        Iterator itr = intList.iterator();
        Integer i = null;
        AreaTagBean tag = null;
        while (itr.hasNext()) {
            i = (Integer) itr.next();
            tag = getAreaTag(i.intValue());
            if (tag != null) {
                tagList.add(tag);
            }
        }

        return tagList;
    }
}
