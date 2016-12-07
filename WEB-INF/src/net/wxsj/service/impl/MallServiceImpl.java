/*
 * Created on 2007-7-27
 *
 */
package net.wxsj.service.impl;

import java.util.ArrayList;

import net.wxsj.bean.mall.AreaTagBean;
import net.wxsj.bean.mall.AreaTagInfoBean;
import net.wxsj.bean.mall.InfoBean;
import net.wxsj.bean.mall.ReplyBean;
import net.wxsj.bean.mall.TagBean;
import net.wxsj.bean.mall.TagInfoBean;
import net.wxsj.service.infc.IMallService;
import net.wxsj.util.db.DbOperation;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-7-27
 * 
 * 说明：
 */
public class MallServiceImpl extends BaseServiceImpl implements IMallService {
    public MallServiceImpl(int useConnType, DbOperation dbOp) {
        this.useConnType = useConnType;
        this.dbOp = dbOp;
    }

    public MallServiceImpl() {
        this.useConnType = CONN_IN_METHOD;
    }
    
    public String tagTableName = "wxsj_tag";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addTag(TagBean bean) {
        return addXXX(bean, tagTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteTag(String condition) {
        return deleteXXX(condition, tagTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public TagBean getTag(String condition) {
        return (TagBean) getXXX(condition, tagTableName,
                "net.wxsj.bean.mall.TagBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getTagCount(String condition) {
        return getXXXCount(condition, tagTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getTagList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, tagTableName,
                "net.wxsj.bean.mall.TagBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateTag(String set, String condition) {
        return updateXXX(set, condition, tagTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getTagList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, tagTableName,
                "net.wxsj.bean.mall.TagBean");
    }
    
    public String areaTagTableName = "wxsj_area_tag";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addAreaTag(AreaTagBean bean) {
        return addXXX(bean, areaTagTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteAreaTag(String condition) {
        return deleteXXX(condition, areaTagTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public AreaTagBean getAreaTag(String condition) {
        return (AreaTagBean) getXXX(condition, areaTagTableName,
                "net.wxsj.bean.mall.AreaTagBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getAreaTagCount(String condition) {
        return getXXXCount(condition, areaTagTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getAreaTagList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, areaTagTableName,
                "net.wxsj.bean.mall.AreaTagBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateAreaTag(String set, String condition) {
        return updateXXX(set, condition, areaTagTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getAreaTagList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, areaTagTableName,
                "net.wxsj.bean.mall.AreaTagBean");
    }
    
    public String infoTableName = "wxsj_info";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addInfo(InfoBean bean) {
        return addXXX(bean, infoTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteInfo(String condition) {
        return deleteXXX(condition, infoTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public InfoBean getInfo(String condition) {
        return (InfoBean) getXXX(condition, infoTableName,
                "net.wxsj.bean.mall.InfoBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getInfoCount(String condition) {
        return getXXXCount(condition, infoTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getInfoList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, infoTableName,
                "net.wxsj.bean.mall.InfoBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateInfo(String set, String condition) {
        return updateXXX(set, condition, infoTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getInfoList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, infoTableName,
                "net.wxsj.bean.mall.InfoBean");
    }
    
    public String tagInfoTableName = "wxsj_tag_info";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addTagInfo(TagInfoBean bean) {
        return addXXX(bean, tagInfoTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteTagInfo(String condition) {
        return deleteXXX(condition, tagInfoTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public TagInfoBean getTagInfo(String condition) {
        return (TagInfoBean) getXXX(condition, tagInfoTableName,
                "net.wxsj.bean.mall.TagInfoBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getTagInfoCount(String condition) {
        return getXXXCount(condition, tagInfoTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getTagInfoList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, tagInfoTableName,
                "net.wxsj.bean.mall.TagInfoBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateTagInfo(String set, String condition) {
        return updateXXX(set, condition, tagInfoTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getTagInfoList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, tagInfoTableName,
                "net.wxsj.bean.mall.TagInfoBean");
    }
    
    public String areaTagInfoTableName = "wxsj_area_tag_info";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addAreaTagInfo(AreaTagInfoBean bean) {
        return addXXX(bean, areaTagInfoTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteAreaTagInfo(String condition) {
        return deleteXXX(condition, areaTagInfoTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public AreaTagInfoBean getAreaTagInfo(String condition) {
        return (AreaTagInfoBean) getXXX(condition, areaTagInfoTableName,
                "net.wxsj.bean.mall.AreaTagInfoBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getAreaTagInfoCount(String condition) {
        return getXXXCount(condition, areaTagInfoTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getAreaTagInfoList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, areaTagInfoTableName,
                "net.wxsj.bean.mall.AreaTagInfoBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateAreaTagInfo(String set, String condition) {
        return updateXXX(set, condition, areaTagInfoTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getAreaTagInfoList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, areaTagInfoTableName,
                "net.wxsj.bean.mall.AreaTagInfoBean");
    }
    
    public String replyTableName = "wxsj_reply";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addReply(ReplyBean bean) {
        return addXXX(bean, replyTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteReply(String condition) {
        return deleteXXX(condition, replyTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ReplyBean getReply(String condition) {
        return (ReplyBean) getXXX(condition, replyTableName,
                "net.wxsj.bean.mall.ReplyBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getReplyCount(String condition) {
        return getXXXCount(condition, replyTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getReplyList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, replyTableName,
                "net.wxsj.bean.mall.ReplyBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateReply(String set, String condition) {
        return updateXXX(set, condition, replyTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getReplyList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, replyTableName,
                "net.wxsj.bean.mall.ReplyBean");
    }    
}
