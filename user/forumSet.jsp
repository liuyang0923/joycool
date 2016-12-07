<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.jcforum.*,net.joycool.wap.cache.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.user.SendAction" %><%
response.setHeader("Cache-Control","no-cache");
if(request.getParameter("mark")!=null){
SendAction action = new SendAction(request);
action.picMark(request);
}
UserBean loginUser=(UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
net.joycool.wap.bean.UserSettingBean set = loginUser.getUserSetting();
ForumUserBean forumUser = ForumCacheUtil.getForumUser(loginUser.getId());
String s = request.getParameter("s");
if(s != null && s.equals("s")) {
	String si = request.getParameter("si");
	if(si == null){
		request.setAttribute("msg","不能为空");
	} else if(StringUtil.getCLength(si) > 30){
		request.setAttribute("msg","不能大于15个中文字");
	} else {
		forumUser.setSignature(si);
		ForumCacheUtil.updateForumUser(forumUser);
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="论坛设置">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("msg")==null?"":request.getAttribute("msg")+"<br/>" %>
<a href="forumSet.jsp?mark=13">论坛回复排列</a>(<%if(set!=null&&set.isFlagVorder()){%>倒序<%}else{%>顺序<%}%>)<br/>
<a href="forumSet.jsp?mark=10">删除主题要求确认</a>(<%if(set!=null&&set.isFlagTopicDelete()){%>否<%}else{%>是<%}%>)<br/>
<a href="forumSet.jsp?mark=11">删除回复要求确认</a>(<%if(set!=null&&set.isFlagReplyDelete()){%>否<%}else{%>是<%}%>)<br/>
<a href="forumSet.jsp?mark=12">使用xhtml页面编辑</a>(<%if(set!=null&&set.isFlagXhtml()){%>是<%}else{%>否<%}%>)<br/>
<a href="forumSet.jsp?mark=9">使用xhtml页面浏览</a>(<%if(set!=null&&set.isFlag(9)){%>是<%}else{%>否<%}%>)<br/>
注意:不支持wap2.0或者xhtml的手机，请不要选择使用xhtml页面编辑。<br/>
注意:xhtml页面比传统页面大，因此会导致流量增加<br/>
<%if(forumUser!=null && forumUser.isVip()) {%>
您的论坛vip还有<%=forumUser.getVipLeft() %>到期<br/>
<%=(forumUser.getSignature() == null?"请设置您的论坛个性签名":"个性签名:"+StringUtil.toWml(forumUser.getSignature())) %><br/>
<input name="si" value=""/><br/>
<anchor>设置论坛个性签名<go href="forumSet.jsp?s=s" method="post">
<postfield name="si" value="$si"/>
</go></anchor><br/><%} %>
<a href="userInfo.jsp">返回用户设置</a><br/>
<a href="/jcforum/index.jsp">返回论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>