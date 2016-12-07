<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.bean.friend.FriendCartoonBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control", "no-cache");%>
<%
FriendAction action = new FriendAction(request);
action.cartoon(request);
int size1=StringUtil.toInt((String)request.getAttribute("cartoon1size"));
int size2=StringUtil.toInt((String)request.getAttribute("cartoon2size"));
int size3=StringUtil.toInt((String)request.getAttribute("cartoon3size"));
FriendCartoonBean cartoon1 =(FriendCartoonBean)request.getAttribute("cartoon1");
FriendCartoonBean cartoon2 =(FriendCartoonBean)request.getAttribute("cartoon2");
FriendCartoonBean cartoon3 =(FriendCartoonBean)request.getAttribute("cartoon3");
session.setAttribute("cartoonrefresh","true");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="卡通头像">
<p align="left">
<%=BaseAction.getTop(request, response)%>
帅哥<br/>
<%if(cartoon2!=null){%>
<img src="<%=(cartoon2.getUrl())%>" alt="帅哥"/><br/>
<a href="/friend/friendInfo.jsp?img=<%=cartoon2.getPic()%>.gif"> 选定</a><br/>
<%}if(size2>1){%>
<a href="/friend/moreCartoon.jsp?type=2"> 更多</a><br/>
<%}%>
美女<br/>
<%if(cartoon1!=null){%>
<img src="<%=(cartoon1.getUrl())%>" alt="美女"/><br/>
<a href="/friend/friendInfo.jsp?img=<%=cartoon1.getPic()%>.gif"> 选定</a><br/>
<%}if(size1>1){%>
<a href="/friend/moreCartoon.jsp?type=1"> 更多</a><br/>
<%}%>
动物<br/>
<%if(cartoon3!=null){%>
<img src="<%=(cartoon3.getUrl())%>" alt="动物"/><br/>
<a href="/friend/friendInfo.jsp?img=<%=cartoon3.getPic()%>.gif"> 选定</a><br/>
<%}if(size3>1){%>
<a href="/friend/moreCartoon.jsp?type=3"> 更多</a><br/>
<%}%>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
