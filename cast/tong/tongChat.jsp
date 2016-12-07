<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*"%><%!
static int NUMBER_OF_PAGE = 10;%><%
	
	
	CastleAction action = new CastleAction(request);
	CastleUserBean user = action.getCastleUser();
	int id = user.getTong();
	TongBean tong = CastleUtil.getTong(id);
	SimpleChatLog sc = SimpleChatLog.getChatLog("ca"+id);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="联盟讨论"><p>
<%PagingBean paging = new PagingBean(action, sc.size(),NUMBER_OF_PAGE,"p");
	String content = action.getParameterNoEnter("content");
	if(content != null) {		// 发言
		sc.add(user.getNameWml() + ":" + StringUtil.toWml(content) + "(" + DateUtil.formatTime(new Date()) + ")");
	}
	action.setAttribute2("castle",new Integer(sc.getChatTotal()));
%>
<input name="gchat"  maxlength="100"/>
<anchor title="确定">发言
<go href="tongChat.jsp" method="post">
    <postfield name="content" value="$gchat"/>
</go></anchor>|
<a href="tongChat.jsp">刷新</a>|<a href="tong.jsp">返回联盟</a><br/>
<%=sc.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%>
<%=paging.shuzifenye("tongChat.jsp", false, "|", response)%>

<a href="tong.jsp">返回联盟</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>