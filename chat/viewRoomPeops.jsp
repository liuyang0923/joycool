<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*,java.util.*,net.joycool.wap.action.chat.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.chat.JCRoomOnlineBean" %><?xml version="1.0" encoding="UTF-8"?>
<%
response.setHeader("Cache-Control","no-cache");
String backTo=request.getParameter("backTo");
int roomId=action.getParameterInt("roomId");
if(backTo==null) backTo="/chat/hall.jsp?roomId="+roomId;
JCRoomChatAction  roomAction=new JCRoomChatAction(request);
roomAction.viewRoomPeops(request,response);
String tag=(String)request.getAttribute("tag");
if(tag!=null){
	//response.sendRedirect(("/chat/hall.jsp"));
	BaseAction.sendRedirect("/chat/hall.jsp", response);
	return;
}
String perPage=(String)request.getAttribute("NUM_PER_PAGE");
String totalCount=(String)request.getAttribute("totalCount");
String totalPage=(String)request.getAttribute("totalPage");
String pageIndex=(String)request.getAttribute("pageIndex");
Vector peopleList=(Vector)request.getAttribute("peopleList");

%>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="房间内的人">
<p align="left">
<%=BaseAction.getTop(request, response)%>
房间里所有的人(<%=totalCount%>)：<br/>
<%
for(int i = 0; i < peopleList.size(); i ++){
	JCRoomOnlineBean roomOnlineUser = (JCRoomOnlineBean) peopleList.get(i);
	UserBean user=UserInfoUtil.getUser(roomOnlineUser.getUserId());
%>
<%=(Integer.parseInt(pageIndex)*Integer.parseInt(perPage)+i + 1)%>.<a href="/user/ViewUserInfo.do?roomId=<%=roomId%>&amp;backTo=<%=StringUtil.getBackTo(request) %>&amp;userId=<%=user.getId()%>"><%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%><%=StringUtil.toWml(user.getNickName())%></a>
<br/>
<%
}
%>
<%=PageUtil.shuzifenye(Integer.parseInt(totalPage),Integer.parseInt(pageIndex),"viewRoomPeops.jsp?roomId="+roomId,true," ",response)%>
<br/>
<a href="<%=(backTo.replace("&","&amp;"))%>">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>