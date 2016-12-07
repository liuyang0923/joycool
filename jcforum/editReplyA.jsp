<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.util.*, net.joycool.wap.bean.jcforum.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil" %><%
response.setHeader("Cache-Control","no-cache");
String contentId=request.getParameter("contentId");
int pageIndex =  StringUtil.toInt((String) request.getParameter("p"));
if(pageIndex == -1) {
	pageIndex = 0;
}
ForumContentBean forumContent = ForumCacheUtil.getForumContent(StringUtil.toInt(contentId));
session.setAttribute("forumreply","true");

int use = 0;
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
if(UserBagCacheUtil.getUserBagItemCount(ForumReplyBean.ACTION_A_ITEM ,loginUser.getId()) > 0) {
	int useBagId = UserBagCacheUtil.getUserBagById(ForumReplyBean.ACTION_A_ITEM,loginUser.getId());
	UserBagBean useBagBean = UserBagCacheUtil.getUserBagCache(useBagId);
	if(useBagBean.getTimeLeft() > 0){
		use = use | 0x1;		//套餐A
	}
	
}
if(UserBagCacheUtil.getUserBagItemCount(ForumReplyBean.ACTION_B_ITEM ,loginUser.getId()) > 0) {
	int useBagId = UserBagCacheUtil.getUserBagById(ForumReplyBean.ACTION_B_ITEM,loginUser.getId());
	UserBagBean useBagBean = UserBagCacheUtil.getUserBagCache(useBagId);
	if(useBagBean.getTimeLeft() > 0){
		use = use | 0x2;		//套餐B
	}
}
List list = ForumReplyBean.getActionList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="发表回帖">
<p align="left">
<%=BaseAction.getTop(request, response)%>
每次回复增加1点经验值<%=use %><br/>
<%if((use & 0x1) != 0) {
	int count = 0;
%>动作套餐A：<br/>
<%for(int i = 0;i < list.size(); i++) {
	ForumActionBean bean = (ForumActionBean)list.get(i);
	
	if(bean.getType() == 1) {
		count++;
%><a href="viewContent.jsp?action=<%=bean.getId()%>&amp;content=<%=bean.getId()%>&amp;contentId=<%=contentId%>"><%=bean.getName()%></a><%if(count % 4 == 0) {%><br/><%}else{%>|<%}}
}if(count % 4 != 0){%><br/><%}}
if((use & 0x2) != 0) {
	int count = 0;
%>动作套餐B：<br/>
<%for(int i = 0;i < list.size(); i++) {
	ForumActionBean bean = (ForumActionBean)list.get(i);
	
	if(bean.getType() == 2) {
		count++;
%><a href="viewContent.jsp?action=<%=bean.getId()%>&amp;content=<%=bean.getId()%>&amp;contentId=<%=contentId%>"><%=bean.getName()%></a><%if(count % 4 == 0) {%><br/><%}else{%>|<%}}
}if(count % 4 != 0){%><br/><%}}
if(forumContent!=null){%>
<a href="viewContent.jsp?contentId=<%=forumContent.getId()%>&amp;p=<%=pageIndex%>">返回主题</a><br/>
<%}%>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>