<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<%! static int COUNT_PRE_PAGE = 10; %>
<% response.setHeader("Cache-Control","no-cache");
   GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   UserFocus focus = null;
   GuestUserInfo focusUser = null;
   GuestUserInfo guestUser = action.getGuestUser();
   if (guestUser == null){
	   response.sendRedirect("login.jsp");
	   return;
   }
   // 取得在线和离线的关注列表。放到request中。
   action.getOnlineAndOffLineList(guestUser.getId());
   List offline = (ArrayList)request.getAttribute("offline");
   int offlineCount = 0;
   if (offline != null && offline.size() > 0)
	   offlineCount = offline.size();  
   PagingBean paging = new PagingBean(action, offlineCount, COUNT_PRE_PAGE, "p");
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="更多离线关注"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
if (offline != null && offline.size() > 0){
	for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
		focus = (UserFocus)offline.get(i);
		if (focus != null){
			focusUser = GuestHallAction.getGuestUser(focus.getRightUid());
			if (focusUser != null){
				%><%=i+1%>.<a href="info.jsp?uid=<%=focusUser.getId()%>"><%=focusUser.getUserNameWml()%></a><br/><%
			}
		}
	}%><%=paging.shuzifenye("focus2.jsp",false,"|",response)%><%
}%><a href="focus.jsp">返回我的关注</a><br/><a href="index.jsp">返回游乐园</a><br/><%
} else {
	%><%=tip%><br/><a href="focus.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>