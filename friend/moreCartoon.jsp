<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.friend.FriendAction" %><%@ page import="net.joycool.wap.bean.friend.FriendCartoonBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.PageUtil" %><%response.setHeader("Cache-Control", "no-cache");%>
<%
FriendAction action = new FriendAction(request);
action.moreCartoon(request);
List list =(List)request.getAttribute("cartoonList");
String totalCount=(String)request.getAttribute("totalCount");
int totalPage=StringUtil.toInt((String)request.getAttribute("totalPage"));
int pageIndex=StringUtil.toInt((String)request.getAttribute("pageIndex"));
String prefixUrl=(String)request.getAttribute("prefixUrl");
session.setAttribute("cartoonrefresh","true");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="卡通头像">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(list!=null){
for(int i=0;i<list.size();i++){
FriendCartoonBean cartoon=(FriendCartoonBean)list.get(i);
if(cartoon!=null){%>
<img src="<%=(cartoon.getUrl())%>" alt="头像"/><br/>
<a href="/friend/friendInfo.jsp?img=<%=cartoon.getPic()%>.gif"> 选定</a><br/>
<%}}}%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, prefixUrl, true, " ", response)%><%if

(totalPage>1){%><br/><%}%>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
