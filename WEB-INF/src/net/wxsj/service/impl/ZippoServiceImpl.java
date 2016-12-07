/*
 * Created on 2006-12-10
 *
 */
package net.wxsj.service.impl;

import java.util.ArrayList;

import net.wxsj.bean.ZippoBean;
import net.wxsj.bean.ZippoMobileBean;
import net.wxsj.bean.ZippoPairBean;
import net.wxsj.bean.ZippoStarBean;
import net.wxsj.bean.ZippoTypeBean;
import net.wxsj.service.infc.IZippoService;
import net.wxsj.util.db.DbOperation;

/**
 * 作者：李北金
 * 
 * 创建日期：2006-12-10
 * 
 * 说明：
 */
public class ZippoServiceImpl extends BaseServiceImpl implements IZippoService {
    public ZippoServiceImpl(int useConnType, DbOperation dbOp) {
        this.useConnType = useConnType;
        this.dbOp = dbOp;
    }

    public ZippoServiceImpl() {
        this.useConnType = CONN_IN_METHOD;
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addZippoMobile(ZippoMobileBean zippoMobile) {
        return addXXX(zippoMobile, "wxsj_zippo_mobile");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addZippoPair(ZippoPairBean zippoPair) {
        return addXXX(zippoPair, "wxsj_zippo_pair");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ZippoBean getZippo(String condition) {
        return (ZippoBean) getXXX(condition, "wxsj_zippo",
                "net.wxsj.bean.ZippoBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getZippoList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, "wxsj_zippo",
                "net.wxsj.bean.ZippoBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getZippoTypeList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, "wxsj_zippo_type",
                "net.wxsj.bean.ZippoTypeBean");
    }

    public ArrayList getZippoStarList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, "wxsj_zippo_star",
                "net.wxsj.bean.ZippoStarBean");
    }

    public ArrayList getZippoPairList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, "wxsj_zippo_pair",
                "net.wxsj.bean.ZippoPairBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ZippoMobileBean getZippoMobile(String condition) {
        return (ZippoMobileBean) getXXX(condition, "wxsj_zippo_mobile",
                "net.wxsj.bean.ZippoMobileBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ZippoPairBean getZippoPair(String condition) {
        return (ZippoPairBean) getXXX(condition, "wxsj_zippo_pair",
                "net.wxsj.bean.ZippoPairBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ZippoStarBean getZippoStar(String condition) {
        return (ZippoStarBean) getXXX(condition, "wxsj_zippo_star",
                "net.wxsj.bean.ZippoStarBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ZippoTypeBean getZippoType(String condition) {
        return (ZippoTypeBean) getXXX(condition, "wxsj_zippo_type",
                "net.wxsj.bean.ZippoTypeBean");
    }
}
