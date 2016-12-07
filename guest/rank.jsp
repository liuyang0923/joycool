<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<%! static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
GuestHallAction action = new GuestHallAction(request,response);
String tip = "";
String title = "";
String topStr = "";
List list = null;
GuestUserInfo tmpUser = null;
int type = action.getParameterInt("t");		//0:财富榜;1:经验榜
GuestUserInfo guestUser = action.getGuestUser();
PagingBean paging = new PagingBean(action, 100, COUNT_PRE_PAGE, "p");
if (type < 0 || type > 1){type = 0;}
if (type == 0){
	title = "财富榜";
	topStr = "财富|<a href=\"rank.jsp?t=1\">经验</a>";
	list = GuestHallAction.service.getUserList(" 1 order by money desc,id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
} else {
	title = "经验榜";
	topStr = "<a href=\"rank.jsp\">财富</a>|经验";
	list = GuestHallAction.service.getUserList(" 1 order by `point` desc,id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=title%>"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=topStr%><br/><%
if (list != null && list.size() > 0){
	for (int i = 0 ; i < list.size() ; i++){
		tmpUser = (GuestUserInfo)list.get(i);
		if (tmpUser != null){
			%><%=i+paging.getStartIndex()+1%>.<a href="info.jsp?uid=<%=tmpUser.getId()%>"><%=tmpUser.getUserNameWml()%></a>(<%=type==0?StringUtil.bigNumberFormat(tmpUser.getMoney()) + "游戏币":String.valueOf(tmpUser.getPoint())%>)<br/><%
		}
	}
}
%><%=paging.shuzifenye("rank.jsp?t=" + type, true, "|", response)%><%
} else {
%><%=tip%><br/><%
}%>
<a href="index.jsp">返回游乐园</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>