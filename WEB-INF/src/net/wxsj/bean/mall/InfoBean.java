/*
 * Created on 2007-7-27
 *
 */
package net.wxsj.bean.mall;

import java.util.ArrayList;
import java.util.Iterator;

import net.joycool.wap.bean.UserBean;
import net.wxsj.framework.JoycoolInfc;
import net.wxsj.framework.mall.MallFrk;
import net.wxsj.util.IntUtil;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-7-27
 * 
 * 说明：
 */
public class InfoBean {
    public static int BUY = 0;

    public static int SELL = 1;

    public static int NOT_VALIDATED = 0;

    public static int VALIDATED = 1;

    public int id;

    public String name;

    public String intro;

    public int userId;

    public int infoType;

    public String tags;

    public String areaTags;

    public int validated;

    public String price;

    public String buyMode;

    public String telephone;

    public String address;

    public int hasTag;

    public int hasAreaTag;

    public String lastReplyTime;

    public int replyCount;

    public int hits;

    public String createDatetime;

    public int isTop;

    public int isJinghua;

    public UserBean getUser() {
        return JoycoolInfc.getUser(userId);
    }

    public String userNick;

    /**
     * @return Returns the userNick.
     */
    public String getUserNick() {
        return userNick;
    }

    /**
     * @param userNick
     *            The userNick to set.
     */
    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public ArrayList getTagIntList() {
        ArrayList intList = IntUtil.getIntList(tags, MallFrk.SEPERATOR);
        return intList;
    }

    public ArrayList getAreaTagIntList() {
        ArrayList intList = IntUtil.getIntList(areaTags, MallFrk.SEPERATOR);
        return intList;
    }
    
    public ArrayList getTagList() {
        ArrayList intList = IntUtil.getIntList(tags, MallFrk.SEPERATOR);
        return MallFrk.getTags(intList);
    }

    public ArrayList getAreaTagList() {
        ArrayList intList = IntUtil.getIntList(areaTags, MallFrk.SEPERATOR);
        return MallFrk.getAreaTags(intList);
    }

    public String getTagListStr() {
        ArrayList tagList = getTagList();
        if (tagList == null || tagList.size() == 0) {
            return "无";
        }

        Iterator itr = tagList.iterator();
        TagBean tag = null;
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (itr.hasNext()) {
            tag = (TagBean) itr.next();
            if (i == 0) {
                sb.append(" ");
            }
            sb.append(tag.getName());
        }

        return sb.toString();
    }

    public String getAreaTagListStr() {
        ArrayList tagList = getAreaTagList();
        if (tagList == null || tagList.size() == 0) {
            return "无";
        }

        Iterator itr = tagList.iterator();
        AreaTagBean tag = null;
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (itr.hasNext()) {
            tag = (AreaTagBean) itr.next();
            if (i == 0) {
                sb.append(" ");
            }
            sb.append(tag.getName());
        }

        return sb.toString();
    }

    public String getInfoTypeStr() {
        if (infoType == InfoBean.BUY) {
            return "求购";
        } else {
            return "出售";
        }
    }
    
    public String getInfoTypeStr1() {
        if (infoType == InfoBean.BUY) {
            return "买";
        } else {
            return "卖";
        }
    }

    /**
     * @return Returns the isJinghua.
     */
    public int getIsJinghua() {
        return isJinghua;
    }

    /**
     * @param isJinghua
     *            The isJinghua to set.
     */
    public void setIsJinghua(int isJinghua) {
        this.isJinghua = isJinghua;
    }

    /**
     * @return Returns the isTop.
     */
    public int getIsTop() {
        return isTop;
    }

    /**
     * @param isTop
     *            The isTop to set.
     */
    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    /**
     * @return Returns the createDatetime.
     */
    public String getCreateDatetime() {
        return createDatetime;
    }

    /**
     * @param createDatetime
     *            The createDatetime to set.
     */
    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * @return Returns the hits.
     */
    public int getHits() {
        return hits;
    }

    /**
     * @param hits
     *            The hits to set.
     */
    public void setHits(int hits) {
        this.hits = hits;
    }

    /**
     * @return Returns the lastReplyTime.
     */
    public String getLastReplyTime() {
        return lastReplyTime;
    }

    /**
     * @param lastReplyTime
     *            The lastReplyTime to set.
     */
    public void setLastReplyTime(String lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
    }

    /**
     * @return Returns the replyCount.
     */
    public int getReplyCount() {
        return replyCount;
    }

    /**
     * @param replyCount
     *            The replyCount to set.
     */
    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    /**
     * @return Returns the hasAreaTag.
     */
    public int getHasAreaTag() {
        return hasAreaTag;
    }

    /**
     * @param hasAreaTag
     *            The hasAreaTag to set.
     */
    public void setHasAreaTag(int hasAreaTag) {
        this.hasAreaTag = hasAreaTag;
    }

    /**
     * @return Returns the hasTag.
     */
    public int getHasTag() {
        return hasTag;
    }

    /**
     * @param hasTag
     *            The hasTag to set.
     */
    public void setHasTag(int hasTag) {
        this.hasTag = hasTag;
    }

    /**
     * @return Returns the address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     *            The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return Returns the areaTags.
     */
    public String getAreaTags() {
        return areaTags;
    }

    /**
     * @param areaTags
     *            The areaTags to set.
     */
    public void setAreaTags(String areaTags) {
        this.areaTags = areaTags;
    }

    /**
     * @return Returns the buyMode.
     */
    public String getBuyMode() {
        return buyMode;
    }

    /**
     * @param buyMode
     *            The buyMode to set.
     */
    public void setBuyMode(String buyMode) {
        this.buyMode = buyMode;
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Returns the infoType.
     */
    public int getInfoType() {
        return infoType;
    }

    /**
     * @param infoType
     *            The infoType to set.
     */
    public void setInfoType(int infoType) {
        this.infoType = infoType;
    }

    /**
     * @return Returns the intro.
     */
    public String getIntro() {
        return intro;
    }

    /**
     * @param intro
     *            The intro to set.
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the price.
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price
     *            The price to set.
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return Returns the tags.
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags
     *            The tags to set.
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return Returns the telephone.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone
     *            The telephone to set.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return Returns the userId.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            The userId to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return Returns the validated.
     */
    public int getValidated() {
        return validated;
    }

    /**
     * @param validated
     *            The validated to set.
     */
    public void setValidated(int validated) {
        this.validated = validated;
    }
}
