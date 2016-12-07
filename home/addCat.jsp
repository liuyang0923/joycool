<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.home.*,net.joycool.wap.service.impl.*,net.joycool.wap.util.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean,net.joycool.wap.cache.util.HomeCacheUtil,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.bean.friend.FriendMarriageBean" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
String tip = "";
int type = action.getParameterInt("t");	// t=0:添加日记,否则添加相册
int submit = action.getParameterInt("s");
if (submit == 1){
	if (type == 0){
		action.homeDiaryCat();
	} else {
		action.homePhotoCat();
	}
	String msg = (String)request.getAttribute("msg");
	if (msg != null && !("".equals(msg))){
		tip = msg;
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=type==0?"日记薄":"相册薄" %>"><p align="left">
<%=BaseAction.getTop(request, response)%>
<% if ("".equals(tip)){
if (type==0){
%>添加日记分类<br/>
新增分类名称:<input name="title"  maxlength="10" value=""/><br/>
设置分类权限:<select name="privacy">
<option value="<%=HomeDiaryCat.PRIVACY_ALL %>">所人有可见</option>
<option value="<%=HomeDiaryCat.PRIVACY_FRIEND %>">仅好友可见</option>
<option value="<%=HomeDiaryCat.PRIVACY_SELF %>">隐藏</option>
</select><br/>
<anchor title="确定">添加
    <go href="addCat.jsp?s=1" method="post">
    <postfield name="action" value="a"/>
    <postfield name="uid" value="<%=action.getLoginUser().getId() %>"/>
    <postfield name="privacy" value="$privacy"/>
    <postfield name="title" value="$title"/>
    </go>
</anchor><br/>
<a href="homeDiaryCat.jsp">返回我的日记首页</a><br/>
<a href="home.jsp">返回我的家园</a><br/><%	
} else {
%>添加相册分类<br/>
新增分类名称:<input name="title"  maxlength="10" value=""/><br/>
设置分类权限:<select name="privacy">
<option value="<%=HomeDiaryCat.PRIVACY_ALL %>">所人有可见</option>
<option value="<%=HomeDiaryCat.PRIVACY_FRIEND %>">仅好友可见</option>
<option value="<%=HomeDiaryCat.PRIVACY_SELF %>">隐藏</option>
</select><br/>
<anchor title="确定">添加
    <go href="addCat.jsp?s=1&amp;t=<%=type %>" method="post">
    <postfield name="action" value="a"/>
    <postfield name="uid" value="<%=action.getLoginUser().getId() %>"/>
    <postfield name="privacy" value="$privacy"/>
    <postfield name="title" value="$title"/>
    </go>
</anchor><br/>
<a href="homePhotoCat.jsp">返回我的相册首页</a><br/>
<a href="home.jsp">返回我的家园</a><br/><%		
}
} else {
%><%=tip%><br/>
<% if (type == 0){
%><a href="homeDiaryCat.jsp">返回日记首页</a><br/><%	
} else {
%><a href="homePhotoCat.jsp">返回相册首页</a><br/><%
}
%>
<br/><%	
}
%>
<%=BaseAction.getBottom(request, response)%>
</p></card></wml>