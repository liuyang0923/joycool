<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.user.SendAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
SendAction action = new SendAction(request);
action.receiveHistory(request,response);
UserBean loginUser = action.getLoginUser();
Vector recordList=(Vector)request.getAttribute("recordList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
String backTo = (String) request.getAttribute("backTo");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我接收到的动作">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(loginUser!=null){
	if(recordList!=null && recordList.size()>0){
		ActionRecordBean record=null;
		String fromUserName=null;
		String content=null;
		String date=null;
		String datetime[]=null;
		UserBean user=null;
		for(int i=0;i<recordList.size();i++){
			record=(ActionRecordBean)recordList.get(i);
			//mcq_更改显示用户昵称  时间 2006-6-19
			user=action.getUser(String.valueOf(record.getFromId()));
			fromUserName=StringUtil.toWml(user.getNickName());
			if(user.getUs2()!=null){
				fromUserName=user.getUs2().getHatShow()+fromUserName;
			}
			content=StringUtil.toWml(action.getRankAction(record.getActionId()).getReceiveMessage());
			content=content.replace("XXX","<a href=\"/user/ViewUserInfo.do?userId="+record.getFromId()+"\">"+fromUserName+"</a>");
			//mcq_end
			date=record.getActionDatetime();
			datetime=date.split(" ");
%><%=i+1%>.<%=content%> <%=datetime[0]%><br/>
<%}String fenye1 = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye1)){
%>
<%=fenye1%><br/>
<%
 }}else{%>没有用户给你发送动作<br/><%}}else{%>您还没有登陆<br/>
<a href="/user/login.jsp?backTo=http://wap.joycool.net/user/receiveHistory.jsp">登陆</a><br/>
<%}%>
<br/>
<a href="/user/sendHistory.jsp?backTo=http://wap.joycool.net/user/messageIndex.jsp">我发送的动作</a><br/>
<a href="/user/messageIndex.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>