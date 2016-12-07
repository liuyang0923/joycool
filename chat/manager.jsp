<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action = new JCRoomChatAction(request);
action.manager(request,response);
int roomId=action.getParameterInt("roomId");
Vector userList=(Vector)request.getAttribute("userList");
String result=(String)request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="用户申请进入页面">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>
<%if(result.equals("success")){%>
<%
if(userList.size()>0){
UserBean User=null;
for(int i=0;i<userList.size();i++){
User=(UserBean)userList.get(i);%>
<a href="/user/ViewUserInfo.do?roomId=<%=roomId%>&amp;userId=<%=User.getId()%>"><%if(User.getUs2()!=null){%><%=User.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(User.getNickName())%></a>|
<a href="/chat/manager.jsp?roomId=<%=roomId%>&amp;action=1&amp;userId=<%=User.getId()%>">批准进入</a>|
<a href="/chat/manager.jsp?roomId=<%=roomId%>&amp;action=2&amp;userId=<%=User.getId()%>">拒绝进入</a><br/>
<a href="/chat/hall.jsp?roomId=<%=roomId%>">返回聊天室</a>
<%}}else{%>
没有用户申请或操作已经完成!<br/>
<a href="/chat/hall.jsp?roomId=<%=roomId%>">返回聊天室</a>
<%}}else if(result.equals("failure")){%>
<%=request.getAttribute("tip")%><br/>
<a href="/chat/hall.jsp?roomId=<%=roomId%>">返回聊天室</a>
<br/>
<%}%>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>