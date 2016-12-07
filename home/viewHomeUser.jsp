<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.home.*"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.home.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.service.infc.IFriendService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.bean.friend.FriendBean" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%
response.setHeader("Cache-Control","no-cache");
HomeAction home=new HomeAction(request);  
home.getHome(request);
HomeUserBean homeUser=(HomeUserBean)request.getAttribute("homeUser");
UserBean user=(UserBean)request.getAttribute("user");
UserStatusBean us=UserInfoUtil.getUserStatus(user.getId());
user.setUs(us);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="用户档案">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=StringUtil.toWml(user.getNickName())%>的档案<br/>
------------------<br/>
<%if(user.isFlagFriend()){
IFriendService friendService = ServiceFactory.createFriendService();
FriendBean friend=friendService.getFriend(user.getId());
	if(friend!=null && !friend.getAttach().equals("")){%>
<img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%>/friend/attach/<%=friend.getAttach()%>" alt="loading..."/><br/>
	<%}
}%>
真实姓名:<%=StringUtil.toWml(homeUser.getName())%><br/>
性别：<logic:equal name="user" property="gender" value="1">男</logic:equal><logic:equal name="user" property="gender" value="0">女</logic:equal><br/>
地区：<%if(user.getCityname() != null){%><%=user.getCityname()%><%}else{%>未知<%}%><br/>
年龄：<%=user.getAge()%><br/>
星座：<%=(String)LoadResource.getConstellation().get(homeUser.getConstellation()+"")%><br/>
身高：<%=homeUser.getHeight()%><br/>
体重：<%=homeUser.getWeight()%><br/>
手机：(保密)<%--=homeUser.getMobile()--%><br/>
职业：<%=StringUtil.toWml(homeUser.getWork())%><br/>
性格：<%=(String)LoadResource.getPersonality().get(homeUser.getPersonality()+"")%><br/>
婚姻状况：<%=(String)LoadResource.getMarriage().get(homeUser.getMarriage()+"")%><br/>
交友目的：<%=(String)LoadResource.getFriendAim().get(homeUser.getAim()+"")%><br/>
择友条件：<%=StringUtil.toWml(homeUser.getFriendCondition())%><br/>
个性签名：<%=StringUtil.toWml(user.getSelfIntroduction())%><br/>
<%--
上次登录时间:<%=user.getUs().getLastLoginTimeStr()%><br/>
当前头衔:
<%
HashMap map=LoadResource.getRankMap();
String name=null;
RankBean rank = (RankBean) map.get(new Integer(user.getUs().getRank()));
if(user.getGender()==1)
{name=rank.getMaleName();}
else{name=rank.getFemaleName();}
%>
<%=name%><br/>
当前等级:<%=user.getUs().getRank()%>级<br/>
当前经验值:<%=user.getUs().getPoint()%>点<br/>
当前财富：<%=user.getUs().getGamePoint()%>乐币<br/>
<logic:equal name="isFriend" value="1">
<%=user.getGender() == 1? "他" : "她"%>是你的好友。<br/>
</logic:equal>
<logic:equal name="isBadGuy" value="1">
<%=user.getGender() == 1? "他" : "她"%>在你的黑名单上。<br/>
</logic:equal>
<br/>
<a href="home.jsp?userId=<%= user.getId()%>" >返回<%= StringUtil.toWml(user.getNickName()) %>的家园</a><br/>
--%>
<a href="viewAllHome.jsp">*家园之星*</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>