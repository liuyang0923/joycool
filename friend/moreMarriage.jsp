<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.friend.FriendMarriageBean"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.Constants"%><%@ page errorPage=""%><%
response.setHeader("Cache-Control","no-cache");
FriendAction action=new FriendAction(request);
action.getMarriage(request);%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="结婚礼堂">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
    String nowtime=(String)request.getAttribute("nowtime");
    String t=(String)request.getAttribute("t");
    String oldtime=(String)request.getAttribute("oldtime");
	String perPage=(String)request.getAttribute("NUM_PER_PAGE");
    String totalCount=(String)request.getAttribute("totalCount");
    int totalPage=StringUtil.toInt((String)request.getAttribute("totalPage"));
    int pageIndex=StringUtil.toInt((String)request.getAttribute("pageIndex"));
    Vector toMarriage=(Vector)request.getAttribute("toMarriage");   
	FriendMarriageBean marriage=null;
	if(toMarriage!=null)
	{int count = toMarriage.size();
    for(int i = 0; i < count; i ++){
	 marriage = (FriendMarriageBean) toMarriage.get(i);
	 int fromId=marriage.getFromId();
	 int toId=marriage.getToId();
	 UserBean from=(UserBean)UserInfoUtil.getUser(fromId);
	 UserBean to=(UserBean)UserInfoUtil.getUser(toId);
	 		if(from==null||to==null)
		continue;
     if(nowtime!=null){	%>
<a href="/friend/redbag.jsp?marriageId=<%=marriage.getId()%>">
<%=StringUtil.toWml(from.getNickName())%>和<%=StringUtil.toWml(to.getNickName())%>的婚礼
<br/>
</a>
<%}
else if(t!=null)
{%>
<%
String time=marriage.getMarriageDatetime();
	String showTime=time.substring(5,7)+"月"+time.substring(8,10)+"日"+time.substring(11,13)+"点:";%>
<%=showTime%><br/>
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=fromId%>"><%=StringUtil.toWml(from.getNickName())%></a>和
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=toId%>"><%=StringUtil.toWml(to.getNickName())%></a>的婚礼<br/>
<%}
else if(oldtime!=null)
{%>
<%String time=marriage.getMarriageDatetime();
	String showTime=time.substring(0,4)+"年"+time.substring(5,7)+"月"+time.substring(8,10)+"日"+time.substring(11,13)+"点:";%>
<%=showTime%><br/>
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=fromId%>"><%=StringUtil.toWml(from.getNickName())%></a>和
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=toId%>"><%=StringUtil.toWml(to.getNickName())%></a>的
<a href="/friend/review.jsp?marriageId=<%=marriage.getId()%>">婚礼</a>
<br/>
<%}
else{
}
}}%>
<%if(nowtime!=null){%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "moreMarriage.jsp?nowtime=1", true, " ", response)%><%if(totalPage>1){%><br/><%}%>
<%}else if(t!=null){%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "moreMarriage.jsp?t=1", true, " ", response)%><%if(totalPage>1){%><br/><%}%>
<%}else if(oldtime!=null){%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "moreMarriage.jsp?oldtime=1", true, " ", response)%><%if(totalPage>1){%><br/><%}%>
<%}else{%>
<%=PageUtil.shuzifenye(totalPage, pageIndex, "moreMarriage.jsp", false, " ", response)%><%if(totalPage>1){%><br/><%}%>
<%}%>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>