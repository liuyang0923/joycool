<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action = new JCRoomChatAction(request);
action.compain(request);
UserBean toUser=(UserBean)request.getAttribute("toUser");
int userId=action.getParameterInt("userId");
int roomId=action.getParameterInt("roomId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="投诉" >
<p align="left">
<%--马长青_2006-6-22_增加顶部信息_start--%>
<%=BaseAction.getTop(request, response)%>
<%--马长青_2006-6-22_增加顶部信息_end--%>
对方:<%if(toUser.getUs2()!=null){%><%=toUser.getUs2().getHatShow()%><%}%><%=((toUser.getNickName() == null || toUser.getNickName().equals("v") || toUser.getNickName().replace(" ", "").equals("")) ? toUser.getUserName() : StringUtil.toWml(toUser.getNickName()))%><br/>
投诉理由:<br/>
<input type="text" name="content" value="向我说脏话"/><br/>
<anchor title="post">确定
  <go href="compainr.jsp?roomId=<%=roomId%>" method="post">
    <postfield name="id" value="<%=toUser.getId()%>"/>
    <postfield name="content" value="$(content)"/>
  </go>
</anchor><br/><br/>
<a href="hall.jsp?roomId=<%=roomId%>">进入聊天大厅</a><br/>     
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%--马长青_2006-6-22_增加底部信息_start--%>
<%=BaseAction.getBottom(request, response)%>
<%--马长青_2006-6-22_增加底部信息_end--%>
</p>
</card>
</wml>
