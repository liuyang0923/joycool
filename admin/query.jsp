<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%!
static String[] forbidType={"chat","forum","mail","home","tong","team","info","game","fan","sell"};
static String[] forbidName={"聊天","论坛","信件","家园","帮会功能","圈子","个人资料","游戏","防沉迷","交易"};
%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="封禁信息查询">
<p align="left">
<%=BaseAction.getTop(request, response)%><%
boolean forbids=false;
for(int i = 0;i < forbidType.length;i++){
ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid(forbidType[i],loginUser.getId());
if(forbid!=null){
forbids = true;
%>[<%=forbidName[i]%>]封禁至<%=DateUtil.formatDate2(forbid.getEndTime())%><br/>
封禁原因:<%=forbid.getBak()%><br/><%
	}
}%>
<%if(!forbids){%>没有查询到封禁信息.<br/><%}%>
<a href="/user/onlineManager.jsp?forumId=355">返回乐酷警局</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>