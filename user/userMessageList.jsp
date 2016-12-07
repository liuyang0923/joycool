<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.user.SendAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
SendAction action = new SendAction(request);
action.userMessageList(request);
List userMessageList=(List)request.getAttribute("userMessageList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int totalCount = ((Integer) request.getAttribute("totalCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue(); 
String prefixUrl = (String) request.getAttribute("prefixUrl");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="通知管理">
<p align="left">
<%--<%=BaseAction.getTop(request, response)%>--%>
<%
if(userMessageList.size()>0){%>
	您有<%=totalCount%>条未读通知<br/>
<%
  for(int i=0;i<userMessageList.size();i++){
  		Integer noticeId = (Integer)userMessageList.get(i);
  		NoticeBean notice = NewNoticeCacheUtil.getNotice(noticeId.intValue());
  		if(notice==null)continue;%>
		<a href="/user/userMessageResult.jsp?noticeId=<%=notice.getId()%>">删</a>
		<%=i+1%>.
		<a href="/viewNotice.jsp?noticeId=<%=notice.getId() %>&amp;backTo=<%=PageUtil.getBackTo(request)%>"><%=StringUtil.toWml(notice.getTitle())%></a><br/>
  <%}%><%String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);
		if(!"".equals(fenye)){%><%=fenye%><%}%><br/>
  <a href="/user/userMessageDel.jsp">删除所有通知</a><br/>
 <%}else{%>你没有未读取的通知消息!<br/><%}%>
<%--<%= PositionUtil.getLastModuleUrl(request, response)%><br/>--%>
<%=PositionUtil.getCurrentModuleUrl(request)%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>