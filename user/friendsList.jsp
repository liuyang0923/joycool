<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%!
static IUserService userService = ServiceFactory.createUserService();
%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
UserSettingBean set = loginUser.getUserSetting();
//用户好友
Vector userList = (Vector) request.getAttribute("userList");
String option = (String) request.getAttribute("option");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
int start = StringUtil.toInt((String)request.getAttribute("start"));
int end = StringUtil.toInt((String)request.getAttribute("end"));
String roomId = (String) request.getParameter("roomId");
if (roomId == null) {
	roomId = "0";
}
HashMap noteMap = null;
if(set!=null && set.isFlag(16)){
	noteMap = UserInfoUtil.getUserNoteMap(loginUser.getId());
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="好友列表">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=option.equals("onlinefriends")?"我的在线好友":""%><%=option.equals("outlinefriends")?"我的离线好友":""%><%=option.equals("strange")?"陌生人":""%><br/>
------------<br/>
<%
int count = userList.size();
for(int i = start; i < end; i ++){
	if(i>count-1) break;
	String userId = (String) userList.get(i);
	UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	if(user==null) continue;
    //liuyi 2006-11-01 结义显示 start 
	UserFriendBean friend = userService.getUserFriend(loginUser.getId(), user.getId());
	UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(""+user.getId());
%>
<%=(i + 1)%>.<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><a href="/chat/post.jsp?roomId=<%=roomId%>&amp;toUserId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%><% if(friend!=null){ if(friend.getMark()==1){%>(义)<%}else if(friend.getMark()==2){%>(<%= (user.getGender()==1)?"夫":"妻"%>)<%} } %></a>
<%if(noteMap!=null){UserNoteBean n = (UserNoteBean)noteMap.get(new Integer(user.getId()));if(n!=null){%><%=StringUtil.toWml(n.getShortNote())%><%}}%><% if(onlineUser!=null){%>(<%=PositionUtil.getPositionName(onlineUser.getId() + "", onlineUser.getPositionId())%>)<%}%><br/>
<%
    //liuyi 2006-11-01 结义显示 end 
}
%>
<%=PageUtil.shuzifenye(Integer.parseInt(totalPage),Integer.parseInt(pageIndex),"/user/FriendsList.do?roomId=" + roomId+ "&amp;option="+option,true,"|",response)%>
<br/>

<a href="ViewFriends.do">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>