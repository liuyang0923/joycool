/*
 * Created on 2006-12-10
 *
 */
package net.wxsj.framework;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import net.wxsj.bean.ZippoBean;
import net.wxsj.bean.ZippoStarBean;
import net.wxsj.bean.ZippoTypeBean;
import net.wxsj.service.factory.ServiceFactory;
import net.wxsj.service.infc.IBaseService;
import net.wxsj.service.infc.IZippoService;

/**
 * 作者：李北金
 * 
 * 创建日期：2006-12-10
 * 
 * 说明：
 */
public class ZippoFrk {
    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-10
     * 
     * 说明：
     * 
     * key: zippo id
     * 
     * value: ZippoBean
     */
    public static Hashtable zippoListById;

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-10
     * 
     * 说明：
     * 
     * key: type id
     * 
     * value: Zippo List
     */
    public static Hashtable zippoListByType;

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-10
     * 
     * 说明：
     * 
     * key: type id
     * 
     * value: ZippoTypeBean
     */
    public static Hashtable zippoTypeList;

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-10
     * 
     * 说明：
     * 
     * key: star id
     * 
     * value: ZippoStarBean
     */
    public static Hashtable zippoStarList;

    public static Hashtable getZippoListById() {
        if (zippoListById != null) {
            return zippoListById;
        }

        IZippoService zippoService = ServiceFactory.createZippoService();
        ArrayList zippoList = zippoService.getZippoList("id > 0", 0, -1, "id");
        Iterator itr = zippoList.iterator();
        zippoListById = new Hashtable();
        ZippoBean zippo = null;
        while (itr.hasNext()) {
            zippo = (ZippoBean) itr.next();
            zippoListById.put("" + zippo.getId(), zippo);
        }

        return zippoListById;
    }

    public static Hashtable getZippoListByType() {
        if (zippoListByType != null) {
            return zippoListByType;
        }

        IZippoService zippoService = ServiceFactory.createZippoService(
                IBaseService.CONN_IN_SERVICE, null);
        ArrayList typeList = zippoService.getZippoTypeList("id > 0", 0, -1,
                "id");
        Iterator itr = typeList.iterator();
        zippoListByType = new Hashtable();
        ZippoTypeBean type = null;
        while (itr.hasNext()) {
            type = (ZippoTypeBean) itr.next();
            zippoListByType.put("" + type.getId(), zippoService.getZippoList(
                    "type_id = " + type.getId(), 0, -1, "id"));
        }

        zippoService.releaseAll();
        
        return zippoListByType;
    }

    public static Hashtable getZippoTypeList() {
        if (zippoTypeList != null) {
            return zippoTypeList;
        }

        IZippoService zippoService = ServiceFactory.createZippoService();
        ArrayList typeList = zippoService.getZippoTypeList("id > 0", 0, -1,
                "id");
        Iterator itr = typeList.iterator();
        zippoTypeList = new Hashtable();
        ZippoTypeBean type = null;
        while (itr.hasNext()) {
            type = (ZippoTypeBean) itr.next();
            zippoTypeList.put("" + type.getId(), type);
        }

        return zippoTypeList;
    }

    public static Hashtable getZippoStarList() {
        if (zippoStarList != null) {
            return zippoStarList;
        }

        IZippoService zippoService = ServiceFactory.createZippoService();
        ArrayList starList = zippoService.getZippoStarList("id > 0", 0, -1,
                "id");
        Iterator itr = starList.iterator();
        zippoStarList = new Hashtable();
        ZippoStarBean star = null;
        while (itr.hasNext()) {
            star = (ZippoStarBean) itr.next();
            zippoStarList.put("" + star.getId(), star);
        }

        return zippoStarList;
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-10
     * 
     * 说明：
     * 
     * 参数及返回值说明：
     * 
     * @param id
     * @return
     */
    public static ZippoTypeBean getTypeById(int id) {
        return (ZippoTypeBean) getZippoTypeList().get("" + id);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-10
     * 
     * 说明：
     * 
     * 参数及返回值说明：
     * 
     * @param id
     * @return
     */
    public static ZippoStarBean getStarById(int id) {
        return (ZippoStarBean) getZippoStarList().get("" + id);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-10
     * 
     * 说明：
     * 
     * 参数及返回值说明：
     * 
     * @param id
     * @return
     */
    public static ZippoBean getZippoById(int id) {
        return (ZippoBean) getZippoListById().get("" + id);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2006-12-10
     * 
     * 说明：
     * 
     * 参数及返回值说明：
     * 
     * @param id
     * @return
     */
    public static ArrayList getZippoListByType(int typeId) {
        return (ArrayList) getZippoListByType().get("" + typeId);
    }
}
