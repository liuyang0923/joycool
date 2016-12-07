<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);

//如果不是管理员，则返回聊天大厅
action.isManager(request,response);
action.deleteOrAddRoomManager(request);


String url=("roomManager.jsp?roomId="+request.getParameter("roomId")+"&amp;managerIndex="+request.getParameter("managerIndex")
+"&amp;friendIndex="+request.getParameter("friendIndex")
+"&amp;onlineIndex="+request.getParameter("onlineIndex"));


int deleteOrAdd=Integer.parseInt(request.getParameter("deleteOrAdd"));
String result=(String)request.getAttribute("result");
UserBean user=(UserBean)request.getAttribute("admin");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){ %>
<card title="设置管理员" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
设置房间管理员需要15000乐币,您没有足够的乐币!（3秒钟跳转）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if (deleteOrAdd==1){%>
<card title="设置管理员" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%>已经被批准成为管理员！<br/>
并扣除您15000乐币.（3秒钟跳转）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="删除管理员" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%><br/>
已经被取消管理员权限！（3秒钟跳转）<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%} %>
</wml>