/*
 * Created on 2006-2-16
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.friendlink.LinkRecordBean;
import net.joycool.wap.bean.friendlink.LinkTypeBean;
import net.joycool.wap.bean.friendlink.RandomLinkBean;
import net.joycool.wap.bean.friendlink.RandomSubLinkBean;
/**
 * @author MCQ
 *
 */
public interface IFriendLinkService { 
    public LinkRecordBean getLinkRecord(String condition);
    public Vector getLinkRecordList(String condition);
    public boolean updateLinkRecord(String set,String condition);
    public void InsertLinkRecord(int linkid,String friendlinkName,String friendlinkaddress,int linktype);
    public int getLinkRecordsCount(String condition);
    public LinkTypeBean getLinkType(String condition);
    public Vector getLinkTypeList();
    public boolean updateLinkType(String set, String condition);
    public int getLinkTypesCount(String condition);
    public int getLinkTypesId(String condition);
    public int getLinkId();
    public int getLinkIdHuangYe();
    public boolean chenkLinkRecordName(String condition);  
    
    //mcq_2006-7-13_欺骗友链连接方法_start
    public RandomLinkBean getRandomLink(String condition);
    public Vector getRandomLinkList(String condition);
    //mcq_2006-7-13_欺骗友链连接方法_end
    
    //mcq_2007-4-12_欺骗友链连接方法_start
    public RandomSubLinkBean getRandomSubLink(String condition);
    public Vector getRandomSubLinkList(String condition);
    //mcq_2007-4-12_欺骗友链连接方法_end
}

