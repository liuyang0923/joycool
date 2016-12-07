package net.joycool.wap.bean.jcforum;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.joycool.wap.service.impl.JcForumServiceImpl;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.StringUtil;

public class ForumReplyBean {
	
	public static int REPLY_ITEM = 5142;		//匿名回复卡
	
	public static int ACTION_A_ITEM = 5143;		//论坛动作套餐A	第三位到第六位
	
	public static int ACTION_B_ITEM = 5144;		//论坛动作套餐B
	
	public static int ACTION_TYPE = 1 << 2;		//是否是论坛动作
	
	public static final int NICK_REPLY = 1 << 1;//是否是匿名发表
	
	int id;

	int contentId;

	int userId;

	String content;

	Date createTime;

	int delMark;

	// 表情
	int cType;

	// 图片附件
	String attach = "";

	
	public void setAction() {
		cType = cType | ACTION_TYPE;
	}
	
	public boolean isAction(){
		return (cType & ACTION_TYPE) != 0;
	}
	
	public int getActionType(){
		return StringUtil.toId(content);
	}
	
	public static HashMap actionMap;
	
	public static List actionList;
	
	public static List getActionList(){
		if(actionList == null) {
			synchronized(ForumReplyBean.class) {
				if(actionList == null) {
					JcForumServiceImpl service = new JcForumServiceImpl();
					actionList = service.getForumActionList(null);
				}
			}
		}
		return actionList;
	}
	
	public static HashMap getActionMap(){
		if(actionMap == null) {
			synchronized(ForumReplyBean.class) {
				if(actionMap == null) {
					actionMap = new HashMap();
					JcForumServiceImpl service = new JcForumServiceImpl();
					List list = service.getForumActionList(null);
					Iterator it = list.iterator();
					while(it.hasNext()) {
						ForumActionBean bean = (ForumActionBean)it.next();
						actionMap.put(new Integer(bean.getId()), bean);
					}
				}
			}
		}
		return actionMap;
	}
	
	public String getActionTypeStr(){
		ForumActionBean bean = (ForumActionBean)getActionMap().get(new Integer(getActionType()));
		if(bean == null) {
			JcForumServiceImpl service = new JcForumServiceImpl();
			bean = service.getForumActionBean(" id = " + getActionType());
			if(bean != null)
				getActionMap().put(new Integer(bean.getId()), bean);
		}
		if(bean == null)
			return "顶";
		else 
			return bean.getContent();
	}
	
	public void setNickReply(){
		cType = cType | NICK_REPLY;
	}
	
	public boolean isNickReply(){
		return (cType & NICK_REPLY) != 0;
	}
	
	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		if(attach.length() != 0 && !attach.startsWith("/"))
			this.attach = "/jcforum/" + attach;
		else
			this.attach = attach;
	}

	public int getCType() {
		return cType;
	}

	public void setCType(int type) {
		cType = type;
	}

	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return Returns the contentId.
	 */
	public int getContentId() {
		return contentId;
	}

	/**
	 * @param contentId
	 *            The contentId to set.
	 */
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	/**
	 * @return Returns the createDatetime.
	 */
	public String getCreateDatetime() {
		return DateUtil.sformatTime(createTime);
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public int getDelMark() {
		return delMark;
	}

	public void setDelMark(int delMark) {
		this.delMark = delMark;
	}

}
