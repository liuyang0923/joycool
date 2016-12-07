<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<head>
<meta http-equiv="Cache-Control" content="max-age=0" forua="true"/>
</head>


<%
String backTo = request.getParameter("backTo");
if(backTo == null){
	backTo = BaseAction.INDEX_URL;
}
int roomId=action.getParameterInt("roomId");
%>

<card title="修改聊天室">
<p align="left">
<%=BaseAction.getTop(request, response)%><br/>
<a href="EditHall.do?editType=1&amp;roomId=<%=roomId%>&amp;backTo=<%=PageUtil.getBackTo(request)%>" title="修改聊天室名称">修改聊天室名称</a><br/>
<a href="EditHall.do?editType=2&amp;roomId=<%=roomId%>&amp;backTo=<%=PageUtil.getBackTo(request)%>" title="改变房间大小">改变房间大小</a><br/>
<a href="EditHall.do?editType=3&amp;roomId=<%=roomId%>&amp;backTo=<%=PageUtil.getBackTo(request)%>" title="升级房屋的图片">升级房屋的图片</a><br/>
<a href="EditHall.do?editType=4&amp;roomId=<%=roomId%>&amp;backTo=<%=PageUtil.getBackTo(request)%>" title="修改用户授权">修改用户授权</a><br/>

<br/><br/>
<a href="<%=(backTo.replace("&", "&amp;"))%>" title="返回上一级">返回上一级</a><br/>
<a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/> 
<%=BaseAction.getBottom(request, response)%>
</p>
</card>

</wml>