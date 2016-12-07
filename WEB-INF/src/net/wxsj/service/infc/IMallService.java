/*
 * Created on 2007-7-27
 *
 */
package net.wxsj.service.infc;

import java.util.ArrayList;

import net.wxsj.bean.mall.AreaTagBean;
import net.wxsj.bean.mall.AreaTagInfoBean;
import net.wxsj.bean.mall.InfoBean;
import net.wxsj.bean.mall.ReplyBean;
import net.wxsj.bean.mall.TagBean;
import net.wxsj.bean.mall.TagInfoBean;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-7-27
 * 
 * 说明：
 */
public interface IMallService extends IBaseService {
    //tag
    public boolean addTag(TagBean bean);

    public boolean updateTag(String set, String condition);

    public boolean deleteTag(String condition);

    public int getTagCount(String condition);

    public TagBean getTag(String condition);

    public ArrayList getTagList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getTagList(String query, String fieldPrefix);
    
    //area_tag
    public boolean addAreaTag(AreaTagBean bean);

    public boolean updateAreaTag(String set, String condition);

    public boolean deleteAreaTag(String condition);

    public int getAreaTagCount(String condition);

    public AreaTagBean getAreaTag(String condition);

    public ArrayList getAreaTagList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getAreaTagList(String query, String fieldPrefix);
    
    //info
    public boolean addInfo(InfoBean bean);

    public boolean updateInfo(String set, String condition);

    public boolean deleteInfo(String condition);

    public int getInfoCount(String condition);

    public InfoBean getInfo(String condition);

    public ArrayList getInfoList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getInfoList(String query, String fieldPrefix);    
    
    //tag_info
    public boolean addTagInfo(TagInfoBean bean);

    public boolean updateTagInfo(String set, String condition);

    public boolean deleteTagInfo(String condition);

    public int getTagInfoCount(String condition);

    public TagInfoBean getTagInfo(String condition);

    public ArrayList getTagInfoList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getTagInfoList(String query, String fieldPrefix);
    
    //area_tag_info
    public boolean addAreaTagInfo(AreaTagInfoBean bean);

    public boolean updateAreaTagInfo(String set, String condition);

    public boolean deleteAreaTagInfo(String condition);

    public int getAreaTagInfoCount(String condition);

    public AreaTagInfoBean getAreaTagInfo(String condition);

    public ArrayList getAreaTagInfoList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getAreaTagInfoList(String query, String fieldPrefix);
    
    //reply
    public boolean addReply(ReplyBean bean);

    public boolean updateReply(String set, String condition);

    public boolean deleteReply(String condition);

    public int getReplyCount(String condition);

    public ReplyBean getReply(String condition);

    public ArrayList getReplyList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getReplyList(String query, String fieldPrefix);    
    
}
