<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="java.util.*" %><%!
static String[] adminType={"chata","foruma","maila","homea","tonga","teama","infoa","newsa","fana","sella"};
static String[] adminName={"聊天","论坛总","信件","家园","帮会功能","圈子","个人资料","新闻","防沉迷","交易"};
%><%response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
UserBean loginUser = action.getLoginUser();
int type=action.getParameterIntS("type");
if(type < 0 || type > adminType.length)type=-1;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷监察名单">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(type==-1){%>

<a href="/Column.do?columnId=10246">乐酷监察权职说明&gt;&gt;</a><br/>
<%for(int i=0;i<adminType.length;i++){
%>
<%=i+1%>.<a href="list.jsp?type=<%=i%>"><%=adminName[i]%>监察(<%=ForbidUtil.getGroup(adminType[i]).getMap().size()%>人)</a><br/>
<%}%><br/>

<%}else{%>
<a href="/Column.do?columnId=10246">乐酷监察权职说明&gt;&gt;</a><br/>
==<%=adminName[type]%>监察==<br/>
<%
List forbidList = ForbidUtil.getForbidList(adminType[type]);
if(forbidList != null){
	Iterator itr = forbidList.iterator();
	for(int i=0;i<forbidList.size();i++){
	ForbidUtil.ForbidBean forbid = (ForbidUtil.ForbidBean)forbidList.get(i);
	UserBean user = UserInfoUtil.getUser(forbid.getValue());
%><%=i+1%>.<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>(<%=LoadResource.getPostionNameByUserId(user.getId())%>)<br/>
<%}}%>
<br/>
<a href="list.jsp">返回上一级</a><br/>

<%}%>
<a href="/user/onlineManager.jsp?forumId=355">返回乐酷警察局</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>