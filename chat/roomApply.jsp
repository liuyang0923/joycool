<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);
action.roomApply(request);
Vector chatList = (Vector)request.getAttribute("chatList");
Vector roomApplyList = (Vector)request.getAttribute("roomApplyList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
String backTo = (String) request.getAttribute("backTo");
String orderBy = (String)request.getAttribute("orderBy");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="申请自建聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%>
申请列表<br/>
---------<br/>
注意：每个用户只能申请或者支持一个聊天室，并且申请或者支持之后不能退出，请慎重选择!<br/>
 <%if(orderBy.equals("vote_count")){%>
<a href="/chat/roomApply.jsp?orderBy=id">按时间</a>|按人气<br/>
    <%}else{%>
按时间|<a href="/chat/roomApply.jsp?orderBy=vote_count">按人气</a><br/>
    <%}%>
<a href="/chat/myApply.jsp">我也要申请</a><br/>
<%
RoomApplyBean roomApply=null;
UserBean user=null;
for(int i = 0; i < roomApplyList.size(); i ++){
	roomApply = (RoomApplyBean)roomApplyList.get(i);
//	user=action.getUser("id="+roomApply.getUserId());
	//zhul 2006-10-12_优化用户信息查询
	user = UserInfoUtil.getUser(roomApply.getUserId());	
%>
<%=i+1%>.<a href="/chat/roomEnter.jsp?applyId=<%=roomApply.getId()%>"><%=StringUtil.toWml(roomApply.getRoomName())%></a>(<%=roomApply.getVoteCount()%>人支持|<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%><%if(roomApply.getMark()==1){%>|已批准<%}%>)<br/>
<%}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
第<%=pageIndex+1%>页|共<%=totalPageCount%>页
<%}%>
<br/>
==聊天室动态==<br/>
<%
JCRoomContentBean content = null;
JCRoomChatAction chatAction=new JCRoomChatAction();
for(int i = 0; i < chatList.size(); i ++){
	content = (JCRoomContentBean)chatList.get(i);
	//liuyi 2007-01-03 聊天内容截断问题 start
	if(content==null)continue;
	
	JCRoomContentBean contentCopy = new JCRoomContentBean();
	contentCopy.setId(content.getId());
	contentCopy.setFromId(content.getFromId());
	contentCopy.setToId(content.getToId());
	contentCopy.setFromNickName(content.getFromNickName());
	contentCopy.setToNickName(content.getToNickName());
	contentCopy.setContent(content.getContent());
	contentCopy.setAttach(content.getAttach());
	contentCopy.setSendDateTime(content.getSendDateTime());
	contentCopy.setIsPrivate(content.getIsPrivate());
	contentCopy.setRoomId(content.getRoomId());
	contentCopy.setMark(content.getMark());
	
	if(contentCopy.getContent().length()>10) { 
	    String content1=contentCopy.getContent().substring(0,10)+"...";
	    contentCopy.setContent(content1);
	}
	
%>
<%=chatAction.getMessageDisplay(contentCopy, request, response)%><br/>
<%
}
//liuyi 2007-01-03 聊天内容截断问题 end
%>
<br/>
<a href="/chat/hall.jsp">返回聊天大厅</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>