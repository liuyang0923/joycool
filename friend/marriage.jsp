<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.friend.FriendMarriageBean"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.Constants"%><%
response.setHeader("Cache-Control","no-cache");%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="结婚礼堂">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
    UserBean loginUser= (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
	FriendAction action=new FriendAction(request);	
	action.marriage(request);
		int totalKinescope=StringUtil.toInt((String)request.getAttribute("totalKinescope"));
		int tototal=StringUtil.toInt((String)request.getAttribute("tototal"));
		int total=StringUtil.toInt((String)request.getAttribute("total"));
	if(request.getParameter("cancelId")!=null)
	{
	int cancelId=StringUtil.toInt(request.getParameter("cancelId"));
	action.cancelMarriage(cancelId);
	}
	FriendMarriageBean marriage=null;
    Vector	vec=null;

	if(action.isOrderTime()>0 )//在结婚表里，且未过结婚日期
    {
		marriage=action.getOnesMarriage(action.isOrderTime());
		int fromId = marriage.getFromId();
		int toId = marriage.getToId();
%>
<%      
        if(loginUser != null && (loginUser.getId()==fromId || loginUser.getId()==toId)){ %>
恭喜两位结为夫妻！<br/>
祝你们白头偕老、永结同心！<br/>	
<%      
        } 
%>
<a href="/friend/engage.jsp?marriageId=<%=action.isOrderTime()%>">是否举行婚礼?</a><br/>
<%}%><br/>
正在举行的婚礼：<br/>
<%    vec=action.getTopMarriage();	
for(int j=0;j<vec.size();j++){
	marriage=(FriendMarriageBean)vec.get(j);
	int fromId=marriage.getFromId();
	int toId=marriage.getToId();
	UserBean from=(UserBean)UserInfoUtil.getUser(fromId);
		UserBean to=(UserBean)UserInfoUtil.getUser(toId);
		if(from==null||to==null)
		continue;
		%>
<a href="/friend/redbag.jsp?marriageId=<%=marriage.getId()%>">
<%=StringUtil.toWml(from.getNickName())%>和<%=StringUtil.toWml(to.getNickName())%>的婚礼<br/>
</a>
<%}
if(total>3)
{%>
<a href="/friend/moreMarriage.jsp?nowtime=1">更多>></a><br/>
<%}%>
即将举行的婚礼：<br/>
<%
vec=action.getTopToMarriage();	
 for(int j=0;j<vec.size();j++){
	marriage=(FriendMarriageBean)vec.get(j);
	int fromId=marriage.getFromId();
	int toId=marriage.getToId();
	String time=marriage.getMarriageDatetime();
	String showTime=time.substring(5,7)+"月"+time.substring(8,10)+"日"+time.substring(11,13)+"点:";
	UserBean from=(UserBean)UserInfoUtil.getUser(fromId);
	UserBean to=(UserBean)UserInfoUtil.getUser(toId);
		if(from==null||to==null)
		continue;%>
	<%=showTime%><br/>
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=fromId%>"><%=StringUtil.toWml(from.getNickName())%></a>
和
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=toId%>"><%=StringUtil.toWml(to.getNickName())%></a>的婚礼<br/>
<%}
if(tototal>3)
{%>
<a href="/friend/moreMarriage.jsp?t=1">更多>></a><br/>
<%}%>
结婚录像：<br/>
<%vec=action.getTopMarriageKinescope();	
for(int j=0;j<vec.size();j++){
	marriage=(FriendMarriageBean)vec.get(j);
	int fromId=marriage.getFromId();
	int toId=marriage.getToId();
	String time=marriage.getMarriageDatetime();
	String showTime=time.substring(0,4)+"年"+time.substring(5,7)+"月"+time.substring(8,10)+"日"+time.substring(11,13)+"点:";
	UserBean from=(UserBean)UserInfoUtil.getUser(fromId);
		UserBean to=(UserBean)UserInfoUtil.getUser(toId);
		if(from==null||to==null)
		continue;%>
		<%=showTime%><br/>

<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=fromId%>"><%=StringUtil.toWml(from.getNickName())%></a>和
<a href="/user/ViewUserInfo.do?roomId=0&amp;userId=<%=toId%>"><%=StringUtil.toWml(to.getNickName())%></a>的
<a href="/friend/review.jsp?marriageId=<%=marriage.getId()%>">婚礼</a>
<br/>
<%}
if(totalKinescope>3)
{%>
<a href="/friend/moreMarriage.jsp?oldtime=1">更多>></a><br/>
<%}%>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>