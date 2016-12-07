<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
int roomId=action.getParameterInt("roomId");

//action
JCRoomChatAction action = new JCRoomChatAction(request);
action.inviteMoreFriends(request);
//用户好友
Vector userList = (Vector) request.getAttribute("userList");
String option = (String) request.getAttribute("option");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
int start = StringUtil.toInt((String)request.getAttribute("start"));
int end = StringUtil.toInt((String)request.getAttribute("end"));

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷门户">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<!--我的好友-->
<!--zhul add code 2006/06/08 start 根据参数OPTION来判断所显示的用户类别-->
<%=option.equals("onlinefriends")?"我的在线好友":""%><%=option.equals("outlinefriends")?"我的离线好友":""%><%=option.equals("strange")?"陌生人":""%><br/>
<!--zhul add code 2006/06/08 end-->
--------<br/>
<%
int count = userList.size();
for(int i = start; i < end; i ++){
	if(i>count-1) break;
	String userId = (String) userList.get(i);
	UserBean user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	if(user==null) continue;
%>
<%=(i + 1)%>.<a href="invite.jsp?roomId=<%=roomId%>&amp;userId=<%=user.getId()%>"><%if(user!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%></a>
<br/>
<%
}
%>
<!--zhul add code 2006/06/08 start 做分页-->
<%=PageUtil.shuzifenye(Integer.parseInt(totalPage),Integer.parseInt(pageIndex),"/chat/inviteMoreFriends.jsp?roomId=" + roomId+ "&amp;option="+option,true," ",response)%>
<!--zhul add code 2006/06/08 end -->
<br/>

<a href="/chat/inviteFriends.jsp" title="进入">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>