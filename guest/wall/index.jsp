<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.wall.*"%>
<%! static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
WallAction action = new WallAction(request);
String tip = "";
WallBean bean = null;
UserBean user = null;
PagingBean paging = null;
List list = WallAction.service.getWallList(" visible=1 order by create_time");
paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p2");
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="乐酷用户墙"><p>
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="o" /><br/>
<%if ("".equals(tip)){
%>讲述他们在乐酷的故事<br/>
==========<br/>
<%if (list != null && list.size() > 0){
%><%--<%=paging.shuzifenye("index.jsp",false,"|",response)%>--%><%
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		bean = (WallBean)list.get(i);
		if (bean != null){
			user = UserInfoUtil.getUser(bean.getUid());
			if (user != null){
			%>★<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>:<%=bean.getShortContWml()%><a href="more.jsp?id=<%=bean.getId()%>">详</a><br/><%
			}
		}
	}%><%=paging.shuzifenye("index.jsp",false,"|",response)%><%
}
%>
==<a href="/Column.do?columnId=9438">新手请看这里</a>==<br/>
<%
} else {
	%><%=tip%><br/><a href="index.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>