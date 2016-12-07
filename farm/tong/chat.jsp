<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%! static int NUMBER_OF_PAGE = 10;%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
TongUserBean tongUser = farmUser.getTongUser();
String url = ("chat.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="门派闲聊">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%
if(tongUser != null){
SimpleChatLog sc = SimpleChatLog.getChatLog(tongUser.getTongId());
PagingBean paging = new PagingBean(action, sc.size(),NUMBER_OF_PAGE,"p");
	String content = action.getParameterNoEnter("content");
	if(content != null) {		// 发言
		sc.add(farmUser.getNameWml() + ":" + StringUtil.toWml(content) + "(" + DateUtil.formatTime(new Date()) + ")");
	}
	action.setAttribute2("tongchat",new Integer(sc.getChatTotal()));
%>
<input name="gchat"  maxlength="100"/>
<anchor title="确定">发言
<go href="<%=url%>" method="post">
    <postfield name="content" value="$gchat"/>
</go></anchor>|
<a href="<%=url%>">刷新</a>|<a href="../map.jsp">返回场景</a><br/>
<%=sc.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%>
<%=paging.shuzifenye("chat.jsp", false, "|", response)%>
<%}else{%>
你不属于任何门派<br/>
<%}%>

<a href="tong.jsp">查看门派</a>|<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>